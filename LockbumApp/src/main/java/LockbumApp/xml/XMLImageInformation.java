package LockbumApp.xml;

/**
 * Wrapper class for information that needs to be
 * contained in zip file for every image.
 *
 */

public class XMLImageInformation {

	private String name;
	private Integer height;
	private Integer width;
	private String hash;

	public XMLImageInformation(String name, Integer height, Integer width, String hash) {
		this.name = name;
		this.height = height;
		this.width = width;
		this.hash = hash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	@Override
	public String toString() {
		return "ImageInformation [name=" + name + ", height=" + height + ", width=" + width + ", hash=" + hash + "]";
	}

}
