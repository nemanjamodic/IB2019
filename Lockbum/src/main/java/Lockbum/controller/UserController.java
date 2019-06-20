package Lockbum.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import Lockbum.model.Authority;
import Lockbum.model.Registration;
import Lockbum.model.User;
import Lockbum.repository.UserRepository;
import Lockbum.service.AuthorityService;
import Lockbum.util.KeyStoreWriter;
import Lockbum.util.certificate.CertificateGenerator;
import Lockbum.util.certificate.IssuerData;
import Lockbum.util.certificate.SubjectData;

@RestController
public class UserController {
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/registration")
	public ResponseEntity<?> registration(
			@RequestBody Registration registration)
	{
		User user = new User();
		// TODO: OVde mora da se proveri da li vec postoji user sa ovim emailom!
		user.setEmail(registration.getEmail());
		user.setPassword(registration.getPassword());
		user.setActive(false);
		
		// Generate initial folders for this user
		generateFolders(user);
		// Generate JKS for this user
		if (generateJKS(user)) {
			user.setCertificate("cert.jks");
		}
		
		Authority authority = authorityService.findRegular();
		
		if (authority != null)
			user.setAuthority(authority);
		else
			System.err.println("Upredicted error!");
		
		userRepository.save(user);
		
		return new ResponseEntity<String>("Uspesno kreiran!", HttpStatus.OK);
		
	}
	
	private boolean generateJKS(User user) {
		try {
			CertificateGenerator gen = new CertificateGenerator();
			KeyPair keyPair = gen.generateKeyPair();
			
			Calendar calendar = new GregorianCalendar();
			Date startDate = calendar.getTime();
			calendar.add(Calendar.YEAR, 2);
			Date endDate = calendar.getTime();
			
			String email = user.getEmail();
			char[] password = user.getPassword().toCharArray();

			X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		    builder.addRDN(BCStyle.CN, email);
		    builder.addRDN(BCStyle.SURNAME, "");
		    builder.addRDN(BCStyle.GIVENNAME, "");
		    builder.addRDN(BCStyle.O, "ChocMail");
		    builder.addRDN(BCStyle.OU, "ChocMail");
		    builder.addRDN(BCStyle.C, "RS");
		    builder.addRDN(BCStyle.E, email);
		    // TODO: skontaj ovaj UID korisnika
		    builder.addRDN(BCStyle.UID, "123445");
			
			String sn = "1";

			IssuerData issuerData = new IssuerData(keyPair.getPrivate(), builder.build());
			SubjectData subjectData = new SubjectData(keyPair.getPublic(), builder.build(), sn, startDate, endDate);
			
			X509Certificate cert = gen.generateCertificate(issuerData, subjectData);
			
			KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
			keyStoreWriter.loadKeyStore(null, password);
			keyStoreWriter.write(email, keyPair.getPrivate(), password, cert);
			keyStoreWriter.saveKeyStore("data/" + email + "/cert.jks", password);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private void generateFolders(User user) {
		String email = user.getEmail();

		Path rootPath = Paths.get("data/" + email);
		Path imagePath = Paths.get("data/" + email + "/data");
		
		try {
			Files.createDirectory(rootPath);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		try {
			Files.createDirectory(imagePath);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}