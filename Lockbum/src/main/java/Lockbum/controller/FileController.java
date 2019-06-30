package Lockbum.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import Lockbum.crypto.AsymmetricEncription;
import Lockbum.crypto.SymmetricAES;
import Lockbum.model.User;
import Lockbum.repository.UserRepository;
import Lockbum.service.UploadService;
import Lockbum.util.KeyStoreReader;

@RestController
public class FileController {
	
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(
			@RequestParam("file") MultipartFile file,
			Principal principal)
	{
		String fileName = uploadService.uploadFile(file, principal.getName());
		if (fileName == null)
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/download/{fileName:.+}")
    public void downloadFile(
    		@PathVariable String fileName, 
    		HttpServletRequest request,
    		HttpServletResponse response,
    		Principal principal)
	{
		// Resolving data necessary for encryption
		User user = userRepository.findByEmail(principal.getName());
		if (user == null)
			return;
		// Obtaining user's key store
		KeyStoreReader ksr = new KeyStoreReader(user);

		// Wrapping user's public and private key in this object
		AsymmetricEncription ae = new AsymmetricEncription(ksr.getPublicKey(), ksr.getPrivateKey());

		// Obtaining user's symmetric key
		SecretKey symmetricKey;
		try {
			// Reading symmetric key file
			File symmKeyFile = new File("./data/" + user.getEmail() + "/symmKey");
			InputStream stream = FileUtils.openInputStream(symmKeyFile);
			byte[] encryptedKey = IOUtils.toByteArray(stream);

			// Decrypting data with user's asymmetric keys
			byte[] decryptedKey = ae.decrypt(encryptedKey);

			// Creating SecretKey object with resulting data
			symmetricKey = new SecretKeySpec(decryptedKey, "AES");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		// Decrypting file using obtained symmetric key
		try {
			// Reading file
			File file = new File("./data/" + principal.getName() + "/data/" + fileName); 
			InputStream stream = FileUtils.openInputStream(file);
			byte[] encrypted = IOUtils.toByteArray(stream);

			// Decrypting data using symmetric key
			SymmetricAES symmAES = new SymmetricAES(symmetricKey);
			byte[] original = symmAES.decrypt(encrypted);

			// Transferring decrypted data to response
			InputStream in = new ByteArrayInputStream(original);
			IOUtils.copy(in, response.getOutputStream());
			// Clearing buffer
			response.flushBuffer();
	    } catch (IOException ex) {
	    	throw new RuntimeException("IOError writing file to output stream");
	    }
    }

}
