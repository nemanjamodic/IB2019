package Lockbum.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import Lockbum.network.UploadResponse;
import Lockbum.service.UploadService;

@RestController
public class UploadController {
	
	@Autowired
	private UploadService uploadService;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(
			@RequestParam("file") MultipartFile file)
	{
		String fileName = uploadService.uploadFile(file);
		
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("download/")
                .path(fileName)
                .toUriString();

        UploadResponse response = new UploadResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
        
        return new ResponseEntity<UploadResponse>(response, HttpStatus.OK);
	}

	@GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(
    		@PathVariable String fileName, 
    		HttpServletRequest request) {
        // Load file as Resource
        Resource resource = uploadService.loadFile(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            //logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
	
	@PostMapping("/test")
	public ResponseEntity<?> upload(
			@RequestParam("file") String file)
	{
		System.out.println(file);
		return new ResponseEntity<String>("Odgovorio sam!", HttpStatus.OK);
	}

}
