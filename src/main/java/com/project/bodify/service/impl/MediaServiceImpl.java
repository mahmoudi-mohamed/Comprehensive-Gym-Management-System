package com.project.bodify.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.bodify.model.Files;
import com.project.bodify.model.Media;
import com.project.bodify.repository.MediaRepository;
import com.project.bodify.service.ProduitService;

@Service
public  class MediaServiceImpl implements ProduitService {
	
	 @Autowired
	    MediaRepository mediaRepository;

	    @Autowired
	    FilesServiceImpl filesServiceImpl;

	    
	    public Media addMedia(String description, List<Files> list,Long author,List<Long> tags) throws Exception {
	        return mediaRepository.save(new Media(list, description, author, tags));
	    }

	 
	    public List<Media> getAllMedia() {
	        return mediaRepository.findAll();
	    }

	    public List<Media> getMediaByauthor(Long author) {
	        return mediaRepository.findByAuthorId(author);
	    }

	    
	    
	    public Optional<Media> getMediaById(Long id) {
	        return mediaRepository.findById(id);
	    }
	    
	    
	   


	 
	    /*public Media updatemedia(Long id, String name, List<Files> images, String description, List<Defaut> defauts) {
	        Optional<Media> optional = mediaRepository.findById(id);
	        if (optional.isPresent()) {
	            Media media = optional.get();
	           
	            media.setMedia(images);
	            media.setDescription(description);
	    
	            return mediaRepository.save(media);
	        } else {
	            return null;
	        }
	    }*/

	    
	    
	    
	    
	    public Media changeliks(Long UserId,Long mediaId) {
	        Optional<Media> optional = mediaRepository.findById(mediaId);
	        if (optional.isPresent()) {
	            Media media = optional.get();
	           
	          if(  media.getLiksliste().contains(UserId)) {
	        	  media.getLiksliste().remove(media.getLiksliste().indexOf(UserId));
	        	  
	        	  
	        	  
	          }else {
	        	  media.getLiksliste().add(UserId); 
	        	  
	        	  
	          }
	    
	            return mediaRepository.save(media);
	        } else {
	            return null;
	        }
	    }
	    
	    
	    public Media changeViews(Long UserId,Long mediaId) {
	        Optional<Media> optional = mediaRepository.findById(mediaId);
	        if (optional.isPresent()) {
	            Media media = optional.get();
	           
	          if(  media.getViewsliste().contains(UserId)) {
	        	 
	        	   
	          }else {
	        	  media.getViewsliste().add(UserId);
	          }
	    
	            return mediaRepository.save(media);
	        } else {
	            return null;
	        }
	    }
	  
	   

	  
	    public void deleteMediaById(Long id) throws Exception {
	        List<Files> media = getMediaById(id).orElseThrow().getMedia();

	        try {
	            for (Files file : media) {
	                filesServiceImpl.deleteFile(file.getId());
	            }
	        } catch (Exception e) {
	            
	        }
	        
	        mediaRepository.deleteById(id);
	    }
	
    
	

	    public Media archiveMedia(Long mediaId) {
	        // Fetch media by ID
	        Media media = mediaRepository.findById(mediaId).orElseThrow();
	        // Update the archived status
	        media.setStatus("ARCHIVED");
	        // Save the updated media
	        return mediaRepository.save(media);
	    }
	
	    public Media unarchiveMedia(Long mediaId) {
	        Media media = mediaRepository.findById(mediaId).orElseThrow();
	        media.setStatus("ACTIVE");
	        return mediaRepository.save(media);
	    }
	
	
	
	    public List<Media> getArchivedMediaByAuthorId(Long authorId) {
	        return mediaRepository.findByAuthorIdAndStatus(authorId, "ARCHIVED");
	    }
	
	    public List<Media> getActiveMediaByAuthorId(Long authorId) {
	        return mediaRepository.findByAuthorIdAndStatus(authorId, "ACTIVE");
	    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
 public long gettime() {
    	
    	LocalDateTime localDateTime = LocalDateTime.now(); 
    	ZoneId tunisiaZone = ZoneId.of("Africa/Tunis");
    	ZonedDateTime zdtTunisia = ZonedDateTime.of(localDateTime, tunisiaZone);
    	long millis = zdtTunisia.toInstant().toEpochMilli();
		return millis;
		
	}
    
    

}
