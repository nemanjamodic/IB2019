package Lockbum.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

	private Path uploadLocation;
	
	public UploadService() {
		uploadLocation = Paths.get("E:\\IBUploadFolder").toAbsolutePath().normalize();
		
		try {
			Files.createDirectories(uploadLocation);
		} catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
		}

	}

	public String uploadFile(MultipartFile file) {
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
			}
			
			Path targetLocation = uploadLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return fileName;
		} catch (Exception ex) {
			throw new RuntimeException("Could not store the file " + fileName, ex);
		}
		
	}
	
	public Resource loadFile(String fileName) {
		try {
			Path filePath = uploadLocation.resolve(fileName).normalize();
			
			Resource resource = new UrlResource(filePath.toUri());
			
			if (resource.exists())
				return resource;
			else
				throw new RuntimeException("File not found: " + fileName);
		} catch (Exception ex) {
			throw new RuntimeException("File not found: " + fileName);
		}
	}
}
