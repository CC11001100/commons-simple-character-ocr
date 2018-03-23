package cc11001100.ocr.split;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 用于切割图片，将其每个字符切割出来
 *
 * @author CC11001100
 */
@FunctionalInterface
public interface ImageSplit {

	/**
	 * 用于切割图片，将其每个字符切割出来
	 *
	 * @apiNote 此方法要求切割出的字符图片与其在原来图片的顺序是一致的
	 * @param img 清除噪音后的图片
	 * @return 切割出的有序的每个字符图片
	 */
	List<BufferedImage> split(BufferedImage img);

}
