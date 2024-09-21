package com.project.bodify.dto;



import java.util.Date;



import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;



@Getter
@Setter
public class ReciDto {
    @Id
	private String id ;
    public void setId(String id) {
		this.id = id;
	}
    
    float Rating;
	
    public float getRating() {
		return Rating;
	}

	public void setRating(float rating) {
		Rating = rating;
	}

	private String userid ;
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getId() {
		return id;
	}
	
	
	
	
	
	
	
}
