
package com.project.bodify.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.bodify.model.Files;
import com.project.bodify.service.impl.FilesServiceImpl;

@RestController
@RequestMapping("/api/file")
public class FilesController {
    
    @Autowired
    private FilesServiceImpl filesServiceImpl;
    
    @PostMapping("/upload")
    public ResponseEntity<?> storeFilesIntoDB(@RequestParam("file") List<MultipartFile> file) {
        try {
            List<Files> savedFileIds = filesServiceImpl.storeFiles(file);
            return ResponseEntity.status(HttpStatus.OK).body(savedFileIds);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + e.getMessage());
        }
    }
    
    @GetMapping("/name/{fileName}")
    public ResponseEntity<?> getImage(@PathVariable String fileName) {
        try {
            Files file = filesServiceImpl.getFiles(fileName);
            if (file == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }
            byte[] imageData = file.getImageData();
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving file: " + e.getMessage());
        }
    }
    
    @GetMapping("/id/{fileId}")
    public ResponseEntity<?> getFileById(@PathVariable Long fileId) {
        try {
            Files file = filesServiceImpl.getFileById(fileId);
            if (file == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }
            byte[] imageData = file.getImageData();
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving file: " + e.getMessage());
        }
    }
    
    @GetMapping("/getAllFiles")
    public ResponseEntity<?> getAllFiles() {
        try {
            List<Files> allFiles = filesServiceImpl.getAllFiles();
            return ResponseEntity.status(HttpStatus.OK).body(allFiles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving files: " + e.getMessage());
        }
    }
    
    @PostMapping("/uploadFiles")
    public ResponseEntity<?> uploadFileIntoFileSystem(@RequestParam("file") MultipartFile file) {
        try {
            String response = filesServiceImpl.storeDataIntoFileSystem(file);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + e.getMessage());
        }
    }
    
    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) {
        try {
            byte[] imageData = filesServiceImpl.downloadFilesFromFileSystem(fileName);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("image/png")).body(imageData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error downloading file: " + e.getMessage());
        }
    }
}
