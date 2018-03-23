package cc11001100.ocr;

import org.junit.Test;

/**
 * @author CC11001100
 */
public class OcrTest {

	@Test
	public void testMain() {

		new OcrUtil().init("https://www.w3cschool.cn/scode", 1000, "D:/test/captcha/w3c/");


	}

}
