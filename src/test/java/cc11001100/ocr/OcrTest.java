package cc11001100.ocr;

import cc11001100.ocr.clean.ImageCleanImpl;
import cc11001100.ocr.http.HttpUtil;
import cc11001100.ocr.split.ImageSplitImpl;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * @author CC11001100
 */
public class OcrTest {

	@Ignore
	@Test
	public void testMain() {

		String captchaUrl = "https://www.w3cschool.cn/scode";
		OcrUtil ocrUtil = new OcrUtil();
		ocrUtil.setImageClean(new ImageCleanImpl(20));
		ocrUtil.setImageSplit(new ImageSplitImpl(4, 4));

		// 初始化标注数据
//		ocrUtil.init(captchaUrl, 1000, "D:/test/captcha/w3c/");

		// 验证是否正确
		ocrUtil.loadDictionaryMap("D:/test/captcha/w3c/char/");
		HttpUtil.batchDownload(captchaUrl, 100, responseByte -> {
			try {
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(responseByte));
				String filename = ocrUtil.ocr(img);
				String outputPath = "D:/test/captcha/w3c/check/" + filename + "_" + System.currentTimeMillis() + ".png";
				ImageIO.write(img, "png", new File(outputPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

}
