package cc11001100.ocr.split;

import cc11001100.ocr.util.ImageUtil;

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
	private Integer dropW;
	private Integer dropH;

	public ImageSplitImpl() {
		this(ImageUtil.DEFAULT_BACKGROUND_COLOR, 0, 0);
	}

	public ImageSplitImpl(Integer backgroundColor) {
		this(backgroundColor, 0, 0);
	}

	public ImageSplitImpl(Integer dropW, Integer dropH) {
		this(ImageUtil.DEFAULT_BACKGROUND_COLOR, dropW, dropH);
	}

	public ImageSplitImpl(Integer backgroundColor, Integer dropW, Integer dropH) {
		this.backgroundColor = backgroundColor;
		this.dropW = dropW;
		this.dropH = dropH;
	}

	@Override
	public List<BufferedImage> split(BufferedImage img) {
		return mattingCharacter(img);
	}

	public Integer getBackgroundColor() {
		return backgroundColor;
	}

	public ImageSplitImpl setBackgroundColor(Integer backgroundColor) {
		this.backgroundColor = backgroundColor;
		return this;
	}

	public Integer getDropW() {
		return dropW;
	}

	public ImageSplitImpl setDropW(Integer dropW) {
		this.dropW = dropW;
		return this;
	}

	public Integer getDropH() {
		return dropH;
	}

	public ImageSplitImpl setDropH(Integer dropH) {
		this.dropH = dropH;
		return this;
	}

	/**
	 * 切割字符
	 *
	 * @param img 去除噪音后的图片
	 * @return 切割出的有序单个字符图片
	 */
	public List<BufferedImage> mattingCharacter(BufferedImage img) {

		// 可能会有一些干扰边，是否需要去除
		if (dropW != 0 || dropH != 0) {
			img = img.getSubimage(dropW / 2, dropH / 2, img.getWidth() - dropW, img.getHeight() - dropH);
		}

		List<BufferedImage> list = new ArrayList<>();
		int w = img.getWidth();
		int h = img.getHeight();

		boolean lastColumnIsSeparator = true;
		int beginColumn = -1;

		for (int i = 0; i < w; i++) {

			boolean currentColumnIsSeparator = true;
			for (int j = 0; currentColumnIsSeparator && j < h; j++) {
				currentColumnIsSeparator = isSeparatorPoint(img.getRGB(i, j));
			}

			// 进入字符区域
			if (lastColumnIsSeparator && !currentColumnIsSeparator) {
				beginColumn = i;
			} else if (!lastColumnIsSeparator && currentColumnIsSeparator) {
				// 离开字符区域，离开字符区域时将上一个字符割出来
				BufferedImage charImage = img.getSubimage(beginColumn, 0, i - beginColumn, h);
				BufferedImage trimCharImage = trimUpAndDown(charImage);
				list.add(trimCharImage);
			}

			lastColumnIsSeparator = currentColumnIsSeparator;
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
				currentRowIsBackground = isSeparatorPoint(img.getRGB(j, i));
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
				currentRowIsBackground = isSeparatorPoint(img.getRGB(j, i));
			}

			if (!currentRowIsBackground) {
				downY = i;
				break;
			}
		}

		return img.getSubimage(0, upY, w, downY - upY + 1);
	}

	/**
	 * 判断一个点是否可以作为分隔符
	 *
	 * @param rgb
	 * @return
	 */
	private boolean isSeparatorPoint(int rgb) {
		// 颜色一致或者是透明色都认为是分隔符
		return (rgb & 0XFFFFFF) == (backgroundColor & 0XFFFFFF) || (rgb >> 24) == 0;
	}

}
