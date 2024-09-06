package com.project.bodify.dto;





import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public  class CreateInterventionDto   {
	


@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@NonNull
private Long machineid;
@NonNull
private String localisation;
private String  priorite;
private String descprobleme;
private String convient;








}
