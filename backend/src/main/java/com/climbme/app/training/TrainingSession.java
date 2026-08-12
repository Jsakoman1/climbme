package com.climbme.app.training;

import com.climbme.app.auth.UserAccount;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "training_sessions")
public class TrainingSession {
    public enum Type { CLIMBING, HANGBOARD, GYM, RUNNING, REST, MOBILITY }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id") private UserAccount user;
    @Column(name = "trained_on", nullable = false) private LocalDate trainedOn;
    @Enumerated(EnumType.STRING) @Column(name = "session_type", nullable = false) private Type sessionType;
    @Column(name = "duration_minutes", nullable = false) private int durationMinutes;
    private Integer strength; private Integer endurance; private Integer mobility;
    @Column(length = 2000) private String notes;
    protected TrainingSession() { }
    public TrainingSession(UserAccount user, Request request) { this.user = user; apply(request); }
    public void apply(Request request) { trainedOn=request.trainedOn(); sessionType=request.sessionType(); durationMinutes=request.durationMinutes(); strength=request.strength(); endurance=request.endurance(); mobility=request.mobility(); notes=request.notes()==null||request.notes().isBlank()?null:request.notes().trim(); }
    public Long getId(){return id;} public LocalDate getTrainedOn(){return trainedOn;} public Type getSessionType(){return sessionType;} public int getDurationMinutes(){return durationMinutes;} public Integer getStrength(){return strength;} public Integer getEndurance(){return endurance;} public Integer getMobility(){return mobility;} public String getNotes(){return notes;}
    public record Request(@jakarta.validation.constraints.NotNull LocalDate trainedOn, @jakarta.validation.constraints.NotNull Type sessionType, @jakarta.validation.constraints.Min(1) @jakarta.validation.constraints.Max(1440) int durationMinutes, @jakarta.validation.constraints.Min(1) @jakarta.validation.constraints.Max(10) Integer strength, @jakarta.validation.constraints.Min(1) @jakarta.validation.constraints.Max(10) Integer endurance, @jakarta.validation.constraints.Min(1) @jakarta.validation.constraints.Max(10) Integer mobility, @jakarta.validation.constraints.Size(max=2000) String notes) { }
}
