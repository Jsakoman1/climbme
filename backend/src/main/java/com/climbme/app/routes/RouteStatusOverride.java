package com.climbme.app.routes;

import com.climbme.app.auth.UserAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "route_status_overrides")
public class RouteStatusOverride {
    public enum Status { ABANDONED }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "user_id", nullable = false) private UserAccount user;
    @Column(name = "route_key", nullable = false, length = 450) private String routeKey;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private Status status;
    protected RouteStatusOverride() { }
    public RouteStatusOverride(UserAccount user, String routeKey) { this.user = user; this.routeKey = routeKey; this.status = Status.ABANDONED; }
    public String getRouteKey() { return routeKey; }
    public Status getStatus() { return status; }
}
