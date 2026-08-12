package com.climbme.app.auth;

import com.climbme.app.climbing.ClimbingAttempt;
import com.climbme.app.climbing.ClimbingAttemptRepository;
import com.climbme.app.routes.RouteStatusOverride;
import com.climbme.app.routes.RouteStatusOverrideRepository;
import com.climbme.app.training.TrainingSession;
import com.climbme.app.training.TrainingSessionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.web.context.SecurityContextRepository;

@Service
public class AuthService {
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final Duration ATTEMPT_WINDOW = Duration.ofMinutes(15);

    private final UserAccountRepository accounts;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final ClimbingAttemptRepository attempts;
    private final TrainingSessionRepository training;
    private final RouteStatusOverrideRepository routeOverrides;
    private final Map<String, ArrayList<Instant>> failedAttempts = new ConcurrentHashMap<>();

    public AuthService(
            UserAccountRepository accounts,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository,
            ClimbingAttemptRepository attempts,
            TrainingSessionRepository training,
            RouteStatusOverrideRepository routeOverrides) {
        this.accounts = accounts;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.attempts = attempts;
        this.training = training;
        this.routeOverrides = routeOverrides;
    }

    @Transactional
    public AccountView register(String rawEmail, String password, HttpServletRequest request, HttpServletResponse response) {
        String key = request.getRemoteAddr();
        requireAttemptCapacity(key);
        String email = normalizeEmail(rawEmail);
        if (accounts.findByEmail(email).isPresent()) {
            recordFailure(key);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account already exists for this email address.");
        }
        accounts.save(new UserAccount(email, passwordEncoder.encode(password)));
        clearFailures(key);
        return authenticate(email, password, request, response, key);
    }

    public AccountView login(String rawEmail, String password, HttpServletRequest request, HttpServletResponse response) {
        String key = request.getRemoteAddr();
        requireAttemptCapacity(key);
        return authenticate(normalizeEmail(rawEmail), password, request, response, key);
    }

    @Transactional
    public void changePassword(UserAccount account, String currentPassword, String newPassword) {
        if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect.");
        }
        account.changePassword(passwordEncoder.encode(newPassword));
        accounts.save(account);
    }

    public void logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
    }

    @Transactional
    public AccountExport export(UserAccount account) {
        Long userId = account.getId();
        return new AccountExport(
                new AccountView(account.getId(), account.getEmail()),
                attempts.findByUserIdOrderByClimbedOnDescIdDesc(userId).stream().map(AttemptExport::from).toList(),
                training.findByUserIdOrderByTrainedOnDescIdDesc(userId).stream().map(TrainingExport::from).toList(),
                routeOverrides.findByUserId(userId).stream().map(RouteOverrideExport::from).toList());
    }

    @Transactional
    public void deleteAccount(UserAccount account, String currentPassword, HttpServletRequest request) {
        if (!passwordEncoder.matches(currentPassword, account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect.");
        }
        Long userId = account.getId();
        routeOverrides.deleteByUserId(userId);
        attempts.deleteByUserId(userId);
        training.deleteByUserId(userId);
        accounts.deleteById(userId);
        logout(request);
    }

    private AccountView authenticate(String email, String password, HttpServletRequest request, HttpServletResponse response, String key) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(email, password));
            SecurityContext context = new SecurityContextImpl(authentication);
            SecurityContextHolder.setContext(context);
            request.getSession(true);
            securityContextRepository.saveContext(context, request, response);
            clearFailures(key);
            UserAccount account = (UserAccount) authentication.getPrincipal();
            return new AccountView(account.getId(), account.getEmail());
        } catch (BadCredentialsException exception) {
            recordFailure(key);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email or password is incorrect.");
        }
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private void requireAttemptCapacity(String key) {
        if (failuresFor(key).size() >= MAX_FAILED_ATTEMPTS) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many failed attempts. Try again in 15 minutes.");
        }
    }

    private void recordFailure(String key) { failuresFor(key).add(Instant.now()); }
    private void clearFailures(String key) { failedAttempts.remove(key); }

    private ArrayList<Instant> failuresFor(String key) {
        ArrayList<Instant> attempts = failedAttempts.computeIfAbsent(key, ignored -> new ArrayList<>());
        synchronized (attempts) {
            Instant threshold = Instant.now().minus(ATTEMPT_WINDOW);
            attempts.removeIf(attempt -> attempt.isBefore(threshold));
            return attempts;
        }
    }

    public record AccountView(Long id, String email) { }

    public record AccountExport(AccountView account, java.util.List<AttemptExport> attempts,
                                java.util.List<TrainingExport> trainingSessions,
                                java.util.List<RouteOverrideExport> routeStatusOverrides) { }
    public record AttemptExport(java.time.LocalDate climbedOn, String location, String sector, String routeName,
                                String grade, Integer lengthMeters, String style, int attemptNumber, boolean sent,
                                Integer timeOnRouteMinutes, Integer rpe, String conditions, String partner, String notes) {
        static AttemptExport from(ClimbingAttempt attempt) {
            return new AttemptExport(attempt.getClimbedOn(), attempt.getLocation(), attempt.getSector(), attempt.getRouteName(),
                    attempt.getGrade().label(), attempt.getLengthMeters(), attempt.getStyle().name(), attempt.getAttemptNumber(),
                    attempt.isSent(), attempt.getTimeOnRouteMinutes(), attempt.getRpe(), attempt.getConditions(), attempt.getPartner(), attempt.getNotes());
        }
    }
    public record TrainingExport(java.time.LocalDate trainedOn, String sessionType, int durationMinutes,
                                 Integer strength, Integer endurance, Integer mobility, String notes) {
        static TrainingExport from(TrainingSession session) {
            return new TrainingExport(session.getTrainedOn(), session.getSessionType().name(), session.getDurationMinutes(),
                    session.getStrength(), session.getEndurance(), session.getMobility(), session.getNotes());
        }
    }
    public record RouteOverrideExport(String routeKey, String status) {
        static RouteOverrideExport from(RouteStatusOverride override) {
            return new RouteOverrideExport(override.getRouteKey(), override.getStatus().name());
        }
    }
}
