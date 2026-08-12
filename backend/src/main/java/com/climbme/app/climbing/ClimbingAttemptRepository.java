package com.climbme.app.climbing;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClimbingAttemptRepository extends JpaRepository<ClimbingAttempt, Long> {
    List<ClimbingAttempt> findByUserIdOrderByClimbedOnDescIdDesc(Long userId);
    Optional<ClimbingAttempt> findByIdAndUserId(Long id, Long userId);
    long countByUserIdAndLocationIgnoreCaseAndSectorIgnoreCaseAndRouteNameIgnoreCase(Long userId, String location, String sector, String routeName);
}
