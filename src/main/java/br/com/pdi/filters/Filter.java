package br.com.pdi.filters;

import br.com.pdi.model.ImageMatrix;

public class Filter {

    public static ImageMatrix aplicarGrayscale(ImageMatrix imagem) {
        // lógica para converter RGB em tons de cinza
        return imagem;
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
