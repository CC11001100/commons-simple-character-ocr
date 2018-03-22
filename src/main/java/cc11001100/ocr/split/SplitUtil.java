package cc11001100.ocr.split;

import cc11001100.ocr.util.ImageUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * @author CC11001100
 */
public class SplitUtil {

	/**
	 * 得到字符字典
	 *
	 * @param srcDirectory
	 * @param destDirectory
	 */
	public static void splitCharacter(ImageSplit split, String srcDirectory, String destDirectory) {
		File file = new File(srcDirectory);
		File[] imgFileArray = file.listFiles();
		Map<Integer, BufferedImage> charDictionary = new HashMap<>();
		for (File imgFile : imgFileArray) {
			BufferedImage image = null;
			try {
				image = ImageIO.read(imgFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<BufferedImage> charList = split.split(image);
			charList.forEach(x -> {
				int hashcode = ImageUtil.imgHashCode(x);
				System.out.println(hashcode);
				charDictionary.put(hashcode, x);
			});
			System.out.println("split...");
		}
		charDictionary.forEach((k, v) -> {
			try {
				ImageIO.write(v, "png", new File(destDirectory + k + ".png"));
				System.out.println("write...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

	}

}
