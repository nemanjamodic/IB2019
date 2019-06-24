package LockbumApp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipArchiver {

	public static File imagesToZip(List<File> images, File signedXML, String archiveName)
	{
		File archive = new File(archiveName);
		
		byte[] buf = new byte[1024];
	    try {
	        ZipOutputStream writer = new ZipOutputStream(new FileOutputStream(archive));

	        for (File image : images) {
	            FileInputStream reader = new FileInputStream(image.getCanonicalFile());

	            writer.putNextEntry(new ZipEntry(image.getName()));

	            int len;
	            while((len = reader.read(buf)) > 0) {
	            	writer.write(buf, 0, len);
	            }
	            
	            writer.closeEntry();
	            reader.close();
	        }
	        
	        FileInputStream reader = new FileInputStream(signedXML.getCanonicalFile());

            writer.putNextEntry(new ZipEntry(signedXML.getName()));

            int len;
            while((len = reader.read(buf)) > 0) {
            	writer.write(buf, 0, len);
            }
            
            writer.closeEntry();
            reader.close();
	        
	        writer.close();
	        System.out.println(archive.getAbsolutePath());
	        return archive;
	    } catch (IOException ex) {
	        System.out.println(ex.getMessage());
	    }
		
		return null;
	}
	
}
