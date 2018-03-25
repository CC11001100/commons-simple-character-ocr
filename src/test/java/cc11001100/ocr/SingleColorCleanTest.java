package cc11001100.ocr;

import cc11001100.ocr.clean.SingleColorFilterClean;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author CC11001100
 */
public class SingleColorCleanTest {

	@Ignore
	@Test
	public void test_001() throws IOException {
		SingleColorFilterClean singleColorClean = new SingleColorFilterClean();
		BufferedImage rawImage = ImageIO.read(new File("E:/a.png"));
		BufferedImage cleanedImage = singleColorClean.clean(rawImage);
		ImageIO.write(cleanedImage, "png", new File("E:/b.png"));
	}

}
