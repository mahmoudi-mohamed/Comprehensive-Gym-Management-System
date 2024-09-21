package com.project.bodify.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;







@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public  class MediaDtoresponse {
	
	
	  private long id;
	  private long authorId;
	  private String author;
	    private String description;
	    private String image;
	    private List<String> viewsliste=new ArrayList<>();
	    private List<String> liksliste=new ArrayList<>();
	    private List<String> TagsList = new ArrayList<>();
	    private String nbview;
	    private String nbliks;
	    private String post_type="TEXT";
	    private LocalDate publish_date;
	
}
