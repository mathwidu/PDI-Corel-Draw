package br.com.pdi.model;

import java.awt.image.BufferedImage;

public class ImageMatrix {
    private int[][] pixelMatrix;
    private int width, height;

    public ImageMatrix(BufferedImage image) {
        carregarImagem(image);
    }


    public ImageMatrix(int[][] pixelMatrix) {
        this.pixelMatrix = pixelMatrix;
        this.height = pixelMatrix.length;
        this.width = pixelMatrix[0].length;
    }

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

    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, pixelMatrix[y][x]);
            }
        }

        return image;
    }


    public int[][] getPixelMatrix() {
        return pixelMatrix;
    }

    public void setPixelMatrix(int[][] newMatrix) {
        if (newMatrix.length == height && newMatrix[0].length == width) {
            this.pixelMatrix = newMatrix;
        } else {
            throw new IllegalArgumentException("Dimensões da matriz inválidas.");
        }
    }

    public int getPixel(int x, int y) {
        return pixelMatrix[y][x];
    }

    public void setPixel(int x, int y, int color) {
        pixelMatrix[y][x] = color;
    }
}
