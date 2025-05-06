package br.com.pdi.morfologia;

import br.com.pdi.model.ImageMatrix;

// Classe que implementa operações de Morfologia Matemática
// Ideal para processar imagens binárias ou em tons de cinza
public class Morfologia {

    // DILATAÇÃO: Expande regiões brancas (valores altos), preenchendo bordas
    public static ImageMatrix dilatar(ImageMatrix imagem) {
        int[][] original = imagem.getPixelMatrix(); // Obtém a matriz original da imagem
        int altura = original.length;
        int largura = original[0].length;

        // Nova matriz que vai conter o resultado da dilatação
        int[][] novaMatriz = new int[altura][largura];

        // Percorre cada pixel, ignorando as bordas para evitar index out of bounds
        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {

                int maxValor = 0; // Inicializa o maior valor da vizinhança como 0

                // Percorre a vizinhança 3x3 do pixel atual
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int pixel = original[y + dy][x + dx];
                        int gray = (pixel >> 16) & 0xFF; // Extrai o valor de cinza
                        maxValor = Math.max(maxValor, gray); // Mantém o maior valor encontrado
                    }
                }

                // Cria um novo pixel com todos os canais R, G e B iguais ao valor máximo
                int novoPixel = (0xFF << 24) | (maxValor << 16) | (maxValor << 8) | maxValor;
                novaMatriz[y][x] = novoPixel;
            }
        }

        return new ImageMatrix(novaMatriz); // Retorna a imagem dilatada
    }

    // EROSÃO: Encolhe regiões brancas, removendo pixels das bordas
    public static ImageMatrix erodir(ImageMatrix imagem) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;

        int[][] novaMatriz = new int[altura][largura];

        // Percorre todos os pixels exceto bordas
        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {

                int minValor = 255; // Começa assumindo o maior valor possível

                // Analisa a vizinhança 3x3 ao redor do pixel atual
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int pixel = original[y + dy][x + dx];
                        int gray = (pixel >> 16) & 0xFF;
                        minValor = Math.min(minValor, gray); // Guarda o menor valor encontrado
                    }
                }

                // Cria novo pixel com o valor mínimo encontrado
                int novoPixel = (0xFF << 24) | (minValor << 16) | (minValor << 8) | minValor;
                novaMatriz[y][x] = novoPixel;
            }
        }

        return new ImageMatrix(novaMatriz); // Retorna a imagem erodida
    }

    // ABERTURA: Erosão seguida de dilatação
    // Remove ruídos pequenos e suaviza bordas
    public static ImageMatrix abrir(ImageMatrix imagem) {
        ImageMatrix imagemCinza = imagem; // (opcional: aplicar grayscale aqui)
        ImageMatrix erodida = erodir(imagemCinza);      // Primeiro erode a imagem
        ImageMatrix aberta = dilatar(erodida);          // Depois dilata o resultado
        return aberta;
    }

    // FECHAMENTO: Dilatação seguida de erosão
    // Preenche pequenos buracos e liga regiões próximas
    public static ImageMatrix fechar(ImageMatrix imagem) {
        ImageMatrix imagemCinza = imagem; // (opcional: aplicar grayscale aqui)
        ImageMatrix dilatada = dilatar(imagemCinza);    // Primeiro dilata a imagem
        ImageMatrix fechada = erodir(dilatada);         // Depois erode o resultado
        return fechada;
    }
}
