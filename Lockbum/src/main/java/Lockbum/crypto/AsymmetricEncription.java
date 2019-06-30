package Lockbum.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AsymmetricEncription {
	
	private PublicKey  publicKey;
	private PrivateKey privateKey;

	public AsymmetricEncription(PublicKey publicKey, PrivateKey privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	public byte[] encrypt(byte[] data) {
		
		try {
			Cipher encryptor = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			encryptor.init(Cipher.ENCRYPT_MODE, publicKey);

			byte[] ciphertext = encryptor.doFinal(data);
			
			return ciphertext;

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
		return new byte[] {};
	}
	
	public byte[] decrypt(byte[] data) {
		
		try {
			Cipher decryptor = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			decryptor.init(Cipher.DECRYPT_MODE, privateKey);

			byte[] original = decryptor.doFinal(data);
			
			return original;

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
