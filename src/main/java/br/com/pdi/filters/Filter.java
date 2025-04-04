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

                int alpha = (pixel >> 24) & 0xFF;
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
        // filtro de suavização (média, gaussiana etc.)
        return imagem;
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
