package cc11001100.ocr;

import cc11001100.ocr.clean.ImageClean;
import cc11001100.ocr.clean.ImageCleanImpl;
import cc11001100.ocr.http.HttpUtil;
import cc11001100.ocr.split.ImageSplit;
import cc11001100.ocr.split.ImageSplitImpl;
import cc11001100.ocr.util.ImageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static cc11001100.ocr.util.ImageUtil.imageHashCode;
import static java.util.stream.Collectors.joining;

/**
 * @author CC11001100
 */
public class OcrUtil {

	private static Logger logger = LogManager.getLogger(OcrUtil.class);

	private Map<Integer, String> dictionaryMap;
	private ImageClean imageClean;
	private ImageSplit imageSplit;

	public OcrUtil() {
		this.imageClean = new ImageCleanImpl();
		this.imageSplit = new ImageSplitImpl();
	}

	public OcrUtil(ImageClean imageClean, ImageSplit imageSplit) {
		this.imageClean = imageClean;
		this.imageSplit = imageSplit;
	}

	public ImageClean getImageClean() {
		return imageClean;
	}

	public OcrUtil setImageClean(ImageClean imageClean) {
		this.imageClean = imageClean;
		return this;
	}

	public ImageSplit getImageSplit() {
		return imageSplit;
	}

	public OcrUtil setImageSplit(ImageSplit imageSplit) {
		this.imageSplit = imageSplit;
		return this;
	}

	public Map<Integer, String> getDictionaryMap() {
		return dictionaryMap;
	}

	public void setDictionaryMap(Map<Integer, String> dictionaryMap) {
		this.dictionaryMap = dictionaryMap;
	}

	/**
	 * 初始化
	 *
	 * @param url         验证码或图片的url
	 * @param times       请求多少次
	 * @param saveBaseDir 下载下来的文件存储到的临时目录
	 */
	public void init(String url, int times, String saveBaseDir) {
		String downloadSaveDir = saveBaseDir + "/raw/";
		String splitCharSaveDir = saveBaseDir + "/char/";

		File downloadSaveDirFile = new File(downloadSaveDir);
		File splitCharSaveDirFile = new File(splitCharSaveDir);

		// 随意指定路径时可自动创建
		if (!downloadSaveDirFile.exists()) {
			downloadSaveDirFile.mkdirs();
		}
		if (!splitCharSaveDirFile.exists()) {
			splitCharSaveDirFile.mkdirs();
		}

		HttpUtil.batchDownload(url, times, downloadSaveDir);
		init(downloadSaveDir, splitCharSaveDir);
	}

	/**
	 * 初始化
	 *
	 * @param fromBasePath 存储原始图片的文件夹
	 * @param toBasePath   处理后保存到的文件夹
	 */
	public void init(String fromBasePath, String toBasePath) {
		init(fromBasePath, file -> true, toBasePath);
	}

	/**
	 * 初始化
	 *
	 * @param fromBasePath 存储原始图片的文件夹
	 * @param filter       读原始图片文件夹的过滤器
	 * @param toBasePath   处理后保存到的文件夹
	 */
	public void init(String fromBasePath, FileFilter filter, String toBasePath) {
		File[] files = new File(fromBasePath).listFiles(filter);
		if (files == null) {
			logger.info("{} not one file can read, check dir or filter, exit.", fromBasePath);
			return;
		}
		int threadNum = Runtime.getRuntime().availableProcessors() * 5;
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		ConcurrentHashMap<Integer, BufferedImage> rawDictionaryMap = new ConcurrentHashMap<>();
		for (File file : files) {
			executorService.execute(() -> {
				BufferedImage img = null;
				try {
					img = ImageIO.read(file);
				} catch (IOException e) {
					logger.error("read file {} failed.", file.getAbsolutePath());
					return;
				}
				BufferedImage cleanedImage = imageClean.clean(img);
				List<BufferedImage> charList = imageSplit.split(cleanedImage);
				charList.forEach(x -> {
					int hashcode = imageHashCode(x);
					rawDictionaryMap.putIfAbsent(hashcode, x);
				});
				logger.info("split {} over", file.getName());
			});
		}
		try {
			executorService.shutdown();
			executorService.awaitTermination(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			logger.info("thread poll error");
		}

		// 将分割去重之后的字符图片保存到磁盘上
		logger.info("begin write distinct char image...");
		rawDictionaryMap.forEach((k, v) -> {
			String outputImageFileName = toBasePath + "/" + k + ".png";
			try {
				ImageIO.write(v, "png", new File(outputImageFileName));
				logger.info("write char img to {}", outputImageFileName);
			} catch (IOException e) {
				logger.error("write image error");
			}
		});

		logger.info("auto process part over, now turn you!");
		// 判断操作系统类型，自动打开标注文件所在目录
//		if (System.getProperty("os.name").contains("Windows")) {
//			try {
//				Runtime.getRuntime().exec("cmd /c start explorer " + toBasePath);
//			} catch (IOException e) {
//				logger.info("open explorer failed, dir={}", toBasePath);
//			}
//		}
	}

	/**
	 * 从一个已经标注号的目录加载字符映射
	 *
	 * @param charBaseDir
	 */
	public void loadDictionaryMap(String charBaseDir) {
		dictionaryMap = ImageUtil.genDictionary(charBaseDir);
		dictionaryMap.forEach((k, v) -> logger.info("load dictionary mapping = ({}, {})", k, v));
	}

	/**
	 * 直接从一个map加载映射
	 *
	 * @param dictionaryMap
	 */
	public void loadDictionaryMap(Map<Integer, String> dictionaryMap) {
		this.dictionaryMap = dictionaryMap;
	}

	/**
	 * 出入原始图片，输出识别到的字符
	 *
	 * @param img
	 * @return
	 */
	public String ocr(BufferedImage img) {
		BufferedImage cleanedImg = imageClean.clean(img);
		List<BufferedImage> charList = imageSplit.split(cleanedImg);
		return charList.stream()
				.map(x -> dictionaryMap.getOrDefault(imageHashCode(x), ""))
				.collect(joining());
	}

	/**
	 * 输入原图片和预期长度，输出识别到的字符，长度不符会抛出异常
	 *
	 * @param img
	 * @param exceptLength
	 * @return
	 */
	public String ocr(BufferedImage img, int exceptLength) {
		String plainText = ocr(img);
		assert plainText.length() == exceptLength;
		return plainText;
	}

}
