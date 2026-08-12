package com.climbme.app.climbing;

import com.climbme.app.auth.UserAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "climbing_attempts")
public class ClimbingAttempt {
    public enum Style { ONSIGHT, FLASH, REDPOINT, PINKPOINT, TOPROPE, PROJECT }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;
    @Column(name = "climbed_on", nullable = false) private LocalDate climbedOn;
    @Column(nullable = false, length = 120) private String location;
    @Column(nullable = false, length = 120) private String sector;
    @Column(name = "route_name", nullable = false, length = 160) private String routeName;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private FrenchGrade grade;
    @Column(name = "length_meters") private Integer lengthMeters;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Style style;
    @Column(name = "attempt_number", nullable = false) private int attemptNumber;
    @Column(nullable = false) private boolean sent;
    @Column(name = "time_on_route_minutes") private Integer timeOnRouteMinutes;
    @Column private Integer rpe;
    @Column(length = 30) private String conditions;
    @Column(length = 120) private String partner;
    @Column(length = 2000) private String notes;

    protected ClimbingAttempt() { }

    public ClimbingAttempt(UserAccount user, ClimbingAttemptRequest request, int attemptNumber) {
        this.user = user;
        apply(request, attemptNumber);
    }

    public void apply(ClimbingAttemptRequest request, int fallbackAttemptNumber) {
        climbedOn = request.climbedOn();
        location = request.location().trim();
        sector = request.sector().trim();
        routeName = request.routeName().trim();
        grade = FrenchGrade.fromLabel(request.grade());
        lengthMeters = request.lengthMeters();
        style = request.style();
        attemptNumber = request.attemptNumber() == null ? fallbackAttemptNumber : request.attemptNumber();
        sent = request.sent();
        timeOnRouteMinutes = request.timeOnRouteMinutes();
        rpe = request.rpe();
        conditions = blankToNull(request.conditions());
        partner = blankToNull(request.partner());
        notes = blankToNull(request.notes());
    }

    private String blankToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
    public Long getId() { return id; }
    public UserAccount getUser() { return user; }
    public LocalDate getClimbedOn() { return climbedOn; }
    public String getLocation() { return location; }
    public String getSector() { return sector; }
    public String getRouteName() { return routeName; }
    public FrenchGrade getGrade() { return grade; }
    public Integer getLengthMeters() { return lengthMeters; }
    public Style getStyle() { return style; }
    public int getAttemptNumber() { return attemptNumber; }
    public boolean isSent() { return sent; }
    public Integer getTimeOnRouteMinutes() { return timeOnRouteMinutes; }
    public Integer getRpe() { return rpe; }
    public String getConditions() { return conditions; }
    public String getPartner() { return partner; }
    public String getNotes() { return notes; }
}
