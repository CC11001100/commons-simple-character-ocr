package cc11001100.ocr;

import cc11001100.ocr.clean.ImageCleanImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author CC11001100
 */
public class CleanTest {

	@Test
	public void test_001() {
		try {
			String imgPath = "D:/test/captcha/w3c/raw/1521791930313.png";
			BufferedImage rawImage = ImageIO.read(new File(imgPath));
			BufferedImage cleanedImage = new ImageCleanImpl().clean(rawImage);
			ImageIO.write(cleanedImage, "png", new File("D:/a.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
