package br.com.pdi.filters;

import br.com.pdi.model.ImageMatrix;

public class Filter {

    public static ImageMatrix aplicarGrayscale(ImageMatrix imagem) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;
        int[][] novaMatriz = new int[altura][largura];
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                int pixel = imagem.getPixel(x, y);

                int alpha = (pixel >> 24);
                int red   = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8)  & 0xFF;
                int blue  = pixel & 0xFF;

                // Cálculo da média ponderada
                int gray = (int)(0.2125 * red + 0.7154 * green + 0.0721 * blue);
                // Criação do novo pixel
                int novoPixel = (alpha << 24) | (gray << 16) | (gray << 8) | gray;
                novaMatriz[y][x] = novoPixel;
            }
        }
        
        return new ImageMatrix(novaMatriz);
    }

    public static ImageMatrix aplicarPassaBaixa(ImageMatrix imagem) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;
        int[][] novaMatriz = new int[altura][largura];
    
        // Kernel de média 3x3
        int[][] kernel = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
        };
        int kernelSize = 3;
        int kernelSum = 16; // soma de todos os valores do kernel
    
        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                if (y == 0 || y == altura - 1 || x == 0 || x == largura - 1) {
                    novaMatriz[y][x] = original[y][x]; // Mantém as bordas
                } else {
                int somaR = 0, somaG = 0, somaB = 0;
    
                for (int ky = 0; ky < kernelSize; ky++) {
                    for (int kx = 0; kx < kernelSize; kx++) {
                        int px = x + kx - 1;
                        int py = y + ky - 1;
                        int pixel = original[py][px];
    
                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = pixel & 0xFF;
    
                        int peso = kernel[ky][kx];
                        somaR += r * peso;
                        somaG += g * peso;
                        somaB += b * peso;
                    }
                }
    
                int mediaR = somaR / kernelSum;
                int mediaG = somaG / kernelSum;
                int mediaB = somaB / kernelSum;

                int alpha = (original[y][x] >> 24) & 0xFF;
                int novoPixel = (alpha << 24) | (mediaR << 16) | (mediaG << 8) | mediaB;
                novaMatriz[y][x] = novoPixel;
            }
        }
    }
    
        return new ImageMatrix(novaMatriz);
    }
    

    public static ImageMatrix aplicarPassaAlta(ImageMatrix imagem) {
        // filtro de nitidez (realce de bordas)
        return imagem;
    }

    public static ImageMatrix aplicarThreshold(ImageMatrix imagem, int limiar) {
        // binarização da imagem
        return imagem;
    }
}
