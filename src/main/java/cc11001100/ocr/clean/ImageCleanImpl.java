package cc11001100.ocr.clean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片去噪音的默认实现，默认使用最大连通域过滤
 *
 * @author CC11001100
 */
public class ImageCleanImpl implements ImageClean {

	private static Logger logger = LogManager.getLogger(ImageCleanImpl.class);

	/**
	 * 连通域面积小于此像素大小的点将被消除
	 */
	private int areaSize;

	public ImageCleanImpl() {
		this(20);
	}

	public ImageCleanImpl(int areaSize) {
		this.areaSize = areaSize;
	}

	@Override
	public BufferedImage clean(BufferedImage img) {
		return noiseClean(img, areaSize);
	}

	/**
	 * 去噪点，使用连通域大小来判断
	 *
	 * @param rawImage       原始的验证码图片
	 * @param areaSizeFilter 连通域小于等于此大小的将被过滤掉
	 * @return 去除噪点后的图片
	 */
	private BufferedImage noiseClean(BufferedImage rawImage, int areaSizeFilter) {

		int w = rawImage.getWidth();
		int h = rawImage.getHeight();
		int[][] book = new int[w][h];

		// <flag, areaSize> 记录每个颜色区域的大小
		Map<Integer, Integer> flagToAreaSizeMap = new HashMap<>();
		// 当出现下一个不同颜色的时候为其分配的标记位
		int nextFlag = 1;
		// 当前面积最大的预期的flag，具有这个标记位的将被认定为是背景色，这样实现了自动识别背景色
		int maxAreaSizeFlag = nextFlag;
		// 为什么需要记录背景色的颜色，因为比如数字8会产生两个封闭的背景色块要能够识别出来
		int maxAreaSizeColor = -1;

		// 1. 标记
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {

				// 此点已经在其它连通域被标记过了
				if (book[i][j] != 0) {
					continue;
				}

				// 为此颜色分配flag
				int currentColor = rawImage.getRGB(i, j) & 0XFFFFFF;
				Integer currentFlag = nextFlag++;

				int currentAreaSize = waterFlow(rawImage, book, i, j, currentColor, currentFlag);

				// 记录最大连通域
				if (currentAreaSize > flagToAreaSizeMap.getOrDefault(maxAreaSizeFlag, -1)) {
					maxAreaSizeFlag = currentFlag;
					maxAreaSizeColor = currentColor;
				}

				flagToAreaSizeMap.put(currentFlag, currentAreaSize);
				nextFlag++;
			}
		}

		// 2. 复制
		BufferedImage resultImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int currentColor = rawImage.getRGB(i, j) & 0XFFFFFF;
				boolean toBeFiltered = book[i][j] == maxAreaSizeFlag  // 背景色
						|| rgbEquals(currentColor, maxAreaSizeColor)
						|| flagToAreaSizeMap.get(book[i][j]) <= areaSizeFilter; // 连通域面积小于等于要过滤掉的连通域大小
				if (toBeFiltered) {
					resultImage.setRGB(i, j, 0X00FFFFFF);
				} else {
					resultImage.setRGB(i, j, currentColor & 0X00FFFFFF);
				}
			}
		}
		return resultImage;
	}

	/**
	 * 计算某颜色的最大连通域面积
	 *
	 * @param img   原始图片
	 * @param book  记录足迹
	 * @param x     当前在图片像素矩阵中的x坐标
	 * @param y     当前在图片像素矩阵中的y坐标
	 * @param color 当前连通域的颜色
	 * @param flag  当前连通域的标记位
	 * @return 此连通域的面积大小
	 */
	private int waterFlow(BufferedImage img, int[][] book, int x, int y, int color, int flag) {

		// 边界检查
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
				boolean canMove = book[nextX][nextY] == 0 && rgbEquals(img.getRGB(nextX, nextY), color);
				if (canMove) {
					book[nextX][nextY] = flag;
					areaSize += waterFlow(img, book, nextX, nextY, color, flag);
				}
			}
		}

		return areaSize;
	}

	/**
	 * 判断两个像素点的rgb值是否相等，忽略alpha通道
	 *
	 * @param rgb1
	 * @param rgb2
	 * @return
	 */
	private boolean rgbEquals(int rgb1, int rgb2) {
		return (rgb1 & 0XFFFFFF) == (rgb2 & 0XFFFFFF);
	}

}
