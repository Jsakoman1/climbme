package com.climbme.app.climbing;

import java.time.LocalDate;

public record ClimbingAttemptResponse(Long id, LocalDate climbedOn, String location, String sector, String routeName,
                                      String grade, Integer lengthMeters, ClimbingAttempt.Style style, int attemptNumber,
                                      boolean sent, Integer timeOnRouteMinutes, Integer rpe, String conditions,
                                      String partner, String notes) {
    static ClimbingAttemptResponse from(ClimbingAttempt attempt) {
        return new ClimbingAttemptResponse(attempt.getId(), attempt.getClimbedOn(), attempt.getLocation(), attempt.getSector(),
                attempt.getRouteName(), attempt.getGrade().label(), attempt.getLengthMeters(), attempt.getStyle(),
                attempt.getAttemptNumber(), attempt.isSent(), attempt.getTimeOnRouteMinutes(), attempt.getRpe(),
                attempt.getConditions(), attempt.getPartner(), attempt.getNotes());
    }
}
