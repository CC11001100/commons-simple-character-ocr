package cc11001100.ocr;

import cc11001100.ocr.split.ImageSplitImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author CC11001100
 */
public class SplitTest {

	private static BufferedImage img;

	@BeforeClass
	public static void beforeClass() {
//		try {
//			String imgPath = "D:/test/captcha/w3c/raw/1521791930313.png";
//			BufferedImage rawImage = ImageIO.read(new File(imgPath));
//			img = new ImageCleanImpl().clean(rawImage);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Test
	public void test_001() throws IOException {

		img = ImageIO.read(new File("D:/a.png"));
		System.out.println(new ImageSplitImpl().setDropH(4).setDropW(4).split(img));
		ImageIO.write(img, "png", new File("D:/b.png"));

	}

}
