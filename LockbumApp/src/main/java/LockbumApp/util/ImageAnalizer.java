package LockbumApp.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAnalizer {
	
	public static List<File> findImagesInFolder(File folder)
	{
		ArrayList<File> images = new ArrayList<File>();
		
		for(File file : folder.listFiles())
			if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".jpeg"))
				images.add(file);
		
		return images;
	}

}
