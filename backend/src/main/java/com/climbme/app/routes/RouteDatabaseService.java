package com.climbme.app.routes;

import com.climbme.app.auth.UserAccount;
import com.climbme.app.climbing.ClimbingAttempt;
import com.climbme.app.climbing.ClimbingAttemptRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteDatabaseService {
    private final ClimbingAttemptRepository attempts;
    private final RouteStatusOverrideRepository overrides;
    public RouteDatabaseService(ClimbingAttemptRepository attempts, RouteStatusOverrideRepository overrides) { this.attempts = attempts; this.overrides = overrides; }

    @Transactional(readOnly = true)
    public List<RouteSummaryResponse> list(UserAccount owner) {
        Set<String> abandoned = overrides.findByUserId(owner.getId()).stream().filter(item -> item.getStatus() == RouteStatusOverride.Status.ABANDONED).map(RouteStatusOverride::getRouteKey).collect(Collectors.toSet());
        Map<String, List<ClimbingAttempt>> groups = attempts.findByUserIdOrderByClimbedOnDescIdDesc(owner.getId()).stream().collect(Collectors.groupingBy(this::key, LinkedHashMap::new, Collectors.toList()));
        return groups.values().stream().map(group -> response(group, abandoned.contains(key(group.getFirst())))).sorted(Comparator.comparing(RouteSummaryResponse::firstTryDate).reversed()).toList();
    }

    @Transactional
    public void abandon(UserAccount owner, RouteIdentity request) {
        String key = key(request.location(), request.sector(), request.routeName());
        overrides.findByUserIdAndRouteKey(owner.getId(), key).orElseGet(() -> overrides.save(new RouteStatusOverride(owner, key)));
    }

    private RouteSummaryResponse response(List<ClimbingAttempt> group, boolean abandoned) {
        List<ClimbingAttempt> chronological = group.stream().sorted(Comparator.comparing(ClimbingAttempt::getClimbedOn).thenComparing(ClimbingAttempt::getId)).toList();
        ClimbingAttempt first = chronological.getFirst();
        ClimbingAttempt sent = chronological.stream().filter(ClimbingAttempt::isSent).findFirst().orElse(null);
        String status = abandoned ? "ABANDONED" : sent != null ? "SENT" : "PROJECT";
        return new RouteSummaryResponse(first.getLocation(), first.getSector(), first.getRouteName(), first.getGrade().label(), first.getClimbedOn(), sent == null ? null : sent.getClimbedOn(), chronological.size(), sent == null ? null : sent.getStyle().name(), status);
    }

    private String key(ClimbingAttempt attempt) { return key(attempt.getLocation(), attempt.getSector(), attempt.getRouteName()); }
    private String key(String location, String sector, String routeName) { return String.join("|", location, sector, routeName).trim().toLowerCase(Locale.ROOT); }
    public record RouteIdentity(String location, String sector, String routeName) { }
}
