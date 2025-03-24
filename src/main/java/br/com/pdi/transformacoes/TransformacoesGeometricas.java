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

    public static ImageMatrix rotacionar(ImageMatrix imagem, double anguloGraus) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;

        double angulo = Math.toRadians((anguloGraus));
        double cos = Math.cos(angulo);
        double sin = Math.sin(angulo);

        int centroX = largura / 2;
        int centroY = altura / 2;

        int[][] novaMatriz = new int[altura][largura];

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                novaMatriz[y][x] = 0xFFFFFFFF; // branco (ARGB)
            }
        }

        for (int y = 0; y < altura; y++) {
           for (int x = 0; x < largura; x++) {
               int xCentralizado = x - centroX;
               int yCentralizado = y - centroY;

               int novoX = (int)Math.round(xCentralizado * cos - yCentralizado * sin);
               int novoY = (int) Math.round(xCentralizado * sin + yCentralizado * cos);

               novoX += centroX;
               novoY += centroY;

               if (novoX >= 0 && novoX < largura && novoY >= 0 && novoY < altura) {
                   novaMatriz[novoY][novoX] = original[y][x];
               }
           }
        }
        return new ImageMatrix(novaMatriz);
    }

    // Os outros métodos (espelhar, escalar)

}
