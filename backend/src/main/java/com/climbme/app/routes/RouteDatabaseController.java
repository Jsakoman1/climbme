package com.climbme.app.routes;

import com.climbme.app.auth.UserAccount;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/routes")
public class RouteDatabaseController {
    private final RouteDatabaseService routes;
    public RouteDatabaseController(RouteDatabaseService routes) { this.routes = routes; }
    @GetMapping public List<RouteSummaryResponse> list(@AuthenticationPrincipal UserAccount owner) { return routes.list(owner); }
    @PostMapping("/abandon") public ResponseEntity<Void> abandon(@AuthenticationPrincipal UserAccount owner, @Valid @RequestBody AbandonRequest request) { routes.abandon(owner, new RouteDatabaseService.RouteIdentity(request.location(), request.sector(), request.routeName())); return ResponseEntity.noContent().build(); }
    public record AbandonRequest(@NotBlank String location, @NotBlank String sector, @NotBlank String routeName) { }
}
