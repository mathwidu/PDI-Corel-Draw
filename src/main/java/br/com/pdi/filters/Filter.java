package br.com.pdi.filters;

import br.com.pdi.model.ImageMatrix;

// Classe responsável por aplicar diversos filtros sobre uma imagem representada pela classe ImageMatrix
public class Filter {

    // Método que ajusta brilho e contraste da imagem
    public static ImageMatrix ajustarBrilhoContraste(ImageMatrix imagem, double contraste, int brilho){
        int[][] original = imagem.getPixelMatrix(); // Recupera a matriz original de pixels
        int altura = original.length;
        int largura = original[0].length;

        int[][] novaMatriz = new int[altura][largura]; // Cria nova matriz para armazenar pixels ajustados

        for(int y = 0; y < altura; y++){
            for(int x = 0; x < largura; x++){
                int pixel = original[y][x]; // Pega o pixel atual

                // Separa os canais de cor (ARGB)
                int alpha = (pixel >> 24) & 0xFF;
                int red   = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue  = pixel & 0xFF;

                // Aplica brilho e contraste
                red   = (int)(contraste * red + brilho);
                green = (int)(contraste * green + brilho);
                blue  = (int)(contraste * blue + brilho);

                // Garante que os valores fiquem entre 0 e 255
                red   = Math.min(Math.max(red, 0), 255);
                green = Math.min(Math.max(green, 0), 255);
                blue  = Math.min(Math.max(blue, 0), 255);

                // Recria o pixel com os novos valores
                int novoPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                novaMatriz[y][x] = novoPixel;
            }
        }

        return new ImageMatrix(novaMatriz);
    }

    // Converte a imagem colorida em tons de cinza (grayscale)
    public static ImageMatrix aplicarGrayscale(ImageMatrix imagem) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;
        int[][] novaMatriz = new int[altura][largura];

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                int pixel = imagem.getPixel(x, y);

                int alpha = (pixel >> 24); // Pega o canal alpha
                int red   = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8)  & 0xFF;
                int blue  = pixel & 0xFF;

                // Calcula a média ponderada com base na percepção visual
                int gray = (int)(0.2125 * red + 0.7154 * green + 0.0721 * blue);

                // Cria pixel com todos os canais iguais (tons de cinza)
                int novoPixel = (alpha << 24) | (gray << 16) | (gray << 8) | gray;
                novaMatriz[y][x] = novoPixel;
            }
        }

        return new ImageMatrix(novaMatriz);
    }

    // Aplica filtro de suavização (blur) — Filtro Passa-Baixa com kernel fixo 3x3
    public static ImageMatrix aplicarPassaBaixa(ImageMatrix imagem) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;
        int[][] novaMatriz = new int[altura][largura];

        // Kernel Gaussiano 3x3
        int[][] kernel = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
        };
        int kernelSum = 16;

        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                int somaR = 0, somaG = 0, somaB = 0;

                // Aplica convolução com o kernel
                for (int ky = 0; ky < 3; ky++) {
                    for (int kx = 0; kx < 3; kx++) {
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

        return new ImageMatrix(novaMatriz);
    }

    // Aplica filtro de detecção de bordas com Sobel — Filtro Passa-Alta
    public static ImageMatrix aplicarPassaAlta(ImageMatrix imagem) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;
        int[][] novaMatriz = new int[altura][largura];

        // Máscaras de Sobel (X e Y)
        int[][] sobelX = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
        };
        int[][] sobelY = {
            {1, 2, 1},
            {0, 0, 0},
            {-1, -2, -1}
        };

        int threshold = 128;

        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                int gx = 0, gy = 0;

                // Aplica convolução com os kernels de Sobel
                for (int ky = 0; ky < 3; ky++) {
                    for (int kx = 0; kx < 3; kx++) {
                        int pixel = original[y + ky - 1][x + kx - 1];
                        int gray = (pixel >> 16) & 0xFF;

                        gx += gray * sobelX[ky][kx];
                        gy += gray * sobelY[ky][kx];
                    }
                }

                // Calcula a magnitude do gradiente
                int magnitude = (int)Math.sqrt(gx * gx + gy * gy);
                magnitude = magnitude > threshold ? 255 : 0; // Aplica threshold (binário)

                int alpha = (original[y][x] >> 24) & 0xFF;
                novaMatriz[y][x] = (alpha << 24) | (magnitude << 16) | (magnitude << 8) | magnitude;
            }
        }

        return new ImageMatrix(novaMatriz);
    }

    // Binariza a imagem com base em um valor limiar
    public static ImageMatrix aplicarThreshold(ImageMatrix imagem, int limiar) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;
        int[][] novaMatriz = new int[altura][largura];

        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                int pixel = original[y][x];

                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;

                // Se o valor do vermelho for maior que o limiar, vira branco (255); caso contrário, preto (0)
                int binario = red >= limiar ? 255 : 0;
                int novoPixel = (alpha << 24) | (binario << 16) | (binario << 8) | binario;

                novaMatriz[y][x] = novoPixel;
            }
        }

        return new ImageMatrix(novaMatriz);
    }

    // Filtro Passa-Alta com threshold ajustável
    public static ImageMatrix aplicarPassaAltaComThreshold(ImageMatrix imagem, int threshold) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;
        int[][] novaMatriz = new int[altura][largura];

        int[][] sobelX = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
        };

        int[][] sobelY = {
            {1, 2, 1},
            {0, 0, 0},
            {-1, -2, -1}
        };

        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                int gx = 0, gy = 0;

                for (int ky = 0; ky < 3; ky++) {
                    for (int kx = 0; kx < 3; kx++) {
                        int pixel = original[y + ky - 1][x + kx - 1];
                        int gray = (pixel >> 16) & 0xFF;

                        gx += gray * sobelX[ky][kx];
                        gy += gray * sobelY[ky][kx];
                    }
                }

                int magnitude = (int) Math.sqrt(gx * gx + gy * gy);
                magnitude = magnitude > threshold ? 255 : 0;

                int alpha = (original[y][x] >> 24) & 0xFF;
                novaMatriz[y][x] = (alpha << 24) | (magnitude << 16) | (magnitude << 8) | magnitude;
            }
        }

        return new ImageMatrix(novaMatriz);
    }

    // Filtro Passa-Baixa com kernel de tamanho variável (ex: 3x3, 5x5...)
    public static ImageMatrix aplicarPassaBaixaComKernel(ImageMatrix imagem, int tamanhoKernel) {
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;
        int[][] novaMatriz = new int[altura][largura];

        int offset = tamanhoKernel / 2;
        int kernelArea = tamanhoKernel * tamanhoKernel;

        for (int y = offset; y < altura - offset; y++) {
            for (int x = offset; x < largura - offset; x++) {
                int somaR = 0, somaG = 0, somaB = 0;

                for (int ky = -offset; ky <= offset; ky++) {
                    for (int kx = -offset; kx <= offset; kx++) {
                        int pixel = original[y + ky][x + kx];
                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = pixel & 0xFF;

                        somaR += r;
                        somaG += g;
                        somaB += b;
                    }
                }

                int mediaR = somaR / kernelArea;
                int mediaG = somaG / kernelArea;
                int mediaB = somaB / kernelArea;
                int alpha = (original[y][x] >> 24) & 0xFF;

                int novoPixel = (alpha << 24) | (mediaR << 16) | (mediaG << 8) | mediaB;
                novaMatriz[y][x] = novoPixel;
            }
        }

        return new ImageMatrix(novaMatriz);
    }

}
