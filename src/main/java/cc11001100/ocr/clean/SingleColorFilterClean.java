package cc11001100.ocr.clean;

import cc11001100.ocr.util.ImageUtil;

import java.awt.image.BufferedImage;

/**
 * 单色过滤器，除了指定颜色之外的其它颜色将被过滤掉，只保留指定颜色
 * 不指定要保留的颜色的话默认为只保留黑色
 * 透明色和其他色将被置为默认的背景色
 *
 * @author CC11001100
 */
public class SingleColorFilterClean implements ImageClean {

	/**
	 * 要保留的颜色
	 */
	private Integer saveColor;

	public SingleColorFilterClean() {
		this(0X000000);
	}

	public SingleColorFilterClean(Integer saveColor) {
		this.saveColor = saveColor;
	}

	@Override
	public BufferedImage clean(BufferedImage img) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				// 这里四个通道的信息都需要
				int argb = img.getRGB(i, j);
				int a = (argb & 0XFF000000) >> 24;
				int rgb = argb & 0X00FFFFFF;
				// 需要注意png的透明背景色，透明色或不是指定颜色的统统置为背景色
				if (a == 0 || rgb != saveColor) {
					res.setRGB(i, j, ImageUtil.DEFAULT_BACKGROUND_COLOR);
				} else {
					res.setRGB(i, j, argb);
				}
			}
		}
		return res;
	}

}
