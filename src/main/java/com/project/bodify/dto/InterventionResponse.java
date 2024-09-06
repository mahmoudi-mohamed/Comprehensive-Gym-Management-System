package com.project.bodify.dto;

import java.util.List;

import com.project.bodify.model.Intervention;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class InterventionResponse {
    private Intervention intervention;
    private List<Intervention>  interventionlist;
    private String message;

    public InterventionResponse(Intervention intervention, String message) {
        this.intervention = intervention;
        this.message = message;
    }
    
    public InterventionResponse( String message,List<Intervention> intervention) {
        this.interventionlist = intervention;
        this.message = message;
    }
    

    public static class ErrorResponse extends InterventionResponse {
        public ErrorResponse(String message) {
            super(null, message);
        }
    }
}
