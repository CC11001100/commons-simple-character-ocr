package cc11001100.ocr;

import cc11001100.ocr.clean.ImageCleanImpl;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author CC11001100
 */
public class CleanTest {

	@Ignore
	@Test
	public void test_001() {
//		try {
//			String imgPath = "D:/test/captcha/w3c/raw/1521861009097.png";
//			BufferedImage rawImage = ImageIO.read(new File(imgPath));
//			BufferedImage cleanedImage = new ImageCleanImpl().clean(rawImage);
//			ImageIO.write(cleanedImage, "png", new File("D:/a.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Ignore
	@Test
	public void test_002() throws IOException {

		BufferedImage img = ImageIO.read(new File("D:/test/proxy/kubobo/raw/1521882842030.png"));

//		for(int i=0; i<img.getHeight(); i++){
//			for(int j=0; j<img.getWidth(); j++){
//				System.out.printf("%4s", Integer.toString(img.getRGB(j, i) & 0XFF000000, 16).toUpperCase());
//			}
//			System.out.println();
//		}

		img = new ImageCleanImpl(0).clean(img);
		ImageIO.write(img, "png", new File("D:/a.png"));


	}

}
