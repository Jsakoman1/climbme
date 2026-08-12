package com.climbme.app.routes;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteStatusOverrideRepository extends JpaRepository<RouteStatusOverride, Long> {
    List<RouteStatusOverride> findByUserId(Long userId);
    Optional<RouteStatusOverride> findByUserIdAndRouteKey(Long userId, String routeKey);
    void deleteByUserId(Long userId);
}
