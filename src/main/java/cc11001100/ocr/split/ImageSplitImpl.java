package cc11001100.ocr.split;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片切割的默认实现：按照空白列切割
 *
 * @author CC11001100
 */
public class ImageSplitImpl implements ImageSplit {

	private Integer backgroundColor;

	public ImageSplitImpl() {
		this(0XFFFFFF);
	}

	public ImageSplitImpl(Integer backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	@Override
	public List<BufferedImage> split(BufferedImage img) {
		return mattingCharacter(img);
	}

	/**
	 * 切割字符
	 *
	 * @param img 去除噪音后的图片
	 * @return 切割出的有序单个字符图片
	 */
	public List<BufferedImage> mattingCharacter(BufferedImage img) {
		List<BufferedImage> list = new ArrayList<>();

		int w = img.getWidth();
		int h = img.getHeight();

		boolean lastColumnIsBackground = true;
		int beginColumn = -1;

		for (int i = 0; i < w; i++) {

			boolean currentColumnIsBackground = true;
			for (int j = 0; currentColumnIsBackground && j < h; j++) {
				currentColumnIsBackground = (img.getRGB(i, j) & 0XFFFFFF) == backgroundColor;
				System.out.println(Integer.toString((img.getRGB(i, j) & 0XFFFFFF), 16).toUpperCase());
			}

			System.out.println(lastColumnIsBackground + ", " + currentColumnIsBackground);

			// TODO fix bug
			// 进入字符区域
			if (lastColumnIsBackground && !currentColumnIsBackground) {
				beginColumn = i;
			} else if (!lastColumnIsBackground && currentColumnIsBackground) {
				// 离开字符区域，离开字符区域时将上一个字符割出来
				BufferedImage charImage = img.getSubimage(beginColumn, 0, i - beginColumn, h);
				BufferedImage trimCharImage = trimUpAndDown(charImage);
				list.add(trimCharImage);
			}

			lastColumnIsBackground = currentColumnIsBackground;
		}

		return list;
	}

	/**
	 * 去除图片上方和下方的空白
	 *
	 * @param img
	 * @return
	 */
	private BufferedImage trimUpAndDown(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();

		// 计算上方空白
		int upY = 0;
		for (int i = 0; i < h; i++) {

			boolean currentRowIsBackground = true;
			for (int j = 0; currentRowIsBackground && j < w; j++) {
				currentRowIsBackground = (img.getRGB(j, i) & 0X00FFFFFF) == backgroundColor;
			}

			if (!currentRowIsBackground) {
				upY = i;
				break;
			}

		}

		// 计算下方空白
		int downY = h;
		for (int i = h - 1; i >= 0; i--) {

			boolean currentRowIsBackground = true;
			for (int j = 0; currentRowIsBackground && j < w; j++) {
				currentRowIsBackground = (img.getRGB(j, i) & 0XFFFFFF) == backgroundColor;
			}

			if (!currentRowIsBackground) {
				downY = i;
				break;
			}
		}

		return img.getSubimage(0, upY, w, downY - upY + 1);
	}

}
