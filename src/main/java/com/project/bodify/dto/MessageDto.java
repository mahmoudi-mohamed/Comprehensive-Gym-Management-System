package com.project.bodify.dto;



import java.util.Date;





import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;



@Getter
@Setter
public class MessageDto {
   

    private Long toid;
	private	String img;
	private String msg;
	private Long fromid;
	public Long getToid() {
		return toid;
	}
	public void setToid(Long toid) {
		this.toid = toid;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Long getFromid() {
		return fromid;
	}
	public void setFromid(Long fromid) {
		this.fromid = fromid;
	}
	
	
	
}
