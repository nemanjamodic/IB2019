package Lockbum.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import Lockbum.service.UploadService;

@RestController("/getJKS")
public class JKSController {
	
	@Autowired
	private UploadService uploadService;

	@GetMapping
    public ResponseEntity<Resource> downloadFile(Principal principal) {
		String path = principal.getName() + "/cert.jks";

		Resource resource = uploadService.loadFile(path);

        String contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
	
}
