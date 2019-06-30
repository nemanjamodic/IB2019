package Lockbum.crypto;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.io.FileUtils;

public class SymmetricAES {
	
	private static short BLOCK_SIZE = 16;

	private SecretKey secretKey;
	private IvParameterSpec ivParameterSpec;

	public SymmetricAES(SecretKey secretKey) {
		this.secretKey = secretKey;
		ivParameterSpec = generateIV();
	}
	
	public static SecretKey generateKey() {
        try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			SecretKey secretKey = keyGen.generateKey();
			return secretKey;
			
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        
        return null;
	}
	
	private IvParameterSpec generateIV() {
		byte [] iv = new byte[BLOCK_SIZE];
		
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
		return ivParameterSpec;
	}
	
	public byte[] encrypt(byte[] data) {
		
		try {
			Cipher aesCipherEnc = Cipher.getInstance("AES/CBC/PKCS5Padding");
			aesCipherEnc.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
			
			byte[] ciphertext = aesCipherEnc.doFinal(data);
			
			File crypted = new File("./data/crypted.zip");
			
			FileUtils.writeByteArrayToFile(crypted, ciphertext);
			
			return ciphertext;

		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public byte[] decrypt(byte[] data) {
			
		try {
			Cipher aesCipherDec = Cipher.getInstance("AES/CBC/PKCS5Padding");
			aesCipherDec.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
			
			byte[] original = aesCipherDec.doFinal(data);
			
			return original;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}

