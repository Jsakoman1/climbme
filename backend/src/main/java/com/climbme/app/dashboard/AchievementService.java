package com.climbme.app.dashboard;
import com.climbme.app.climbing.*;import java.util.*;
import org.springframework.stereotype.Service;
@Service public class AchievementService{public List<DashboardResponse.Achievement> derive(List<ClimbingAttempt> attempts){Set<String> seen=new HashSet<>();return attempts.stream().filter(ClimbingAttempt::isSent).sorted(Comparator.comparing(ClimbingAttempt::getClimbedOn).thenComparing(ClimbingAttempt::getId)).filter(a->seen.add(a.getGrade().label()+"|"+a.getStyle())).map(a->new DashboardResponse.Achievement(a.getClimbedOn().toString(),"First "+a.getGrade().label()+" "+a.getStyle().name().toLowerCase(),a.getGrade().label(),a.getStyle().name(),a.getLocation())).toList();}}
