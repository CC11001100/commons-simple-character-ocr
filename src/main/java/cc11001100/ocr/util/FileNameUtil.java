package cc11001100.ocr.util;

/**
 * @author CC11001100
 */
public class FileNameUtil {

	/**
	 * 得到没有扩展名的文件名
	 *
	 * @param path
	 * @return
	 */
	public static String getFileNameNoExtension(String path) {
		int right = path.lastIndexOf(".");
		if (right == -1) {
			right = path.length();
		}

		int left = path.lastIndexOf("/");
		if (left == -1) {
			left = 0;
		} else {
			left++;
		}

		return path.substring(left, right);
	}

}
