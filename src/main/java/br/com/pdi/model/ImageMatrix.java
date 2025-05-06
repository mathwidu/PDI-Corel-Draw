package br.com.pdi.model;

import java.awt.image.BufferedImage;

// Classe que encapsula uma imagem como matriz de pixels do tipo ARGB
public class ImageMatrix {

    private int[][] pixelMatrix; // Matriz de inteiros representando os pixels (cada valor contém ARGB)
    private int width, height;   // Largura e altura da imagem

    // Construtor que recebe uma imagem do tipo BufferedImage e carrega a matriz de pixels
    public ImageMatrix(BufferedImage image) {
        carregarImagem(image);
    }

    // Construtor alternativo que recebe diretamente uma matriz de pixels
    public ImageMatrix(int[][] pixelMatrix) {
        this.pixelMatrix = pixelMatrix;
        this.height = pixelMatrix.length;         // Altura = número de linhas
        this.width = pixelMatrix[0].length;       // Largura = número de colunas
    }

    // Método privado que transforma um BufferedImage em matriz de inteiros (ARGB)
    private void carregarImagem(BufferedImage image) {
        width = image.getWidth();     // Lê largura da imagem
        height = image.getHeight();   // Lê altura da imagem

        pixelMatrix = new int[height][width]; // Inicializa a matriz de pixels

        // Percorre todos os pixels da imagem (linha por linha)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Lê valor ARGB do pixel (x, y) e armazena na matriz
                pixelMatrix[y][x] = image.getRGB(x, y);
            }
        }
    }

    // Converte a matriz de volta para um BufferedImage para poder exibir/salvar
    public BufferedImage toBufferedImage() {
        // Cria uma nova imagem ARGB com as dimensões salvas
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Percorre toda a matriz e insere os pixels na imagem
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, pixelMatrix[y][x]); // Define o pixel da imagem
            }
        }

        return image; // Retorna o BufferedImage reconstruído
    }

    // Getter para acessar a matriz de pixels inteira
    public int[][] getPixelMatrix() {
        return pixelMatrix;
    }

    // Setter para substituir a matriz atual por outra (se tiver o mesmo tamanho)
    public void setPixelMatrix(int[][] newMatrix) {
        // Valida se as dimensões da nova matriz são compatíveis
        if (newMatrix.length == height && newMatrix[0].length == width) {
            this.pixelMatrix = newMatrix;
        } else {
            throw new IllegalArgumentException("Dimensões da matriz inválidas.");
        }
    }

    // Recupera o valor ARGB de um pixel específico (x, y)
    public int getPixel(int x, int y) {
        return pixelMatrix[y][x];
    }

    // Define o valor ARGB de um pixel específico (x, y)
    public void setPixel(int x, int y, int color) {
        pixelMatrix[y][x] = color;
    }
}
