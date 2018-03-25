package cc11001100.ocr;

import cc11001100.ocr.clean.SingleColorFilterClean;
import cc11001100.ocr.split.ImageSplitImpl;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author CC11001100
 */
public class SplitTest {

	@Ignore
	@Test
	public void test_001() throws IOException {
		BufferedImage img = ImageIO.read(new File("D:/a.png"));
		System.out.println(new ImageSplitImpl().split(img));
		ImageIO.write(img, "png", new File("D:/b.png"));
	}

	@Ignore
	@Test
	public void test_002() throws IOException {
		BufferedImage img = ImageIO.read(new File("E:/test/proxy/ant/raw/1521997979655.png"));
		BufferedImage cleanedImg = new SingleColorFilterClean().clean(img);
		ImageIO.write(cleanedImg, "png", new File("E:/test/proxy/ant/res/a.png"));
		List<BufferedImage> charImgList = new ImageSplitImpl().split(cleanedImg);
		for(int i=0; i<charImgList.size(); i++){
			ImageIO.write(charImgList.get(i), "png", new File("E:/test/proxy/ant/res/_" + i + ".png"));
		}
	}

}
