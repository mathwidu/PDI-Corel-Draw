package br.com.pdi.transformacoes;

import br.com.pdi.model.ImageMatrix;

public class TransformacoesGeometricas {

    public static ImageMatrix transladar(ImageMatrix imagem, int dx, int dy) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;

        int novaAltura = altura + Math.abs(dy);
        int novaLargura = largura + Math.abs(dx);
        int[][] novaMatriz = new int[novaAltura][novaLargura];

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                int novoX = x + dx;
                int novoY = y + dy;

                if (novoX >= 0 && novoX < novaLargura && novoY >= 0 && novoY < novaAltura) {
                    novaMatriz[novoY][novoX] = original[y][x];
                }
            }
        }

        return new ImageMatrix(novaMatriz);
    }

    // Os outros métodos (rotacionar, espelhar, escalar)
}
