package com.project.bodify.dto;



import java.util.Date;



import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;



@Getter
@Setter
public class AccDto {
    @Id
	private long id ;
    public void setId(long id) {
		this.id = id;
	}
	
    private String userid ;
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public long getId() {
		return id;
	}
	
	
	
	
	
	
	
}
