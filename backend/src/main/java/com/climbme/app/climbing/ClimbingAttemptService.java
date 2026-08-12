package com.climbme.app.climbing;

import com.climbme.app.auth.UserAccount;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClimbingAttemptService {
    private final ClimbingAttemptRepository attempts;

    public ClimbingAttemptService(ClimbingAttemptRepository attempts) { this.attempts = attempts; }

    @Transactional
    public ClimbingAttemptResponse create(UserAccount owner, ClimbingAttemptRequest request) {
        validateSemantics(request);
        int nextAttempt = (int) attempts.countByUserIdAndLocationIgnoreCaseAndSectorIgnoreCaseAndRouteNameIgnoreCase(
                owner.getId(), request.location().trim(), request.sector().trim(), request.routeName().trim()) + 1;
        return ClimbingAttemptResponse.from(attempts.save(new ClimbingAttempt(owner, request, nextAttempt)));
    }

    @Transactional(readOnly = true)
    public List<ClimbingAttemptResponse> list(UserAccount owner) {
        return attempts.findByUserIdOrderByClimbedOnDescIdDesc(owner.getId()).stream().map(ClimbingAttemptResponse::from).toList();
    }

    @Transactional
    public ClimbingAttemptResponse update(UserAccount owner, Long id, ClimbingAttemptRequest request) {
        validateSemantics(request);
        ClimbingAttempt attempt = owned(owner, id);
        attempt.apply(request, attempt.getAttemptNumber());
        return ClimbingAttemptResponse.from(attempt);
    }

    @Transactional
    public void delete(UserAccount owner, Long id) { attempts.delete(owned(owner, id)); }

    private ClimbingAttempt owned(UserAccount owner, Long id) {
        return attempts.findByIdAndUserId(id, owner.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Climbing attempt not found."));
    }

    private void validateSemantics(ClimbingAttemptRequest request) {
        if (request.style() == ClimbingAttempt.Style.PROJECT && request.sent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A project attempt cannot be marked as sent.");
        }
        try { FrenchGrade.fromLabel(request.grade()); }
        catch (IllegalArgumentException exception) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage()); }
    }
}
