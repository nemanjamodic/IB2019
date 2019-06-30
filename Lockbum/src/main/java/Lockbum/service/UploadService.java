package Lockbum.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import Lockbum.crypto.AsymmetricEncription;
import Lockbum.crypto.SymmetricAES;
import Lockbum.model.User;
import Lockbum.repository.UserRepository;
import Lockbum.util.KeyStoreReader;

@Service
public class UploadService {

	@Autowired
	private UserRepository userRepository;

	private Path uploadLocation;
	
	public UploadService() {
		uploadLocation = Paths.get("data").toAbsolutePath().normalize();
		
		try {
			Files.createDirectories(uploadLocation);
		} catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
		}

	}

	public String uploadFile(MultipartFile file, String email) {
		// Resolving data necessary for encryption
		User user = userRepository.findByEmail(email);
		if (user == null)
			return null;

		// Obtaining user's key store
		KeyStoreReader ksr = new KeyStoreReader(user);
		
		// Wrapping user's public and private key in this object
		AsymmetricEncription ae = new AsymmetricEncription(ksr.getPublicKey(), ksr.getPrivateKey());
		
		// Obtaining user's symmetric key
		SecretKey symmetricKey;
		try {
			// Reading symmetric key file
			File symmKey = new File("./data/" + user.getEmail() + "/symmKey");
			InputStream stream = FileUtils.openInputStream(symmKey);
			byte[] encryptedKey = IOUtils.toByteArray(stream);
			
			// Decrypting data with user's asymmetric keys
			byte[] decryptedKey = ae.decrypt(encryptedKey);
			
			// Creating SecretKey object with resulting data
			symmetricKey = new SecretKeySpec(decryptedKey, "AES");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
		
		// Encrypting file using obtained symmetric key
		try {
			// Reading file
			InputStream stream = file.getInputStream();
			byte[] bytes = IOUtils.toByteArray(stream);
			
			// Encrypting data using symmetric key
			SymmetricAES symmAES = new SymmetricAES(symmetricKey);
			byte[] encrypted = symmAES.encrypt(bytes);

			// Preparing file
			String fileName = StringUtils.cleanPath(email + "/data/" + file.getOriginalFilename());
			Path targetLocation = uploadLocation.resolve(fileName);
			File encryptedFile = targetLocation.toFile();
			
			// Transferring encrypted data to file
			FileUtils.writeByteArrayToFile(encryptedFile, encrypted);
			
            return fileName;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
	}
	
	public Resource loadFile(String path) {
		try {
			Path filePath = uploadLocation.resolve(path).normalize();
			
			Resource resource = new UrlResource(filePath.toUri());
			
			if (resource.exists())
				return resource;
			else
				throw new RuntimeException("File not found: " + path);
		} catch (Exception ex) {
			throw new RuntimeException("File not found: " + path);
		}
	}
}
