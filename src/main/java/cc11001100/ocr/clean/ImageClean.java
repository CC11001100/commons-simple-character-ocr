package cc11001100.ocr.clean;

import java.awt.image.BufferedImage;

/**
 * @author CC11001100
 */
@FunctionalInterface
public interface ImageClean {

	BufferedImage clean(BufferedImage img);

}
