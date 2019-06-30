package LockbumApp.security;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import LockbumApp.Application;

public class SecurityService {

	private static final String KEY_STORE_FILE = "./data/cert.jks";
	
	public static Certificate readCertificate()
	{
		try {
			// kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			// ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEY_STORE_FILE));
			//ks.load(in, Application.user.getPassword().toCharArray());
			ks.load(in, Application.user.getEmail().toCharArray());

			if (ks.isKeyEntry(Application.user.getEmail())) {
				//Certificate cert = ks.getCertificate(Application.user.getEmail());
				Certificate cert = ks.getCertificate(Application.user.getEmail());
				return cert;
			} else
				return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;		
		}
	}

	public static SecretKey generateDataEncryptionKey() {

		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede"); // Triple
																			// DES
			return keyGenerator.generateKey();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static PrivateKey readPrivateKey() {
		try {
			//kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			
			//ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEY_STORE_FILE));
			//ks.load(in, Application.user.getPassword().toCharArray());
			ks.load(in, Application.user.getEmail().toCharArray());
			
			if(ks.isKeyEntry(Application.user.getEmail())) {
				//PrivateKey pk = (PrivateKey) ks.getKey(Application.user.getEmail(), Application.user.getPassword().toCharArray());
				PrivateKey pk = (PrivateKey) ks.getKey(Application.user.getEmail(), Application.user.getEmail().toCharArray());
				return pk;
			}
			else
				return null;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
}
