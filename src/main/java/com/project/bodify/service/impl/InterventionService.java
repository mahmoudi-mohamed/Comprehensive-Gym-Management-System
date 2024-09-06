package com.project.bodify.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bodify.dto.AddInterventionDto;
import com.project.bodify.dto.InterventionResponse;
import com.project.bodify.dto.InterventionResponse.ErrorResponse;
import com.project.bodify.model.Intervention;
import com.project.bodify.model.PushNotificationRequest;
import com.project.bodify.model.User;
import com.project.bodify.repository.InterventionRepository;
import com.project.bodify.service.PushNotificationService;

@Service
public class InterventionService {

    private final InterventionRepository interventionRepository;
    
    @Autowired
    private  PushNotificationService notificationService;
    @Autowired
    private  UserServiceImpl userServiceImpl;
    
    
    
    
    @Autowired
    JwtServiceImpl jwtServiceImpl;
    
    public InterventionService(InterventionRepository interventionRepository) {
        this.interventionRepository = interventionRepository;
    }

    
    
    
    
    
    
    
    
    
    
    
    public Intervention saveIntervention(AddInterventionDto intervention, Long id) {
    	/* List<String> tokenfcm = new ArrayList<>();
         List<User> technicians = userServiceImpl.findTechnicians(); 

         for (User technician : technicians) {
             tokenfcm.add(technician.getTokenfcm());
         }
    	String tokenfcm=userServiceImpl.getUserbyid(intervention.getRealisesId()).orElseThrow().getTokenfcm();
         if (!tokenfcm.isEmpty()) {
        	 User user=userServiceImpl.getUserbyid(id).get();
             notificationService.sendPushNotificationToToken(new PushNotificationRequest("Demande de maintenance","Une demande de maintenance a été ajoutée par "+user.getFirstName()+" "+user.getLastName()+",et le problème est le suivant : "+ intervention.getDescprobleme(), null),tokenfcm, tokenfcm);
         }
         */
    	String tokenfcm=userServiceImpl.getUserbyid(intervention.getRealisesId()).orElseThrow().getTokenfcm();
         if (tokenfcm!=null) {
        	 User user=userServiceImpl.getUserbyid(id).get();
            notificationService.sendPushNotificationToToken(new PushNotificationRequest("Demande de maintenance"," Une demande de maintenance a été ajoutée par "+user.getFirstName()+" "+user.getLastName()+", et le problème est le suivant : "+ intervention.getDescprobleme(), tokenfcm,tokenfcm));
        }
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         sdf.setTimeZone(TimeZone.getTimeZone("Africa/Tunis"));
        return interventionRepository.save(new Intervention(intervention.getPosteid(), intervention.getLocalisation(), id, intervention.getPriorite(), LocalDateTime.now(), intervention.getDescprobleme(),intervention.getMachineid(),intervention.getRealisesId(),id));
       
    }

    
    
    
    
    
    
    
    
    
    
    
    
    public List<Intervention> getAllInterventions() {
        return interventionRepository.findAll();
    }

   
    public Optional<Intervention> getInterventionById(Long id) {
        return interventionRepository.findById(id);
    }
    
    public InterventionResponse acceptIntervention(Long interventionId, Long userId) {
        try {
            if (interventionRepository.existsById(interventionId)) {
                Intervention intervention = interventionRepository.getById(interventionId);
if (intervention.getRealisesId()==userId) {
	

                if (!intervention.getEtat().equals("accepte")) {
                    intervention.setDaterealises(LocalDateTime.now());
                    intervention.setEtat("accepte");
                    String tokenfcm=userServiceImpl.getUserbyid(intervention.getDemenderId()).orElseThrow().getTokenfcm();
                    if (tokenfcm!=null) {
                    	 User user=userServiceImpl.getUserbyid(userId).get();
                        notificationService.sendPushNotificationToToken(new PushNotificationRequest("Voir l'intervention  ",user.getFirstName()+" "+user.getLastName()+" Voir l'intervention N :" + intervention.getId() , tokenfcm,tokenfcm));
                    }
                    return new InterventionResponse(interventionRepository.save(intervention), "Intervention Accepted Successfully.");
                } else {
                    return new ErrorResponse("Intervention has already been accepted.");
                }
            } else {
                return new ErrorResponse("Intervention not found.");
            }}else {
            	return new ErrorResponse("Intervention  not accepted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse("An unexpected error occurred.");
        }
    }

    
    
    
    
    public InterventionResponse presenttoresolveIntervention(Long interventionId, Long userId) {
        try {
            if (interventionRepository.existsById(interventionId)) {
                Intervention intervention = interventionRepository.getById(interventionId);
if (intervention.getRealisesId()==userId) {
	

                
                    intervention.setDatepresent(LocalDateTime.now());
                   // intervention.setEtat("present");
                    String tokenfcm=userServiceImpl.getUserbyid(intervention.getDemenderId()).orElseThrow().getTokenfcm();
                    if (tokenfcm!=null) {
                    	 User user=userServiceImpl.getUserbyid(userId).get();
                        notificationService.sendPushNotificationToToken(new PushNotificationRequest("Présent Technicien ",user.getFirstName()+" "+user.getLastName()+" Présent pour résoudre l'intervention N :" +intervention.getId(), tokenfcm,tokenfcm));
                    }
                    return new InterventionResponse(interventionRepository.save(intervention), " Successfully.");
                
            } else {
                return new ErrorResponse("Intervention not found.");
            }}else {
            	return new ErrorResponse("Intervention  not accepted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse("An unexpected error occurred.");
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
 /*   
    public InterventionResponse repairIntervention(Long interventionId, Long userId,RepareDto repareDto) {
        try {
            if (interventionRepository.existsById(interventionId)) {
                Intervention intervention = interventionRepository.getById(interventionId);

                if (intervention.getRealisesId() != null && intervention.getRealisesId().equals(userId)) {
                	
                	if (intervention.getEtat().equals("repare")) {
                		 return new InterventionResponse(null, "Intervention repaired "+intervention.getDaterepare());
						
					}else {
                    intervention.setDaterepare(LocalDateTime.now());
                    intervention.setEtat("repare");
                    intervention.setDesctravaux(repareDto.getDesctravaux());
                    intervention.setPiecesutlise(repareDto.getPiecesutlise());
                    intervention.setArret(repareDto.getArret());
                    String tokenfcm=userServiceImpl.getUserbyid(intervention.getDemenderId()).orElseThrow().getTokenfcm();
                    if (tokenfcm!=null) {
                    User user=	userServiceImpl.getUserbyid(userId).get();
                        notificationService.sendPushNotificationToToken(new PushNotificationRequest("Fin de la Maintenance",user.getFirstName() +" "+user.getLastName()+" Terminé de résoudre l'intervention N : "+intervention.getId(), tokenfcm,tokenfcm));
                    }
                    return new InterventionResponse(interventionRepository.save(intervention), "Intervention repaired successfully.");}
                } else {
                    return new ErrorResponse("User is not authorized to repair the intervention.");
                }
            } else {
                return new ErrorResponse("Intervention not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse("An unexpected error occurred.");
        }
    }
    
    
    
    */
    
    
    public InterventionResponse getInterventionByrealisesId(Long id) {
        try {
            List<Intervention> interventions = interventionRepository.findByRealisesId(id);
            if (!interventions.isEmpty()) {
                return new InterventionResponse( "Interventions found for the specified user ID.",interventions);
            } else {
                return new InterventionResponse.ErrorResponse("No interventions found for the specified user ID.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new InterventionResponse.ErrorResponse("An unexpected error occurred while retrieving interventions.");
        }
    }
    
    
    public List<Intervention> getInterventionByrealisesIdAndEtat(Long id,String etat) {
    	List<Intervention> interventions = interventionRepository.findByRealisesIdAndEtat(id,etat);
        try {
        	return  interventions;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
    }
    
    
    
    
    
    
    

    public  List<Intervention> getInterventionByDemenderId(Long id) {
    	List<Intervention> interventions;
        try {
        	return  interventions = interventionRepository.findByDemenderId(id);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
		
    }

    
    
    public List<Intervention> getInterventionEnattente() {
        try {
            List<Intervention> interventions = interventionRepository.findByEtat("attente");
            if (!interventions.isEmpty()) {
                return interventions;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public List<Intervention> getInterventionByEtat(String etat) {
    	List<Intervention> interventions = interventionRepository.findByEtat(etat);
        try {
        	return  interventions;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
    }
    
    
    public List<Intervention> getInterventionByEtat(String etat1,String etat2) {
    	List<Intervention> interventions = interventionRepository.findByEtatOrEtat(etat1, etat2);
        try {
        	return  interventions;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
    }
    
    
    

    
    public Intervention updateInterventionRealisesId(Long id, Long newRealisesId,Long fixedby) {
        Optional<Intervention> optionalIntervention = interventionRepository.findById(id);
        
        if (optionalIntervention.isPresent()) {
            Intervention existingIntervention = optionalIntervention.get();
            String namefixedby=userServiceImpl.getUserbyid(fixedby).orElseThrow().getFirstName()+" "+userServiceImpl.getUserbyid(fixedby).orElseThrow().getLastName();
            String tokenfcmold=userServiceImpl.getUserbyid(existingIntervention.getRealisesId()).orElseThrow().getTokenfcm();
            if (tokenfcmold!=null) {
            notificationService.sendPushNotificationToToken(new PushNotificationRequest("Elimine pour Opération de maintenance  ",namefixedby+"  Vous a éliminé de résoudre l'intervention N :" +existingIntervention.getId(), tokenfcmold,tokenfcmold));
            }
            existingIntervention.setRealisesId(newRealisesId); 
            existingIntervention.setDaterealises(LocalDateTime.now());
            existingIntervention.setFixedBy(fixedby);
          //  existingIntervention.setEtat("accepte"); 
            String tokenfcm=userServiceImpl.getUserbyid(newRealisesId).orElseThrow().getTokenfcm();
            if (tokenfcm!=null) {
            	// User user=userServiceImpl.getUserbyid(newRealisesId).get();
                notificationService.sendPushNotificationToToken(new PushNotificationRequest("Sélectionné Pour Résoudre Intervention ",namefixedby+" Vous a sélectionné pour résoudre l'intervention N :" +existingIntervention.getId(), tokenfcm,tokenfcm));
            }
            return interventionRepository.save(existingIntervention);
        } else {
            return null;
        }
    }
    
    
    
    
    
    
    public Intervention VirefIntervention(Long id, int viref,Long fixedby) {
        Optional<Intervention> optionalIntervention = interventionRepository.findById(id);
        
        if (optionalIntervention.isPresent()) {
            Intervention existingIntervention = optionalIntervention.get();
            if (existingIntervention.getEtat().equals("repare")) {
				
			
            String namedemender=userServiceImpl.getUserbyid(existingIntervention.getDemenderId()).orElseThrow().getFirstName()+" "+userServiceImpl.getUserbyid(existingIntervention.getDemenderId()).orElseThrow().getLastName();
            String tokenfcmold=userServiceImpl.getUserbyid(existingIntervention.getRealisesId()).orElseThrow().getTokenfcm();
            if (viref==0) {
            	existingIntervention.setEtat("accepte");
            	 if (tokenfcmold!=null) {
                     notificationService.sendPushNotificationToToken(new PushNotificationRequest("Opération de maintenance Non Verifie  ",namedemender+" Non verifie de réparer  :" +existingIntervention.getDescprobleme(), tokenfcmold,tokenfcmold));
                     }
				
			}else {
				if (viref==1) {
					existingIntervention.setVerif(viref);
					 if (tokenfcmold!=null) {
	                     notificationService.sendPushNotificationToToken(new PushNotificationRequest("Opération de maintenance  Verifie  ",namedemender+" verifie de réparer  :" +existingIntervention.getDescprobleme(), tokenfcmold,tokenfcmold));
	                     }
				}
				
				
			}
            
            
            
            
            
            
            }
            
            return interventionRepository.save(existingIntervention);
        } else {
            return null;
        }
    }
    
    
    
    
    
    
    public Intervention pouseIntervention(Long id) {
        Optional<Intervention> optionalIntervention = interventionRepository.findById(id);
        
        if (optionalIntervention.isPresent()) {
        	
            Intervention existingIntervention = optionalIntervention.get();
            if (existingIntervention.getEtat().equals("accepte")) {
            	if (existingIntervention.getDatepresent()!=null) {
    				
    			
			
          //  String namedemender=userServiceImpl.getUserbyid(existingIntervention.getDemenderId()).orElseThrow().getFirstName()+" "+userServiceImpl.getUserbyid(existingIntervention.getDemenderId()).orElseThrow().getLastName();
            String tokenfcm=userServiceImpl.getUserbyid(existingIntervention.getDemenderId()).orElseThrow().getTokenfcm();
            
            	existingIntervention.setDurecumile(existingIntervention.getDurecumile()+getDateDiff(existingIntervention.getDatepresent(),LocalDateTime.now(),ChronoUnit.MINUTES));
            	existingIntervention.setDatepresent(null);
            	 if (tokenfcm!=null) {
                     notificationService.sendPushNotificationToToken(new PushNotificationRequest("Opération de maintenance en pause "," Pause intervention N : " +existingIntervention.getId(), tokenfcm,tokenfcm));
                     }
				
			
            	}
				
            }
    return interventionRepository.save(existingIntervention);
} else {
    return null;
}
}

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
   
    public Intervention updateIntervention(Long id, Intervention updatedIntervention) {
        if (interventionRepository.existsById(id)) {
            updatedIntervention.setId(id); 
            return interventionRepository.save(updatedIntervention);
        } else {
            return null; 
        }
    }

    public InterventionResponse deleteIntervention(Long id) {
        try {
            if (interventionRepository.existsById(id)) {
                interventionRepository.deleteById(id);
                return new InterventionResponse(null, "Intervention deleted successfully.");
            } else {
                return new InterventionResponse.ErrorResponse("Intervention not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new InterventionResponse.ErrorResponse("An unexpected error occurred while deleting the intervention.");
        }}
    public ZonedDateTime gettime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId tunisiaZone = ZoneId.of("Africa/Tunis");
        ZonedDateTime zdtTunisia = ZonedDateTime.of(localDateTime, tunisiaZone);
        return zdtTunisia;
    }
    
    
    
    public Map<LocalDate, Long> countInterventionsByDateDeclare() {
        List<Intervention> interventions = interventionRepository.findAll();
        Map<LocalDate, Long> countByDateDeclare = new HashMap<>();

        for (Intervention intervention : interventions) {
            LocalDate dateWithoutTime = intervention.getDatedeclare().toLocalDate();
            countByDateDeclare.put(dateWithoutTime, countByDateDeclare.getOrDefault(dateWithoutTime, 0L) + 1);
        }

        return countByDateDeclare;
    }
    
    
    
    public List<Intervention> getInterventionsByDateRepare(LocalDateTime dateWithoutTime) {
        Date dateWithoutTimeAsDate = Date.from(dateWithoutTime.atZone(ZoneId.systemDefault()).toInstant());
        return interventionRepository.getInterventionsByDateRepare(dateWithoutTimeAsDate);
    }
    
    
    
    
    public static long getDateDiff(LocalDateTime date1, LocalDateTime date2, ChronoUnit unit) {
        return unit.between(date1, date2);
    }
    
    
    
    
    
    
}
