package com.project.bodify.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bodify.Socket;
import com.project.bodify.dto.AddInterventionDto;
import com.project.bodify.dto.InterventionResponse;

import com.project.bodify.dto.UserDto;
import com.project.bodify.model.Intervention;
import com.project.bodify.model.User;
import com.project.bodify.service.impl.InterventionService;
import com.project.bodify.service.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/interventions")
public class InterventionController {

    private final InterventionService interventionService;
    
    @Autowired
    private  Socket socket;
    @Autowired
    UserServiceImpl userService;
   
  
    public InterventionController(InterventionService interventionService) {
        this.interventionService = interventionService;
    }

    @PostMapping
    public ResponseEntity<Intervention> saveIntervention(@RequestBody AddInterventionDto intervention ) {
        Intervention savedIntervention = interventionService.saveIntervention(intervention,getIDFromToken());
        socket.broadcast("/api/interventions");

        return ResponseEntity.status(HttpStatus.CREATED).body(savedIntervention);

       
        
    }

    @GetMapping
    public ResponseEntity<List<Intervention>> getAllInterventions() {
        List<Intervention> interventions = interventionService.getAllInterventions();

      
        for (Intervention intervention : interventions) {
            if (intervention.getDemenderId() != null) {
                User requester = userService.getUserbyid(intervention.getDemenderId()).orElseThrow();
                intervention.setRequesterName(requester.getFirstName() + " " + requester.getLastName());
                
            }
            
            if (intervention.getRealisesId() != null) {
                User executor = userService.getUserbyid(intervention.getRealisesId()).orElseThrow();
                intervention.setExecutorName(executor.getFirstName() + " " + executor.getLastName());
            }
            
            if (intervention.getFixedBy() != null) {
                User requester = userService.getUserbyid(intervention.getFixedBy()).orElseThrow();
                intervention.setFixedName(requester.getFirstName() + " " + requester.getLastName());
                
            }
            
            if (intervention.getDaterepare() != null && intervention.getDatepresent() != null ) {
               
                intervention.setDuretoresolved(intervention.getDurecumile()+getDateDiff(intervention.getDatepresent(),intervention.getDaterepare(),ChronoUnit.MINUTES));
                
            }
        }
        Collections.reverse(interventions);

        return ResponseEntity.ok(interventions);
    }
    
    
    @GetMapping("/attente")
    public ResponseEntity<List<Intervention>> getInterventionsEnattente() {
    	 List<Intervention> interventions=interventionService.getInterventionEnattente();
    	 
    	 
    	 for (Intervention intervention : interventions) {
             if (intervention.getDemenderId() != null) {
                 User requester = userService.getUserbyid(intervention.getDemenderId()).orElseThrow();
                 intervention.setRequesterName(requester.getFirstName() + " " + requester.getLastName());
             }
             if (intervention.getRealisesId() != null) {
                 User executor = userService.getUserbyid(intervention.getRealisesId()).orElseThrow();
                 intervention.setExecutorName(executor.getFirstName() + " " + executor.getLastName());
             }
             
             if (intervention.getFixedBy() != null) {
                 User requester = userService.getUserbyid(intervention.getFixedBy()).orElseThrow();
                 intervention.setFixedName(requester.getFirstName() + " " + requester.getLastName());
                 
             }
         }
    	   Collections.reverse(interventions);

         return ResponseEntity.ok(interventions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Intervention> getInterventionById(@PathVariable Long id) {
        Optional<Intervention> intervention = interventionService.getInterventionById(id);
        return intervention.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    
    @GetMapping("/technicians")
    public List<UserDto> getTechnicians() {
        List<User> users = userService.findCoach();
        List<UserDto> usersDto = new ArrayList<>(); // Initialize the list

        for (User user : users) {
            usersDto.add(new UserDto(user.getId(), user.getFirstName()+" "+user.getLastName(), user.getEmail()));
        }
        
        return usersDto; 
    }
    
    @GetMapping("/realises/{id}")
    public ResponseEntity<InterventionResponse> getInterventionByrealisesId(@PathVariable Long id) {
    	
         
        return ResponseEntity.ok(interventionService.getInterventionByrealisesId(id));
    }
    
    
    
    
    @GetMapping("/realises/{id}/{etat}")
    public ResponseEntity<List<Intervention>> getInterventionByrealisesIdAndEtat(@PathVariable Long id,@PathVariable String etat) {
    	List<Intervention> interventions=interventionService.getInterventionByrealisesIdAndEtat(id,etat);
    	 Collections.reverse(interventions);
        return ResponseEntity.ok(interventions);
    }
    
    
    
    @GetMapping("/etat/{etat}")
    public ResponseEntity<List<Intervention>> getInterventionByEtat(@PathVariable String etat) {
         
    	List<Intervention> interventions = interventionService.getInterventionByEtat(etat);
    	for (Intervention intervention : interventions) {
            if (intervention.getDemenderId() != null) {
                User requester = userService.getUserbyid(intervention.getDemenderId()).orElseThrow();
                intervention.setRequesterName(requester.getFirstName() + " " + requester.getLastName());
            }
            if (intervention.getRealisesId() != null) {
                User executor = userService.getUserbyid(intervention.getRealisesId()).orElseThrow();
                intervention.setExecutorName(executor.getFirstName() + " " + executor.getLastName());
            }
            
            if (intervention.getFixedBy() != null) {
                User requester = userService.getUserbyid(intervention.getFixedBy()).orElseThrow();
                intervention.setFixedName(requester.getFirstName() + " " + requester.getLastName());
                
            }
            
            if (intervention.getDaterepare() != null && intervention.getDatepresent() != null ) {
               
                intervention.setDuretoresolved(intervention.getDurecumile()+getDateDiff(intervention.getDatepresent(),intervention.getDaterepare(),ChronoUnit.MINUTES));
                
            }
        }
    	Collections.reverse(interventions);
    	 return ResponseEntity.ok(interventions);
    }
    
    
    
    @GetMapping("/realises/my/{etat}")
    public ResponseEntity<List<Intervention>> getmyInterventionbyEtat(@PathVariable String etat) {
    	List<Intervention> interventions =interventionService.getInterventionByrealisesIdAndEtat(getIDFromToken(),etat);
    	 for (Intervention intervention : interventions) {
             if (intervention.getDemenderId() != null) {
                 User requester = userService.getUserbyid(intervention.getDemenderId()).orElseThrow();
                 intervention.setRequesterName(requester.getFirstName() + " " + requester.getLastName());
             }
             if (intervention.getRealisesId() != null) {
                 User executor = userService.getUserbyid(intervention.getRealisesId()).orElseThrow();
                 intervention.setExecutorName(executor.getFirstName() + " " + executor.getLastName());
             }
             
             if (intervention.getFixedBy() != null) {
                 User requester = userService.getUserbyid(intervention.getFixedBy()).orElseThrow();
                 intervention.setFixedName(requester.getFirstName() + " " + requester.getLastName());
                 
             }
             
             if (intervention.getDaterepare() != null && intervention.getDatepresent() != null ) {
                
                 intervention.setDuretoresolved(intervention.getDurecumile()+getDateDiff(intervention.getDatepresent(),intervention.getDaterepare(),ChronoUnit.MINUTES));
                 
             }
         }
    	 Collections.reverse(interventions);

         return ResponseEntity.ok(interventions);
        
    }
    
    
    
    @PutMapping("/{id}/update-realisesId")
    public ResponseEntity<Intervention> updateInterventionRealisesId(@PathVariable Long id, @RequestParam Long newRealisesId) {
        Intervention updatedIntervention = interventionService.updateInterventionRealisesId(id, newRealisesId,getIDFromToken());
        return ResponseEntity.ok(updatedIntervention);
    }
    
    
    
    
    
    
    @PutMapping("/{id}/verif")
    public ResponseEntity<Intervention> verifIntervention(@PathVariable Long id, @RequestParam int verif) {
        Intervention updatedIntervention = interventionService.VirefIntervention(id, verif, getIDFromToken());        //(id, newRealisesId,getIDFromToken());
        socket.broadcast("/api/interventions");
        return ResponseEntity.ok(updatedIntervention);
    }
    
    
    @PutMapping("/pouse/{id}")
    public ResponseEntity<Intervention> pauseIntervention(@PathVariable Long id) {
        Intervention updatedIntervention = interventionService.pouseIntervention(id);        //(id, newRealisesId,getIDFromToken());
        return ResponseEntity.ok(updatedIntervention);
    }
    
    
    
   
    
    @GetMapping("/demender/{id}")
    public ResponseEntity<List<Intervention>> getInterventionByDemenderId(@PathVariable Long id) {
    	List<Intervention> interventions =interventionService.getInterventionByDemenderId(id);
    	
    	Collections.reverse(interventions);
        return ResponseEntity.ok(interventions);
    }
    
    
    
    @GetMapping("/demender/my")
    public ResponseEntity<List<Intervention>> getmyInterventionbyDemender() {
    	List<Intervention> interventions =interventionService.getInterventionByDemenderId(getIDFromToken());
    	 for (Intervention intervention : interventions) {
             if (intervention.getDemenderId() != null) {
                 User requester = userService.getUserbyid(intervention.getDemenderId()).orElseThrow();
                 intervention.setRequesterName(requester.getFirstName() + " " + requester.getLastName());
             }
             if (intervention.getRealisesId() != null) {
                 User executor = userService.getUserbyid(intervention.getRealisesId()).orElseThrow();
                 intervention.setExecutorName(executor.getFirstName() + " " + executor.getLastName());
             }
             
             if (intervention.getFixedBy() != null) {
                 User requester = userService.getUserbyid(intervention.getFixedBy()).orElseThrow();
                 intervention.setFixedName(requester.getFirstName() + " " + requester.getLastName());
                 
             }
             
             if (intervention.getDaterepare() != null && intervention.getDatepresent() != null ) {
                
                 intervention.setDuretoresolved(intervention.getDurecumile()+getDateDiff(intervention.getDatepresent(),intervention.getDaterepare(),ChronoUnit.MINUTES));
                 
             }
         }
    	 Collections.reverse(interventions);
         return ResponseEntity.ok(interventions);
        
    }
    
    
    

    @PutMapping("/{id}")
    public ResponseEntity<Intervention> updateIntervention(@PathVariable Long id, @RequestBody Intervention updatedIntervention) {
        Intervention intervention = interventionService.updateIntervention(id, updatedIntervention);
        if (intervention != null) {
        	 socket.broadcast("/api/interventions");
            return ResponseEntity.ok(intervention);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/accepte/{id}")
    public ResponseEntity<InterventionResponse> acceptentervention(@PathVariable Long id) {
    	InterventionResponse intervention = interventionService.acceptIntervention(id, getIDFromToken());
        if (intervention != null) {
        	 socket.broadcast("/api/interventions");
            return ResponseEntity.ok(intervention);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    @PutMapping("/present/{id}")
    public ResponseEntity<InterventionResponse> presenttoresolveintervention(@PathVariable Long id) {
    	InterventionResponse intervention = interventionService.presenttoresolveIntervention(id, getIDFromToken());
        if (intervention != null) {
        	 socket.broadcast("/api/interventions");
            return ResponseEntity.ok(intervention);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
  /*  
    @PutMapping("/repare/{id}")
    public ResponseEntity<InterventionResponse> repareIntervention(@PathVariable Long id,@RequestBody RepareDto repare) {
    	InterventionResponse intervention = interventionService.repairIntervention(id, getIDFromToken(),repare);
        if (intervention != null) {
        	 socket.broadcast("/api/interventions");
            return ResponseEntity.ok(intervention);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    */
    
    
    
    @DeleteMapping("/{id}")
    public ResponseEntity<InterventionResponse> deleteIntervention(@PathVariable Long id) {
    	 socket.broadcast("/api/interventions");
    	return ResponseEntity.ok( interventionService.deleteIntervention(id));
    }
    
    
    
    
    
    @GetMapping("/count-by-date")
    public ResponseEntity<Map<LocalDate, Long>> countInterventionsByDateDeclare() {
        Map<LocalDate, Long> countByDateDeclare = interventionService.countInterventionsByDateDeclare();
        return ResponseEntity.ok(countByDateDeclare);
    }
    
    
    
    
    @GetMapping("/byDateRepare/{date}")
    public ResponseEntity<List<Intervention>> getInterventionsByDateRepare(
            @PathVariable("date") String dateString) {
        
        List<Intervention> interventions;
        
        if ("toute".equalsIgnoreCase(dateString)) {
           
            interventions = interventionService.getAllInterventions();
        } else {
         
            LocalDate date;
            try {
                date = LocalDate.parse(dateString);
            } catch (DateTimeParseException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Convert LocalDate to LocalDateTime at the start of the day
            LocalDateTime startOfDay = date.atStartOfDay(ZoneOffset.UTC).toLocalDateTime();
            interventions = interventionService.getInterventionsByDateRepare(startOfDay);
        }

        // Process the interventions
        for (Intervention intervention : interventions) {
            if (intervention.getDemenderId() != null) {
                userService.getUserbyid(intervention.getDemenderId()).ifPresent(requester ->
                    intervention.setRequesterName(requester.getFirstName() + " " + requester.getLastName()));
            }

            if (intervention.getRealisesId() != null) {
                userService.getUserbyid(intervention.getRealisesId()).ifPresent(executor ->
                    intervention.setExecutorName(executor.getFirstName() + " " + executor.getLastName()));
            }

            if (intervention.getFixedBy() != null) {
                userService.getUserbyid(intervention.getFixedBy()).ifPresent(fixer ->
                    intervention.setFixedName(fixer.getFirstName() + " " + fixer.getLastName()));
            }

            if (intervention.getDaterepare() != null && intervention.getDatepresent() != null) {
                intervention.setDuretoresolved(intervention.getDurecumile() +
                    getDateDiff(intervention.getDatepresent(), intervention.getDaterepare(), ChronoUnit.MINUTES));
            }
        }

        return new ResponseEntity<>(interventions, HttpStatus.OK);
    }



    
    
    
    private Long getIDFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed or user not authenticated");
        }
       User us= (User) authentication.getPrincipal(); 
       return us.getId();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public static long getDateDiff(LocalDateTime date1, LocalDateTime date2, ChronoUnit unit) {
        return unit.between(date1, date2);
    }
}
