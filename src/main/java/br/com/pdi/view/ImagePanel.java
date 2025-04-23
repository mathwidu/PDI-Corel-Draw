package br.com.pdi.view;

import br.com.pdi.model.ImageMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class ImagePanel extends JPanel {
    private JLabel originalImageLabel, transformedImageLabel;
    private ImageMatrix originalImageMatrix, transformedImageMatrix;
    private JButton confirmarEdicaoButton, desfazerEdicaoButton;

    private Stack<ImageMatrix> historicoEdicoes;

    public ImagePanel() {
        originalImageLabel = new JLabel("Imagem Original", SwingConstants.CENTER);
        transformedImageLabel = new JLabel("Imagem Transformada", SwingConstants.CENTER);

        originalImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        transformedImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        confirmarEdicaoButton = new JButton("Confirmar Edição");
        confirmarEdicaoButton.setVisible(false);
        confirmarEdicaoButton.addActionListener(e -> confirmarEdicao());

        desfazerEdicaoButton = new JButton("Desfazer");
        desfazerEdicaoButton.setVisible(false);
        desfazerEdicaoButton.addActionListener(e -> desfazerEdicao());

        historicoEdicoes = new Stack<>();

        setLayout(new BorderLayout());

        JPanel imagensPanel = new JPanel(new GridLayout(1, 2));
        imagensPanel.add(originalImageLabel);
        imagensPanel.add(transformedImageLabel);
        add(imagensPanel, BorderLayout.CENTER);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoesPanel.add(confirmarEdicaoButton);
        botoesPanel.add(desfazerEdicaoButton);
        add(botoesPanel, BorderLayout.SOUTH);
    }

    public void setOriginalImageMatrix(ImageMatrix imageMatrix) {
        this.originalImageMatrix = imageMatrix;
        this.transformedImageMatrix = null;
        historicoEdicoes.clear(); 
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
    }

    public void atualizarExibicao() {
        int panelWidth = getWidth() / 2;
        int panelHeight = getHeight();

        if (panelWidth == 0 || panelHeight == 0) return;

        if (originalImageMatrix != null) {
            originalImageLabel.setIcon(new ImageIcon(escalarImagem(originalImageMatrix.toBufferedImage(), panelWidth, panelHeight)));
            originalImageLabel.setText(null);
        }

        if (transformedImageMatrix != null) {
            transformedImageLabel.setIcon(new ImageIcon(escalarImagem(transformedImageMatrix.toBufferedImage(), panelWidth, panelHeight)));
            transformedImageLabel.setText(null);
            confirmarEdicaoButton.setVisible(true);
        } else {
            transformedImageLabel.setIcon(null);
            transformedImageLabel.setText("Imagem Transformada");
            confirmarEdicaoButton.setVisible(false);
        }

        desfazerEdicaoButton.setVisible(!historicoEdicoes.isEmpty());
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

    private void confirmarEdicao() {
        if (transformedImageMatrix != null) {
            historicoEdicoes.push(originalImageMatrix);
            originalImageMatrix = transformedImageMatrix;
            transformedImageMatrix = null;
            atualizarExibicao();
            JOptionPane.showMessageDialog(this, "Edição confirmada. Agora você pode aplicar novos filtros.");
        }
    }

    private void desfazerEdicao() {
        if (!historicoEdicoes.isEmpty()) {
            originalImageMatrix = historicoEdicoes.pop();
            transformedImageMatrix = null;
            JOptionPane.showMessageDialog(this, "Última edição desfeita.");
            atualizarExibicao();
        }
    }
}
