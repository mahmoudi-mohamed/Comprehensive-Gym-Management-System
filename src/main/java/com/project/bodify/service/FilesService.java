// FilesService.java
package com.project.bodify.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.bodify.model.Files;

public interface FilesService {
    
    List<Files> storeFiles(List<MultipartFile> fileList) throws IOException;

    Files getFiles(String fileName);

    Files getFileById(Long id);

    List<Files> getAllFiles();
    
    String storeDataIntoFileSystem(MultipartFile file) throws IOException;
    
    byte[] downloadFilesFromFileSystem(String fileName) throws IOException;
    
    boolean deleteFile(Long id);
}
