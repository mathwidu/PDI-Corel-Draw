package br.com.pdi.transformacoes;

import br.com.pdi.model.ImageMatrix;

public class TransformacoesGeometricas {

    public static ImageMatrix transladar(ImageMatrix imagem, int dx, int dy) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;

        // Ajuste para lidar com valores negativos: cria deslocamento de origem
        int deslocamentoX = Math.max(0, -dx);
        int deslocamentoY = Math.max(0, -dy);

        int novaLargura = largura + Math.abs(dx);
        int novaAltura = altura + Math.abs(dy);

        int[][] novaMatriz = new int[novaAltura][novaLargura];

        // Preenche com branco (fundo)
        for (int y = 0; y < novaAltura; y++) {
            for (int x = 0; x < novaLargura; x++) {
                novaMatriz[y][x] = 0x00000000; // pixel transparente

            }
        }

        // Copia pixels da original para nova posição, ajustando deslocamento
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                int novoX = x + dx + deslocamentoX;
                int novoY = y + dy + deslocamentoY;

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
                novaMatriz[y][x] = 0x00000000; // pixel transparente
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

    public static ImageMatrix espelhar (ImageMatrix imagem, boolean horizontal){
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;

        int[][] novaMatriz = new int[altura][largura];
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                int novoX;
                int novoY;

                if(horizontal){
                    novoX = largura - 1 - x;
                    novoY = y;
                }
                else{
                    novoX = largura - 1 - x;
                    novoY = altura - 1 - y;
                }
                novaMatriz[novoY][novoX] = original[y][x];
            }
        }
        return new ImageMatrix(novaMatriz);
    }

    public static ImageMatrix escalar(ImageMatrix imagem, double sx, double sy){
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;

        int novaLargura = (int) (largura * sx);
        int novaAltura = (int) (altura * sy);

        int[][] novaMatriz = new int[novaAltura][novaLargura];

        // Preenche com pixels transparentes
        for (int y = 0; y < novaAltura; y++) {
            for (int x = 0; x < novaLargura; x++) {
                novaMatriz[y][x] = 0x00000000;
            }
        }

        for(int y = 0; y < novaAltura; y++){
            for(int x = 0; x < novaLargura; x++){
                int origemX = (int) (x / sx);
                int origemY = (int) (y / sy);

                if (origemX >= 0 && origemX < largura && origemY >= 0 && origemY < altura) {
                    novaMatriz[y][x] = original[origemY][origemX];
                }
            }
        }

        return new ImageMatrix(novaMatriz);
    }

}
