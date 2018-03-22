package cc11001100.ocr.clean;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CC11001100
 */
public class ImageCleanImpl implements ImageClean {

	@Override
	public BufferedImage clean(BufferedImage img) {
		return noiseClean(img, 10);
	}

	/**
	 * 去噪点，使用连通域大小来判断
	 *
	 * @param originalCaptcha 原始的验证码图片
	 * @param areaSizeFilter  连通域小于等于此大小的将被过滤掉
	 * @return
	 */
	private BufferedImage noiseClean(BufferedImage originalCaptcha, int areaSizeFilter) {

		// 会有一些干扰边，把边缘部分切割丢掉
		int edgeDropWidth = 15;
		BufferedImage captcha = originalCaptcha.getSubimage(edgeDropWidth / 2, edgeDropWidth / 2,  //
		originalCaptcha.getWidth() - edgeDropWidth, originalCaptcha.getHeight() - edgeDropWidth);

		int w = captcha.getWidth();
		int h = captcha.getHeight();
		int[][] book = new int[w][h];

		// 连通域最大的色块将被认为是背景色，这样实现了自动识别背景色
		Map<Integer, Integer> flagAreaSizeMap = new HashMap<>();
		int currentFlag = 1;
		int maxAreaSizeFlag = currentFlag;
		int maxAreaSizeColor = 0XFFFFFFFF;

		// 标记
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

				if (book[i][j] != 0) {
					continue;
				}

				book[i][j] = currentFlag;
				int currentColor = captcha.getRGB(i, j);
				int areaSize = waterFlow(captcha, book, i, j, currentColor, currentFlag);

				if (areaSize > flagAreaSizeMap.getOrDefault(maxAreaSizeFlag, 0)) {
					maxAreaSizeFlag = currentFlag;
					maxAreaSizeColor = currentColor;
				}

				flagAreaSizeMap.put(currentFlag, areaSize);
				currentFlag++;
			}
		}

		// 复制
		BufferedImage resultImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int currentColor = captcha.getRGB(i, j);
				if (book[i][j] == maxAreaSizeFlag //
				|| (currentColor & 0XFFFFFF) == (maxAreaSizeColor & 0XFFFFFF) //
				|| flagAreaSizeMap.get(book[i][j]) <= areaSizeFilter) {
					resultImage.setRGB(i, j, 0XFFFFFFFF);
				} else {
					resultImage.setRGB(i, j, currentColor);
				}
			}
		}
		return resultImage;
	}

	/**
	 * 将图像抽象为颜色矩阵
	 *
	 * @param img
	 * @param book
	 * @param x
	 * @param y
	 * @param color
	 * @param flag
	 * @return
	 */
	private int waterFlow(BufferedImage img, int[][] book, int x, int y, int color, int flag) {

		if (x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight()) {
			return 0;
		}

		// 这个1统计的是当前点
		int areaSize = 1;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int nextX = x + i;
				int nextY = y + j;

				if (nextX < 0 || nextX >= img.getWidth() || nextY < 0 || nextY >= img.getHeight()) {
					continue;
				}

				// 如果这一点没有被访问过，并且颜色相同
				//              if (book[nextX][nextY] == 0 && isSimilar(img.getRGB(nextX, nextY), color, 0)) {
				if (book[nextX][nextY] == 0 && (img.getRGB(nextX, nextY) & 0XFFFFFF) == (color & 0XFFFFFF)) {
					book[nextX][nextY] = flag;
					areaSize += waterFlow(img, book, nextX, nextY, color, flag);
				}

			}
		}

		return areaSize;
	}

}
