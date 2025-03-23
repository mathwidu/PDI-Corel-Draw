package br.com.pdi.model;

import java.awt.image.BufferedImage;

public class ImageMatrix {
    private int[][] pixelMatrix; // Matriz da imagem
    private int width, height; // Dimensões da imagem

    public ImageMatrix(BufferedImage image) {
        carregarImagem(image);
    }

    // Converte a BufferedImage em matriz de pixels
    private void carregarImagem(BufferedImage image) {
        width = image.getWidth();
        height = image.getHeight();
        pixelMatrix = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelMatrix[y][x] = image.getRGB(x, y);
            }
        }
    }

    // Converte a matriz de volta para BufferedImage
    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, pixelMatrix[y][x]);
            }
        }

        return image;
    }

    // Getter para a matriz de pixels
    public int[][] getPixelMatrix() {
        return pixelMatrix;
    }

    // Setter para modificar a matriz
    public void setPixelMatrix(int[][] newMatrix) {
        if (newMatrix.length == height && newMatrix[0].length == width) {
            this.pixelMatrix = newMatrix;
        } else {
            throw new IllegalArgumentException("Dimensões da matriz inválidas.");
        }
    }

    // Obtém um pixel específico
    public int getPixel(int x, int y) {
        return pixelMatrix[y][x];
    }

    // Define um pixel específico
    public void setPixel(int x, int y, int color) {
        pixelMatrix[y][x] = color;
    }
}
