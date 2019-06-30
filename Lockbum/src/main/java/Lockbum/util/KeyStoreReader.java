package Lockbum.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import Lockbum.model.User;

public class KeyStoreReader {

	private User user;
	// Values get set after calling readCertificate() method
	private Certificate certificate = null;
	private PrivateKey privateKey = null;
	private PublicKey publicKey = null;

	public KeyStoreReader(User user) {
		this.user = user;
		
		readCertificate();
	}

	public void readCertificate() {
		try {
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(user.getCertificate()));
			
			String alias = user.getEmail();
			//char[] password = user.getPassword().toCharArray();
			char[] password = user.getEmail().toCharArray();
			
			ks.load(in, password);

			if (ks.isKeyEntry(alias)) {
				certificate = ks.getCertificate(alias);
				privateKey = (PrivateKey) ks.getKey(alias, password);
				publicKey = certificate.getPublicKey();
			} else
				System.out.println("Unable to find certificate!");

		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

}
