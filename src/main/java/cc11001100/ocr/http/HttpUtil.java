package cc11001100.ocr.http;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.http.client.fluent.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author CC11001100
 */
public class HttpUtil {

	private static Logger logger = LogManager.getLogger(HttpUtil.class);

	public static final Integer HIGH = 10000;
	public static final Integer MIDDLE = 5000;
	public static final Integer LOW = 3000;

	public static void batchDownload(String url, String basePath) {
		batchDownload(url, MIDDLE, basePath);
	}

	public static void batchDownload(String url, int howMany, String basePath) {
		int threadNum = Runtime.getRuntime().availableProcessors() * 10;
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		while (howMany-- > 0) {
			executorService.execute(() -> {
				download(url, basePath);
				logger.info("url={} save ok", url);
			});
		}
		try {
			executorService.shutdown();
			executorService.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.info("download thread poll error.");
		}

		logger.info("download done.");
	}

	public static void batchDownload(String url, int howMany, Consumer<byte[]> responseHandler) {
		int threadNum = Runtime.getRuntime().availableProcessors() * 10;
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		while (howMany-- > 0) {
			executorService.execute(() -> {
				byte[] responseBytes = download(url);
				responseHandler.accept(responseBytes);
				logger.info("url={} save ok", url);
			});
		}
		try {
			executorService.shutdown();
			executorService.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.info("download thread poll error.");
		}

		logger.info("download done.");
	}

	/**
	 * 下载文件将响应内容以字节码返回
	 *
	 * @param url
	 * @return
	 */
	public static byte[] download(String url) {
		for (int i = 0; i < 10; i++) {
			try {
				return Request.Get(url).execute().returnContent().asBytes();
			} catch (IOException e) {
				logger.info("url={}, cause={}, retry..", url, e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 下载文件并保存
	 *
	 * @param url
	 * @param basePath
	 */
	public static void download(String url, String basePath) {
		for (int i = 0; i < 10; i++) {
			try {
				String fileName = basePath + "/" + System.currentTimeMillis() + ".png";
				Request.Get(url).execute().saveContent(new File(fileName));
				break;
			} catch (IOException e) {
				logger.info("url={}, cause={}, retry..", url, e.getMessage());
			}
		}
	}

}
