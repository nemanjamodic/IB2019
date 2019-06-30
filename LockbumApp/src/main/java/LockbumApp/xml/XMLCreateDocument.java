package LockbumApp.xml;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import LockbumApp.Application;
import LockbumApp.security.SecurityService;
import LockbumApp.util.Base64;

public class XMLCreateDocument {
	
	/**
	 * Extracts information about images contained in the list.
	 * Name, height, width and hash.
	 * 
	 * @param List of images.
	 * 
	 */
	public static List<XMLImageInformation> XMLInformationFromImages(List<File> images)
	{
		ArrayList<XMLImageInformation> imageInformation = new ArrayList<XMLImageInformation>();
		
		for(File image : images) {
			
			String name = image.getName();
			Integer height;
			Integer width;
			String hashByte;
			
			try {
				BufferedImage img = ImageIO.read(image);
				byte[] data = Files.readAllBytes(image.toPath());
				
				height = img.getHeight();
				width = img.getWidth();	
								
				MessageDigest md = MessageDigest.getInstance("SHA-256");
		        md.update(data);
		        byte[] hash = md.digest();
		        hashByte = Base64.encodeToString(hash);
		        
		        XMLImageInformation inf = new XMLImageInformation(name, height, width, hashByte);
		        imageInformation.add(inf);
		        
			}catch(Exception e) {
				e.printStackTrace();
			}						        
		}
		
		return imageInformation;
	}

	/**
	 * Generates XML document that contains all necessary information
	 * about user and the images that need to be sent.
	 * 
	 * @param List of XMLImageInformation wrapper class that represents
	 * information about images.
	 */
	public static Document createDocument(List<XMLImageInformation> imageInformation) 
	{
		try {
			
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		
			Element rootElement = document.createElement("lockbum");
			document.appendChild(rootElement);
			
			Element email = document.createElement("email");
			email.setTextContent(Application.user.getEmail());
			rootElement.appendChild(email);
			
			Element images = document.createElement("images");
			
			for (XMLImageInformation information : imageInformation) {
				Element image = document.createElement("image");
				
				Element name = document.createElement("name");
				name.setTextContent(information.getName());
				
				Element height = document.createElement("height");
				height.setTextContent(information.getHeight().toString());

				Element width = document.createElement("width");
				width.setTextContent(information.getWidth().toString());
				
				Element hash = document.createElement("hash");
				hash.setTextContent(information.getHash());
				
				image.appendChild(name);
				image.appendChild(height);
				image.appendChild(width);
				image.appendChild(hash);
				
				images.appendChild(image);
			}
			
			rootElement.appendChild(images);
			
			Element date = document.createElement("date");
			date.setTextContent(new Date().toString());
			rootElement.appendChild(date);
			
			return document;
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Encrypts a document.
	 * 
	 * @param Document to be encrypted.
	 */
	public static Document encryptDocument(Document document) {
		SecretKey secretKey = SecurityService.generateDataEncryptionKey();
		
		Certificate cert = SecurityService.readCertificate();
		
		document = encrypt(document, secretKey, cert);

		return document;		
	}
	
	private static Document encrypt(Document document, SecretKey key, Certificate certificate) {

		try {

			// cipher za kriptovanje XML-a
			XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.TRIPLEDES);
			
			// inicijalizacija za kriptovanje
			xmlCipher.init(XMLCipher.ENCRYPT_MODE, key);

			// cipher za kriptovanje tajnog kljuca,
			// Koristi se Javni RSA kljuc za kriptovanje
			XMLCipher keyCipher = XMLCipher.getInstance(XMLCipher.RSA_v1dot5);
			
			// inicijalizacija za kriptovanje tajnog kljuca javnim RSA kljucem
			keyCipher.init(XMLCipher.WRAP_MODE, certificate.getPublicKey());
			
			// kreiranje EncryptedKey objekta koji sadrzi  enkriptovan tajni (session) kljuc
			EncryptedKey encryptedKey = keyCipher.encryptKey(document, key);
			
			// u EncryptedData element koji se kriptuje kao KeyInfo stavljamo
			// kriptovan tajni kljuc
			// ovaj element je koreni elemnt XML enkripcije
			EncryptedData encryptedData = xmlCipher.getEncryptedData();
			
			// kreira se KeyInfo element
			KeyInfo keyInfo = new KeyInfo(document);
			
			// postavljamo naziv 
			keyInfo.addKeyName("Kriptovani tajni kljuc");
			
			// postavljamo kriptovani kljuc
			keyInfo.add(encryptedKey);
			
			// postavljamo KeyInfo za element koji se kriptuje
			encryptedData.setKeyInfo(keyInfo);

			// trazi se element ciji sadrzaj se kriptuje
			NodeList images = document.getElementsByTagName("images");
			Element image = (Element) images.item(0);

			xmlCipher.doFinal(document, image, true); // kriptuje sa sadrzaj

			return document;

		} catch (XMLEncryptionException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Document signDocument(Document document) {
		
		PrivateKey privateKey = SecurityService.readPrivateKey();			
		Certificate certificate = SecurityService.readCertificate();
		
		document = signDocument(document, privateKey, certificate);
		
		return document;
	}
	
	private static Document signDocument(Document doc, PrivateKey privateKey, Certificate cert) {
      
      try {
			Element rootEl = doc.getDocumentElement();
			
			//kreira se signature objekat
			XMLSignature sig = new XMLSignature(doc, null, XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1);
			
			//kreiraju se transformacije nad dokumentom
			Transforms transforms = new Transforms(doc);
			    
			//iz potpisa uklanja Signature element
			//Ovo je potrebno za enveloped tip po specifikaciji
			transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
			
			//normalizacija
			transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
			    
			//potpisuje se citav dokument (URI "")
			sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
			    
			//U KeyInfo se postavalja Javni kljuc samostalno i citav sertifikat
			sig.addKeyInfo(cert.getPublicKey());
			sig.addKeyInfo((X509Certificate) cert);
			    
			//poptis je child root elementa
			rootEl.appendChild(sig.getElement());
			
			//potpisivanje
			sig.sign(privateKey);
			
			return doc;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public static File saveDocument(Document document, String fileName) {
		try {
			File outFile = new File(fileName);
			FileOutputStream writer = new FileOutputStream(outFile);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();

			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(writer);

			transformer.transform(source, result);

			writer.close();
			
			return outFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static File generateDocument(List<File> images) {
		return saveDocument(signDocument(createDocument(XMLInformationFromImages(images))), "info_signed.xml");
	}
}