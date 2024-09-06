package com.project.bodify.config;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Component
public class TimeZoneConfig {

    @PostConstruct
    public void init(){
        // Setting the default time zone to Africa/Tunis
        TimeZone.setDefault(TimeZone.getTimeZone("Africa/Tunis"));
    }
}
