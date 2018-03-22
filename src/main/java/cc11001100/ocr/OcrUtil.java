package cc11001100.ocr;

import cc11001100.ocr.clean.ImageClean;
import cc11001100.ocr.clean.ImageCleanImpl;
import cc11001100.ocr.http.HttpUtil;
import cc11001100.ocr.split.ImageSplit;
import cc11001100.ocr.split.ImageSplitImpl;
import cc11001100.ocr.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author CC11001100
 */
public class OcrUtil {

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

	public void init(String url, int times, String saveBaseDir) {
		String downloadSaveDir = saveBaseDir + "/raw/";
		String splitCharSaveDir = saveBaseDir + "/char/";
		HttpUtil.batchDownload(url, times, downloadSaveDir);
		init(downloadSaveDir, splitCharSaveDir);
	}

	public void init(String fromBaseDir, String saveBaseDir) {
		init(fromBaseDir, file -> true, saveBaseDir);
	}

	public void init(String fromBaseDir, FileFilter filter, String saveBaseDir) {

	}

	public void loadDictionaryMap(String charBaseDir) {
		dictionaryMap = ImageUtil.genDictionary(charBaseDir);
	}

	public String ocr(BufferedImage img) {
		BufferedImage cleanedImg = imageClean.clean(img);
		List<BufferedImage> charList = imageSplit.split(cleanedImg);
		int imageHashCode = ImageUtil.imgHashCode(img);
		return charList.stream()
				.map(x -> dictionaryMap.getOrDefault(imageHashCode, ""))
				.collect(Collectors.joining());
	}

	public String ocr(BufferedImage img, int exceptLength) {
		String plainText = ocr(img);
		assert plainText.length() == exceptLength;
		return plainText;
	}

}
