package com.climbme.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "climbme.operations")
public class OperationalProperties {
    private boolean secureCookies;

    public boolean isSecureCookies() { return secureCookies; }

    public void setSecureCookies(boolean secureCookies) { this.secureCookies = secureCookies; }
}
