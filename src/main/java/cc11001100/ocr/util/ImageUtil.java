package cc11001100.ocr.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CC11001100
 */
public class ImageUtil {

	/**
	 * 计算图像的哈希值，即将图片内容压缩为一个整数
	 * <p>
	 * NOTE: 适用于小图像
	 *
	 * @param img
	 * @return
	 */
	public static int imgHashCode(BufferedImage img) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				sb.append(i).append("|").append(j).append("|").append(img.getRGB(i, j) & 0X00FFFFFF).append("|");
			}
		}
		return sb.toString().hashCode();
	}

	/**
	 * 根据字符图片生成字符字典
	 *
	 * @param charDirectory
	 */
	public static Map<Integer, String> genDictionary(String charDirectory, FileFilter fileFilter) {
		Map<Integer, String> dictionaryMap = new HashMap<>();
		File[] charImages = new File(charDirectory).listFiles(fileFilter);
		assert charImages != null;
		for (File charImgFile : charImages) {
			try {
				BufferedImage charBufferedImage = ImageIO.read(charImgFile);
				int charHashCode = imgHashCode(charBufferedImage);
				String charName = charImgFile.getName().split("\\.")[0];
				dictionaryMap.put(charHashCode, charName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dictionaryMap;
	}

	public static Map<Integer, String> genDictionary(String charDirectory){
		return genDictionary(charDirectory, file -> true);
	}

}
