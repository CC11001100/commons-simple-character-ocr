package cc11001100.ocr.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author CC11001100
 */
public class ImageUtil {

	private static Logger logger = LogManager.getLogger(ImageUtil.class);

	public static final Integer DEFAULT_BACKGROUND_COLOR = 0XFFFFFF;

	/**
	 * 计算图像的哈希值，即将图片内容压缩为一个整数
	 *
	 * @param img 要被hash的图像
	 * @return hashcode
	 * @apiNote 只适用于小图像
	 */
	public static int imageHashCode(BufferedImage img) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				sb.append(i).append("|").append(j).append("|").append(img.getRGB(i, j) & 0X00FFFFFF).append("|");
			}
		}
		return sb.toString().hashCode();
	}

	/**
	 * 从标注好的目录读入映射字典
	 *
	 * @param taggedCharImageDir 标注好的字符图片所在的目录
	 * @param fileFilter         读此目录的可筛选
	 * @return 映射字典
	 * @see #genDictionary(String)
	 */
	public static Map<Integer, String> genDictionary(String taggedCharImageDir, FileFilter fileFilter, Function<String, String> processFilename) {
		Map<Integer, String> dictionaryMap = new HashMap<>();
		File[] charImageFiles = new File(taggedCharImageDir).listFiles(fileFilter);
		if (charImageFiles == null) {
			throw new IllegalArgumentException("read char dictionary map null, check dir " + taggedCharImageDir + " or filter");
		}
		for (File charImageFile : charImageFiles) {
			try {
				BufferedImage charBufferedImage = ImageIO.read(charImageFile);
				int charHashCode = imageHashCode(charBufferedImage);
				String charName = processFilename.apply(FileNameUtil.getFileNameNoExtension(charImageFile.getName()));
				dictionaryMap.put(charHashCode, charName);
			} catch (IOException e) {
				logger.error("read file {} error", charImageFile.getAbsolutePath());
			}
		}
		if (dictionaryMap.isEmpty()) {
			throw new IllegalArgumentException("read char dictionary map empty, check dir " + taggedCharImageDir + " or filter");
		}
		return dictionaryMap;
	}

	/**
	 * 从标注好的目录读入映射字典
	 *
	 * @param taggedCharImageDir 标注好的字符图片所在的目录
	 * @return 映射字典
	 * @see #genDictionary(String, FileFilter, Function)
	 */
	public static Map<Integer, String> genDictionary(String taggedCharImageDir) {
		return genDictionary(taggedCharImageDir, file -> true, filename -> filename);
	}

}
