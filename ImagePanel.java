import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private JLabel originalImageLabel, transformedImageLabel;
    private ImageMatrix originalImageMatrix, transformedImageMatrix;

    public ImagePanel() {
        setLayout(new GridLayout(1, 2));
        originalImageLabel = new JLabel("Imagem Original", SwingConstants.CENTER);
        transformedImageLabel = new JLabel("Imagem Transformada", SwingConstants.CENTER);

        originalImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        transformedImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        add(originalImageLabel);
        add(transformedImageLabel);
    }

    public void setOriginalImageMatrix(ImageMatrix imageMatrix) {
        this.originalImageMatrix = imageMatrix;
        this.transformedImageMatrix = null; // Resetamos a transformada ao carregar nova imagem
        atualizarExibicao();
    }

    public void setTransformedImageMatrix(ImageMatrix imageMatrix) {
        this.transformedImageMatrix = imageMatrix;
        atualizarExibicao();
    }

    public ImageMatrix getOriginalImageMatrix() {
        return originalImageMatrix;
    }

    public ImageMatrix getTransformedImageMatrix() {
        return transformedImageMatrix;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        atualizarExibicao();
    }

    private void atualizarExibicao() {
        int panelWidth = getWidth() / 2;
        int panelHeight = getHeight();

        if (panelWidth == 0 || panelHeight == 0) return;

        if (originalImageMatrix != null) {
            originalImageLabel.setIcon(new ImageIcon(escalarImagem(originalImageMatrix.toBufferedImage(), panelWidth, panelHeight)));
        }

        if (transformedImageMatrix != null) {
            transformedImageLabel.setIcon(new ImageIcon(escalarImagem(transformedImageMatrix.toBufferedImage(), panelWidth, panelHeight)));
        } else {
            transformedImageLabel.setIcon(null);
            transformedImageLabel.setText("Imagem Transformada");
        }
    }

    private Image escalarImagem(BufferedImage image, int maxWidth, int maxHeight) {
        double aspectRatio = (double) image.getWidth() / image.getHeight();
        int newWidth, newHeight;

        if (maxWidth / aspectRatio <= maxHeight) {
            newWidth = maxWidth;
            newHeight = (int) (maxWidth / aspectRatio);
        } else {
            newWidth = (int) (maxHeight * aspectRatio);
            newHeight = maxHeight;
        }

        return image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }
}
