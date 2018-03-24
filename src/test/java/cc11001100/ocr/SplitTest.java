package cc11001100.ocr;

import cc11001100.ocr.split.ImageSplitImpl;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author CC11001100
 */
public class SplitTest {

	@Test
	public void test_001() throws IOException {

		BufferedImage img = ImageIO.read(new File("D:/a.png"));
		System.out.println(new ImageSplitImpl().split(img));
		ImageIO.write(img, "png", new File("D:/b.png"));

	}

}
