package com.climbme.app.climbing;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ClimbingAttemptRequest(
        @NotNull LocalDate climbedOn,
        @NotBlank @Size(max = 120) String location,
        @NotBlank @Size(max = 120) String sector,
        @NotBlank @Size(max = 160) String routeName,
        @NotBlank String grade,
        @Min(1) @Max(2000) Integer lengthMeters,
        @NotNull ClimbingAttempt.Style style,
        @Min(1) @Max(999) Integer attemptNumber,
        boolean sent,
        @Min(1) @Max(600) Integer timeOnRouteMinutes,
        @Min(1) @Max(10) Integer rpe,
        @Size(max = 30) String conditions,
        @Size(max = 120) String partner,
        @Size(max = 2000) String notes) { }
