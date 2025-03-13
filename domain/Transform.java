package domain;

import java.awt.image.BufferedImage;
import utilities.BufferedImageUtils;

public class Transform {
    public BufferedImage translate(BufferedImage image, int[] startPosition, int[] desiredPosition) {
        BufferedImage newImage = BufferedImageUtils.deepCopy(image);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int pixelColor = image.getRGB(x, y);

                int xPosition = x + desiredPosition[0];
                int yPosition = y + desiredPosition[1];

                newImage.setRGB(xPosition, yPosition, pixelColor);
            }
        }

        return newImage;
    }
}
