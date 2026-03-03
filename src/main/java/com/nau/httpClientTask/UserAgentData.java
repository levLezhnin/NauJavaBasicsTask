package com.nau.httpClientTask;

import com.fasterxml.jackson.annotation.JsonProperty;

class UserAgentData {

    @JsonProperty("user-agent")
    private String userAgent;

    public String getUserAgent() {
        return userAgent;
    }

    public UserAgentData() {}

    public UserAgentData(String userAgent) {
        this.userAgent = userAgent;
    }
}
