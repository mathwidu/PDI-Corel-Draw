import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private JLabel originalImageLabel, transformedImageLabel;
    private BufferedImage originalImage;

    public ImagePanel() {
        setLayout(new GridLayout(1, 2));
        originalImageLabel = new JLabel("Imagem Original", SwingConstants.CENTER);
        transformedImageLabel = new JLabel("Imagem Transformada", SwingConstants.CENTER);

        originalImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        transformedImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        add(originalImageLabel);
        add(transformedImageLabel);
    }

    public void setOriginalImage(BufferedImage image) {
        this.originalImage = image;
        originalImageLabel.setIcon(new ImageIcon(image.getScaledInstance(originalImageLabel.getWidth(), originalImageLabel.getHeight(), Image.SCALE_SMOOTH)));
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }
}

