package com.climbme.app.training;
import com.climbme.app.auth.UserAccount;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TrainingSessionRepository extends JpaRepository<TrainingSession,Long>{ List<TrainingSession> findByUserIdOrderByTrainedOnDescIdDesc(Long id); Optional<TrainingSession> findByIdAndUserId(Long id,Long userId); void deleteByUserId(Long userId); }
