package br.com.pdi.transformacoes;

import br.com.pdi.model.ImageMatrix;

// Classe que realiza transformações geométricas em imagens
public class TransformacoesGeometricas {

    // Método para transladar (mover) a imagem pelos eixos X e Y
    public static ImageMatrix transladar(ImageMatrix imagem, int dx, int dy) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;

        // Calcula quanto precisamos deslocar a imagem para não perder pixels com translação negativa
        int deslocamentoX = Math.max(0, -dx);
        int deslocamentoY = Math.max(0, -dy);

        // Calcula o novo tamanho da imagem considerando o deslocamento
        int novaLargura = largura + Math.abs(dx);
        int novaAltura = altura + Math.abs(dy);

        // Cria nova matriz preenchida com pixels transparentes
        int[][] novaMatriz = new int[novaAltura][novaLargura];

        for (int y = 0; y < novaAltura; y++) {
            for (int x = 0; x < novaLargura; x++) {
                novaMatriz[y][x] = 0x00000000; // pixel preto e transparente
            }
        }

        // Copia os pixels da imagem original para a nova posição na imagem transformada
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                int novoX = x + dx + deslocamentoX;
                int novoY = y + dy + deslocamentoY;

                // Verifica se a nova posição está dentro dos limites
                if (novoX >= 0 && novoX < novaLargura && novoY >= 0 && novoY < novaAltura) {
                    novaMatriz[novoY][novoX] = original[y][x];
                }
            }
        }

        return new ImageMatrix(novaMatriz);
    }


    // Método para rotacionar a imagem em torno de seu centro por um ângulo em graus
    public static ImageMatrix rotacionar(ImageMatrix imagem, double anguloGraus) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;

        // Converte o ângulo para radianos e calcula seno e cosseno
        double angulo = Math.toRadians(anguloGraus);
        double cos = Math.cos(angulo);
        double sin = Math.sin(angulo);

        int centroX = largura / 2;
        int centroY = altura / 2;

        // Define os cantos da imagem em relação ao centro
        int[] xCantos = {-centroX, largura - centroX, -centroX, largura - centroX};
        int[] yCantos = {-centroY, -centroY, altura - centroY, altura - centroY};

        double[] xRot = new double[4];
        double[] yRot = new double[4];

        // Rotaciona cada canto para determinar o novo tamanho da imagem
        for (int i = 0; i < 4; i++) {
            xRot[i] = xCantos[i] * cos - yCantos[i] * sin;
            yRot[i] = xCantos[i] * sin + yCantos[i] * cos;
        }

        // Encontra os extremos da imagem rotacionada
        double minX = xRot[0], maxX = xRot[0];
        double minY = yRot[0], maxY = yRot[0];

        for (int i = 1; i < 4; i++) {
            minX = Math.min(minX, xRot[i]);
            maxX = Math.max(maxX, xRot[i]);
            minY = Math.min(minY, yRot[i]);
            maxY = Math.max(maxY, yRot[i]);
        }

        // Calcula as novas dimensões da imagem rotacionada
        int novaLargura = (int) Math.ceil(maxX - minX);
        int novaAltura = (int) Math.ceil(maxY - minY);

        // Cria nova matriz com fundo transparente
        int[][] novaMatriz = new int[novaAltura][novaLargura];
        for (int y = 0; y < novaAltura; y++) {
            for (int x = 0; x < novaLargura; x++) {
                novaMatriz[y][x] = 0x00000000;
            }
        }

        // Define o novo centro da imagem rotacionada
        int novoCentroX = novaLargura / 2;
        int novoCentroY = novaAltura / 2;

        // Rotaciona cada pixel da imagem original e reposiciona na nova matriz
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                int xCentralizado = x - centroX;
                int yCentralizado = y - centroY;

                int novoX = (int) Math.round(xCentralizado * cos - yCentralizado * sin);
                int novoY = (int) Math.round(xCentralizado * sin + yCentralizado * cos);

                int destinoX = novoCentroX + novoX;
                int destinoY = novoCentroY + novoY;

                if (destinoX >= 0 && destinoX < novaLargura && destinoY >= 0 && destinoY < novaAltura) {
                    novaMatriz[destinoY][destinoX] = original[y][x];
                }
            }
        }

        return new ImageMatrix(novaMatriz);
    }


    // Método para espelhar a imagem (horizontal ou vertical)
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
                    // Espelhamento horizontal: inverte a posição X
                    novoX = largura - 1 - x;
                    novoY = y;
                }
                else{
                    // Espelhamento vertical: inverte X e Y
                    novoX = largura - 1 - x;
                    novoY = altura - 1 - y;
                }
                novaMatriz[novoY][novoX] = original[y][x];
            }
        }
        return new ImageMatrix(novaMatriz);
    }

    // Método para escalar a imagem com os fatores de escala sx (horizontal) e sy (vertical)
    public static ImageMatrix escalar(ImageMatrix imagem, double sx, double sy){
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;

        // Calcula as novas dimensões da imagem
        int novaLargura = (int) (largura * sx);
        int novaAltura = (int) (altura * sy);

        int[][] novaMatriz = new int[novaAltura][novaLargura];

        // Preenche a nova matriz com preto transparente
        for (int y = 0; y < novaAltura; y++) {
            for (int x = 0; x < novaLargura; x++) {
                novaMatriz[y][x] = 0x00000000;
            }
        }

        // Mapeia cada pixel da nova imagem para um pixel correspondente da imagem original
        for(int y = 0; y < novaAltura; y++){
            for(int x = 0; x < novaLargura; x++){
                int origemX = (int) (x / sx);
                int origemY = (int) (y / sy);

                // Garante que as coordenadas estejam dentro dos limites da imagem original
                if (origemX >= 0 && origemX < largura && origemY >= 0 && origemY < altura) {
                    novaMatriz[y][x] = original[origemY][origemX];
                }
            }
        }

        return new ImageMatrix(novaMatriz);
    }

}