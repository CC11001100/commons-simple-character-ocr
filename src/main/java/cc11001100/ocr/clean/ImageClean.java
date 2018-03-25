package cc11001100.ocr.clean;

import java.awt.image.BufferedImage;

/**
 * 用于去除图片噪音
 *
 * @author CC11001100
 */
@FunctionalInterface
public interface ImageClean {

	/**
	 * 输入带噪音的图片，输出已消除噪音的图片
	 *
	 * 原始图片不应该被改变
	 *
	 * @param img 原始图片
	 * @return 去除噪音后的图片
	 */
	BufferedImage clean(BufferedImage img);

}
