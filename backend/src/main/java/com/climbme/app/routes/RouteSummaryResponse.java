package com.climbme.app.routes;

import java.time.LocalDate;

public record RouteSummaryResponse(String location, String sector, String routeName, String grade,
                                   LocalDate firstTryDate, LocalDate sendDate, int totalAttempts,
                                   String styleSent, String status) { }
