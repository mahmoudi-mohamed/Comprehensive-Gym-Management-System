package com.project.bodify.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import com.project.bodify.dto.MediaDtoresponse;
import com.project.bodify.model.Files;
import com.project.bodify.model.Media;
import com.project.bodify.model.User;
import com.project.bodify.service.impl.FilesServiceImpl;
import com.project.bodify.service.impl.MediaServiceImpl;
import com.project.bodify.service.impl.UserServiceImpl;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaServiceImpl mediaServiceImpl;
    @Autowired
    private FilesServiceImpl filesServiceImpl;
    @Autowired
  private  UserServiceImpl userServiceImpl;
    
    
    
    
    

    @PostMapping
    public ResponseEntity<Media> addMedia(@RequestParam String description, @RequestParam List<MultipartFile> files,List<Long> tags) throws Exception {
        /*if (!isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed or user not authenticated");
        }
        User user = (User) authentication.getPrincipal();
        Media newMedia = mediaServiceImpl.addMedia(description, filesServiceImpl.storeFiles(files),user.getId(),tags);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMedia);
    }
    
    
    
    
    
    
    
    
    
    
    

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletemediaById(@PathVariable Long id) {
      
       

        if (mediaServiceImpl.getMediaById(id).isPresent()) {
            try {
                mediaServiceImpl.deleteMediaById(id);
                return ResponseEntity.ok().body("Media with ID: " + id + " deleted successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                     .body("Failed to delete Media with ID: " + id);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    
    
    
    
    
    

  
   /* private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                             .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }*/

    @GetMapping()
    public ResponseEntity<List<MediaDtoresponse>> getAllMedia() {
        List<Media> medias = mediaServiceImpl.getAllMedia();
        List<MediaDtoresponse> dtoResponses = new ArrayList<>();

        for (Media media : medias) {
            String imageName = null;
            if (media.getMedia() != null && !media.getMedia().isEmpty()) {
                imageName = media.getMedia().get(0).getName();
            }
            
            MediaDtoresponse dto = new MediaDtoresponse(media.getId(),media.getAuthorId(), userServiceImpl.getUserbyid(media.getAuthorId()).get().getFirstName()+" "+userServiceImpl.getUserbyid(media.getAuthorId()).get().getLastName(), media.getDescription(), imageName, null, null, null, media.getViewsliste().size()+"", media.getLiksliste().size()+"");
            dtoResponses.add(dto);
        }

        return ResponseEntity.ok(dtoResponses);
    }
    
    
    
    
    

    @GetMapping("/{id}")
    public MediaDtoresponse getMediaById(@PathVariable Long id) {
        Optional<Media> media = mediaServiceImpl.getMediaById(id);
       MediaDtoresponse dtoResponses;
       String imageName = null;
       imageName = media.get().getMedia().get(0).getName();
       return  dtoResponses=new MediaDtoresponse(media.get().getId(),media.get().getAuthorId(), userServiceImpl.getUserbyid(media.get().getAuthorId()).get().getFirstName()+" "+userServiceImpl.getUserbyid(media.get().getAuthorId()).get().getLastName(), media.get().getDescription(), imageName, null, null, null, media.get().getViewsliste().size()+"", media.get().getLiksliste().size()+"");
        
      
    }
    
    
    
    
    @GetMapping("/user/{id}")
    public ResponseEntity<List<MediaDtoresponse>> getMediaByuserId(@PathVariable Long id) {
    	 List<Media> medias = mediaServiceImpl.getMediaByauthor(id);
         List<MediaDtoresponse> dtoResponses = new ArrayList<>();

         for (Media media : medias) {
             String imageName = null;
             if (media.getMedia() != null && !media.getMedia().isEmpty()) {
                 imageName = media.getMedia().get(0).getName();
             }
             
             MediaDtoresponse dto = new MediaDtoresponse(media.getId(),media.getAuthorId(), userServiceImpl.getUserbyid(media.getAuthorId()).get().getFirstName()+" "+userServiceImpl.getUserbyid(media.getAuthorId()).get().getLastName(), media.getDescription(), imageName, null, null, null, media.getViewsliste().size()+"", media.getLiksliste().size()+"");
             dtoResponses.add(dto);
         }

         return ResponseEntity.ok(dtoResponses);
      
    }
    
    
    
    
    
    @PutMapping("/liks/{id}")
    public MediaDtoresponse updateliks(@PathVariable Long id) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed or user not authenticated");
        }
        User user = (User) authentication.getPrincipal();
    	mediaServiceImpl.changeliks(user.getId(), id);
    return	getMediaById(id);
        }
    
    
    
    
    
    
    @PutMapping("/view/{id}")
    public MediaDtoresponse updateviews(@PathVariable Long id) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication failed or user not authenticated");
        }
        User user = (User) authentication.getPrincipal();
    	mediaServiceImpl.changeViews(user.getId(), id);
    return	getMediaById(id);
        }
    
    
    
    @PutMapping("/{id}/archive")
    public ResponseEntity<Media> archiveMedia(@PathVariable Long id) {
        Media archivedMedia = mediaServiceImpl.archiveMedia(id);
        return ResponseEntity.ok(archivedMedia);
    }
    
    
    @PutMapping("/{id}/unarchive")
    public ResponseEntity<Media> unarchiveMedia(@PathVariable Long id) {
        Media archivedMedia = mediaServiceImpl.unarchiveMedia(id);
        return ResponseEntity.ok(archivedMedia);
    }
    
    
    @GetMapping("/archived/author/{authorId}")
    public ResponseEntity<List<Media>> getArchivedMediaByAuthorId(@PathVariable Long authorId) {
        List<Media> archivedMedia = mediaServiceImpl.getArchivedMediaByAuthorId(authorId);
        return ResponseEntity.ok(archivedMedia);
    }
    
    @GetMapping("/active/author/{authorId}")
    public ResponseEntity<List<Media>> getActiveMediaByAuthorId(@PathVariable Long authorId) {
        List<Media> archivedMedia = mediaServiceImpl.getActiveMediaByAuthorId(authorId);
        return ResponseEntity.ok(archivedMedia);
    }

  
}
