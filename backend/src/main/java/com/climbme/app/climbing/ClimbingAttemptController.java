package com.climbme.app.climbing;

import com.climbme.app.auth.UserAccount;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attempts")
public class ClimbingAttemptController {
    private final ClimbingAttemptService attempts;
    public ClimbingAttemptController(ClimbingAttemptService attempts) { this.attempts = attempts; }
    @GetMapping public List<ClimbingAttemptResponse> list(@AuthenticationPrincipal UserAccount owner) { return attempts.list(owner); }
    @PostMapping public ResponseEntity<ClimbingAttemptResponse> create(@AuthenticationPrincipal UserAccount owner, @Valid @RequestBody ClimbingAttemptRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(attempts.create(owner, request)); }
    @PutMapping("/{id}") public ClimbingAttemptResponse update(@AuthenticationPrincipal UserAccount owner, @PathVariable Long id, @Valid @RequestBody ClimbingAttemptRequest request) { return attempts.update(owner, id, request); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@AuthenticationPrincipal UserAccount owner, @PathVariable Long id) { attempts.delete(owner, id); return ResponseEntity.noContent().build(); }
}
