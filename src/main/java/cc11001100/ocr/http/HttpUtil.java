package cc11001100.ocr.http;

import java.io.File;
import java.io.IOException;
import org.apache.http.client.fluent.Request;

/**
 * @author CC11001100
 */
public class HttpUtil {

	public static final Integer HIGH = 10000;
	public static final Integer MIDDLE = 5000;
	public static final Integer LOW = 3000;

	public static void batchDownload(String url, String saveBaseDir) {
		batchDownload(url, MIDDLE, saveBaseDir);
	}

	public static void batchDownload(String url, int times, String saveBaseDir) {
		while (times-- > 0) {
			download(url, saveBaseDir);
		}
	}

	private static void download(String url, String saveBaseDir) {
		for (int i = 0; i < 10; i++) {
			try {
				String fileName = saveBaseDir + "/" + System.currentTimeMillis() + ".png";
				Request.Get(url).execute().saveContent(new File(fileName));
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
