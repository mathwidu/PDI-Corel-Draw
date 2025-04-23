package br.com.pdi.filters;

import br.com.pdi.model.ImageMatrix;

public class Filter {
    public static ImageMatrix ajustarBrilhoContraste(ImageMatrix imagem, double contraste, int brilho){
        int[][] original = imagem.getPixelMatrix();
        int altura = original.length;
        int largura = original[0].length;
        int[][] novaMatriz = new int[altura][largura];
    
        for(int y = 0; y < altura; y++){
            for(int x = 0; x < largura; x++){
                int pixel = original[y][x];
    
                int alpha = (pixel >> 24) & 0xFF;
                int red   = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue  = pixel & 0xFF;
    
                
                red = (int)(contraste * red + brilho);
                green = (int)(contraste * green + brilho);
                blue = (int)(contraste * blue + brilho);
    
                
                red = Math.min(Math.max(red, 0), 255);
                green = Math.min(Math.max(green, 0), 255);
                blue = Math.min(Math.max(blue, 0), 255);
    
                int novoPixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                novaMatriz[y][x] = novoPixel;
            }
        }
    
        return new ImageMatrix(novaMatriz);
    }
    

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

                
                int gray = (int)(0.2125 * red + 0.7154 * green + 0.0721 * blue);
                
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
    
        
        int[][] kernel = {
            {1, 2, 1},
            {2, 4, 2},
            {1, 2, 1}
        };
        int kernelSize = 3;
        int kernelSum = 16; 
    
        for (int y = 1; y < altura - 1; y++) {
            for (int x = 1; x < largura - 1; x++) {
                if (y == 0 || y == altura - 1 || x == 0 || x == largura - 1) {
                    novaMatriz[y][x] = original[y][x]; 
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
    
        int threshold = 128; 
    
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
    
                int magnitude = (int)Math.sqrt(gx * gx + gy * gy);
                magnitude = magnitude > threshold ? 255 : 0;
    
                int alpha = (original[y][x] >> 24) & 0xFF;
                novaMatriz[y][x] = (alpha << 24) | (magnitude << 16) | (magnitude << 8) | magnitude;
            }
        }
    
        return new ImageMatrix(novaMatriz);
    }
    

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
    
                int binario = red >= limiar ? 255 : 0;
                int novoPixel = (alpha << 24) | (binario << 16) | (binario << 8) | binario;
    
                novaMatriz[y][x] = novoPixel;
            }
        }
    
        return new ImageMatrix(novaMatriz);
    }
    

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
