// FilesServiceImpl.java
package com.project.bodify.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bodify.model.Files;
import com.project.bodify.repository.FileRepository;
import com.project.bodify.service.FilesService;

@Service
public class FilesServiceImpl implements FilesService {
    
    @Autowired
    private FileRepository fileRepository;
    
    private final String FILE_PATH = "/home/imagess/";
   
    
    
    @Override
    public List<Files> storeFiles(List<MultipartFile> fileList) throws IOException {
        if (fileList == null || fileList.isEmpty()) {
            throw new IllegalArgumentException("File list is empty");
        }

        List<Files> savedFiles = new ArrayList<>();

        for (MultipartFile file : fileList) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("One or more files are empty");
            }

            Files savedFile = storeFileWithGeneratedName(file);
            savedFiles.add(savedFile);
        }

        return savedFiles;
    }

    public Files storeFileWithGeneratedName(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String extension = getFileExtension(originalFileName);
        String generatedName = generateFileName() + extension;

        Files files = Files
                .builder()
                .name(generatedName)
                .type(file.getContentType())
                .imageData(file.getBytes())
                .build();

        files = fileRepository.save(files);

        if (files.getId() == null) {
            throw new IOException("File could not be saved into the database");
        }

        return files;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return dotIndex == -1 ? "" : fileName.substring(dotIndex);
    }

    private String generateFileName() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Files getFiles(String fileName) {
        Files file = fileRepository.findByName(fileName);
        if (file == null) {
            throw new IllegalArgumentException("File not found");
        }
        return file;
    }
    
    @Override
    public Files getFileById(Long id) {
        Optional<Files> fileOptional = fileRepository.findById(id);
        return fileOptional.orElseThrow(() -> new IllegalArgumentException("File not found"));
    }
    
    @Override
    public List<Files> getAllFiles() {
        List<Files> allFiles = fileRepository.findAll();
        if (allFiles.isEmpty()) {
            throw new IllegalArgumentException("No files found");
        }
        return allFiles;
    }
    
    @Override
    public String storeDataIntoFileSystem(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String filePath = FILE_PATH + file.getOriginalFilename();
        
        Files files = Files
            .builder()
            .name(file.getOriginalFilename())
            .path(filePath)
            .type(file.getContentType())
            .imageData(file.getBytes())
            .build();
        
        files = fileRepository.save(files);
        
        file.transferTo(new File(filePath));
        
        if (files.getId() != null) {
            return "File uploaded successfully into the database";
        } else {
            throw new IOException("File could not be saved into the database");
        }
    }
    
    @Override
    public byte[] downloadFilesFromFileSystem(String fileName) throws IOException {
        Files file = fileRepository.findByName(fileName);
        if (file == null) {
            throw new IllegalArgumentException("File not found");
        }
        String path = file.getPath();
        return java.nio.file.Files.readAllBytes(new File(path).toPath());
    }
    
    @Override
    public boolean deleteFile(Long id) {
        if(fileRepository.existsById(id)) {
            fileRepository.deleteById(id);
            return true;
        } else {
            return false;
        }        
    }   
}
