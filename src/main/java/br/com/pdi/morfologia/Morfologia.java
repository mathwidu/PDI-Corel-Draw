package br.com.pdi.morfologia;

import br.com.pdi.model.ImageMatrix;

public class Morfologia {
    public static ImageMatrix dilatar(ImageMatrix imagem) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;

        int[][] novaMatriz = new int[altura][largura];

        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {

                int maxValor = 0;

                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int pixel = original[y + dy][x + dx];
                        int gray = (pixel >> 16) & 0xFF;
                        maxValor = Math.max(maxValor, gray);
                    }
                }

                int novoPixel = (0xFF << 24) | (maxValor << 16) | (maxValor << 8) | maxValor;
                novaMatriz[y][x] = novoPixel;
            }
        }

        return new ImageMatrix(novaMatriz);
    }

    public static ImageMatrix erodir(ImageMatrix imagem) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;
    
        int[][] novaMatriz = new int[altura][largura];
    
        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
    
                int minValor = 255;
    
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int pixel = original[y + dy][x + dx];
                        int gray = (pixel >> 16) & 0xFF;
                        minValor = Math.min(minValor, gray);
                    }
                }
    
                int novoPixel = (0xFF << 24) | (minValor << 16) | (minValor << 8) | minValor;
                novaMatriz[y][x] = novoPixel;
            }
        }
    
        return new ImageMatrix(novaMatriz);
    }
    

    public static ImageMatrix abrir(ImageMatrix imagem) {
        ImageMatrix imagemCinza = imagem;
        ImageMatrix erodida = erodir(imagemCinza);
        ImageMatrix aberta = dilatar(erodida);
        return aberta;
    }
    
    public static ImageMatrix fechar(ImageMatrix imagem) {
        ImageMatrix imagemCinza = imagem;
        ImageMatrix dilatada = dilatar(imagemCinza);
        ImageMatrix fechada = erodir(dilatada);
        return fechada;
    }
}
