package com.project.bodify.dto;



import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
public class GetConvDto {
    @Id
	private String id ;
    
	
    public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getAutre() {
		return autre;
	}


	public void setAutre(String autre) {
		this.autre = autre;
	}


	private String autre ;
	
	
	
	
	
	
	
	
}
