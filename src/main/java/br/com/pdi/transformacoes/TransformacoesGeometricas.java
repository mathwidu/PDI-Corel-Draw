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
    
        // 1. Converter ângulo para radianos
        double angulo = Math.toRadians(anguloGraus);
        double cos = Math.cos(angulo);
        double sin = Math.sin(angulo);
    
        // 2. Calcular o centro da imagem original
        int centroX = largura / 2;
        int centroY = altura / 2;
    
        // 3. Obter os 4 cantos da imagem, centralizados no (0,0)
        int[] xCantos = {-centroX, largura - centroX, -centroX, largura - centroX};
        int[] yCantos = {-centroY, -centroY, altura - centroY, altura - centroY};
    
        // 4. Aplicar rotação nos 4 cantos
        double[] xRot = new double[4];
        double[] yRot = new double[4];
    
        for (int i = 0; i < 4; i++) {
            xRot[i] = xCantos[i] * cos - yCantos[i] * sin;
            yRot[i] = xCantos[i] * sin + yCantos[i] * cos;
        }
    
        // 5. Encontrar os limites mínimos e máximos
        double minX = xRot[0], maxX = xRot[0];
        double minY = yRot[0], maxY = yRot[0];
    
        for (int i = 1; i < 4; i++) {
            minX = Math.min(minX, xRot[i]);
            maxX = Math.max(maxX, xRot[i]);
            minY = Math.min(minY, yRot[i]);
            maxY = Math.max(maxY, yRot[i]);
        }
    
        // 6. Calcular o novo tamanho da imagem
        int novaLargura = (int) Math.ceil(maxX - minX);
        int novaAltura = (int) Math.ceil(maxY - minY);
    
        // 7. Criar nova matriz com fundo transparente
        int[][] novaMatriz = new int[novaAltura][novaLargura];
        for (int y = 0; y < novaAltura; y++) {
            for (int x = 0; x < novaLargura; x++) {
                novaMatriz[y][x] = 0x00000000;
            }
        }
    
        // 8. Novo centro da nova matriz
        int novoCentroX = novaLargura / 2;
        int novoCentroY = novaAltura / 2;
    
        // 9. Rotacionar cada pixel e colocá-lo na nova matriz
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                // Centraliza coordenadas em torno do centro original
                int xCentralizado = x - centroX;
                int yCentralizado = y - centroY;
    
                // Aplica rotação
                int novoX = (int) Math.round(xCentralizado * cos - yCentralizado * sin);
                int novoY = (int) Math.round(xCentralizado * sin + yCentralizado * cos);
    
                // Ajusta para o novo centro da imagem
                int destinoX = novoCentroX + novoX;
                int destinoY = novoCentroY + novoY;
    
                // Verifica se está dentro dos limites
                if (destinoX >= 0 && destinoX < novaLargura && destinoY >= 0 && destinoY < novaAltura) {
                    novaMatriz[destinoY][destinoX] = original[y][x];
                }
            }
        }
    
        // 10. Retorna a nova imagem
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
