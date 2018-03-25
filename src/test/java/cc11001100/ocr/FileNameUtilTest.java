package cc11001100.ocr;

import cc11001100.ocr.util.FileNameUtil;
import org.junit.Test;

/**
 * @author CC11001100
 */
public class FileNameUtilTest {

	@Test
	public void test_001(){

		System.out.println(FileNameUtil.getFileNameNoExtension(".."));
		System.out.println(FileNameUtil.getFileNameNoExtension("/a.png"));
		System.out.println(FileNameUtil.getFileNameNoExtension("/a/b.png"));
		System.out.println(FileNameUtil.getFileNameNoExtension("/a"));

	}

}
