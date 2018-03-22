package cc11001100.ocr.split;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CC11001100
 */
public class ImageSplitImpl implements ImageSplit {

	@Override
	public List<BufferedImage> split(BufferedImage img) {
		return mattingCharacter(img);
	}

	/**
	 * 切割字符
	 *
	 * @param img
	 * @return
	 */
	public static List<BufferedImage> mattingCharacter(BufferedImage img) {
		List<BufferedImage> list = new ArrayList<>();

		int w = img.getWidth();
		int h = img.getHeight();

		boolean lastColumnIsBlack = true;
		int beginColumn = -1;

		for (int i = 0; i < w; i++) {

			boolean currentColumnIsBlack = true;
			for (int j = 0; j < h; j++) {
				if ((img.getRGB(i, j) & 0XFFFFFF) != 0XFFFFFF) {
					currentColumnIsBlack = false;
				}
			}

			// 进入字符区域
			if (lastColumnIsBlack && !currentColumnIsBlack) {
				beginColumn = i;
			} else if (!lastColumnIsBlack && currentColumnIsBlack) {
				// 离开字符区域
				BufferedImage charImage = img.getSubimage(beginColumn, 0, i - beginColumn, h);
				BufferedImage trimCharImage = trimUpAndDown(charImage);
				list.add(trimCharImage);
			}

			lastColumnIsBlack = currentColumnIsBlack;

		}

		return list;
	}

	private static BufferedImage trimUpAndDown(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();

		// 计算上方空白
		int upBeginLine = -1;
		for (int i = 0; i < h; i++) {

			boolean currentColumnIsBlack = true;
			for (int j = 0; j < w; j++) {
				if ((img.getRGB(j, i) & 0XFFFFFF) != 0XFFFFFF) {
					currentColumnIsBlack = false;
				}
			}

			if (!currentColumnIsBlack) {
				upBeginLine = i;
				break;
			}

		}

		// 计算下方空白
		int downBeginLine = -1;
		for (int i = h - 1; i >= 0; i--) {

			boolean currentColumnIsBlack = true;
			for (int j = 0; j < w; j++) {
				if ((img.getRGB(j, i) & 0XFFFFFF) != 0XFFFFFF) {
					currentColumnIsBlack = false;
				}
			}

			if (!currentColumnIsBlack) {
				downBeginLine = i;
				break;
			}
		}

		return img.getSubimage(0, upBeginLine, w, downBeginLine - upBeginLine + 1);
	}

}
