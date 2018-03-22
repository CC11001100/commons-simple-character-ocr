package cc11001100.ocr.split;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author CC11001100
 */
@FunctionalInterface
public interface ImageSplit {

	List<BufferedImage> split(BufferedImage img);

}
