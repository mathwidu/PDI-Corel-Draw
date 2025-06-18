package br.com.pdi.analysis;

import br.com.pdi.model.ImageMatrix; 
import br.com.pdi.filters.Filter; 
import br.com.pdi.morfologia.Morfologia; 

import javax.swing.*; 
import java.util.*; 

public class DesafioDetector {

    // Método principal que realiza toda a análise da imagem
    public static void analisarImagem(ImageMatrix imagem) {
        // Verifica se a imagem foi carregada corretamente
        if (imagem == null) {
            JOptionPane.showMessageDialog(null, "Nenhuma imagem carregada ou transformada disponível.");
            return; // Encerra o método se não houver imagem
        }

        // Etapa 1: binariza e aplica abertura morfológica para reduzir ruído
        ImageMatrix imagemBinaria = binarizar(imagem); // Converte em preto e branco
        imagemBinaria = Morfologia.abrir(imagemBinaria); // Remove pequenos ruídos (abertura)

        // Etapa 2: análise dos componentes conectados
        List<Integer> areas = new ArrayList<>(); // Lista para guardar áreas dos objetos
        List<Integer> perimetros = new ArrayList<>(); // Lista para guardar perímetros
        List<int[]> boundingBoxes = new ArrayList<>(); // Lista para guardar as bounding boxes
        int total = contarComponentes(imagemBinaria, areas, perimetros, boundingBoxes); // Conta quantos objetos existem

        // Etapa 3: conta quantos objetos são considerados quebrados
        int quebrados = contarQuebrados(areas);

        // Etapa 4: classifica os formatos dos comprimidos (cápsula ou redondo)
        int[] formatos = contarFormatos(areas, perimetros, boundingBoxes); // formatos[0] = cápsulas, formatos[1] = redondos

        // Etapa 5: exibe o resultado final na tela
        String resultado = String.format(
            "Total de comprimidos: %d\nComprimidos quebrados: %d\nCápsulas: %d\nRedondos: %d",
            total, quebrados, formatos[0], formatos[1]
        );

        JOptionPane.showMessageDialog(null, resultado, "Resultado da Análise", JOptionPane.INFORMATION_MESSAGE);
    }

    // Método auxiliar que aplica grayscale e binarização na imagem
    private static ImageMatrix binarizar(ImageMatrix imagemOriginal) {
        ImageMatrix emCinza = Filter.aplicarGrayscale(imagemOriginal); // Converte para tons de cinza
        return Filter.aplicarThreshold(emCinza, 128); // Binariza usando limiar 128 (pixels ≥128 viram branco)
    }

    // Conta quantos objetos estão presentes na imagem usando flood fill
    private static int contarComponentes(ImageMatrix imagem, List<Integer> areas, List<Integer> perimetros, List<int[]> boundingBoxes) {
        int[][] matriz = imagem.getPixelMatrix(); // Obtém a matriz de pixels da imagem
        int altura = matriz.length; // Número de linhas
        int largura = matriz[0].length; // Número de colunas

        boolean[][] visitado = new boolean[altura][largura]; // Marca quais pixels já foram processados
        int contador = 0; // Conta quantos componentes válidos foram encontrados

        // Percorre toda a matriz de pixels
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                // Se o pixel for branco (objeto) e ainda não foi visitado, inicia flood fill
                if (isBranco(matriz[y][x]) && !visitado[y][x]) {
                    // Executa flood fill e obtém área, perímetro e limites do objeto
                    int[] resultado = floodFillComPerimetro(matriz, visitado, x, y);
                    int area = resultado[0];
                    int perimetro = resultado[1];
                    int minX = resultado[2], maxX = resultado[3];
                    int minY = resultado[4], maxY = resultado[5];

                    // Considera válido apenas objetos com área ≥ 100 pixels
                    if (area >= 100) {
                        contador++; // Incrementa contador de componentes
                        areas.add(area); // Salva área
                        perimetros.add(perimetro); // Salva perímetro
                        boundingBoxes.add(new int[]{minX, maxX, minY, maxY}); // Salva bounding box
                    }
                }
            }
        }

        return contador; // Retorna o total de componentes encontrados
    }

    // Algoritmo de flood fill com cálculo de área e perímetro do objeto
    private static int[] floodFillComPerimetro(int[][] matriz, boolean[][] visitado, int x, int y) {
        int altura = matriz.length;
        int largura = matriz[0].length;

        Stack<int[]> pilha = new Stack<>(); // Pilha para implementar o flood fill
        pilha.push(new int[]{x, y}); // Começa a partir do ponto (x, y)

        int area = 0; // Conta os pixels da região
        int perimetro = 0; // Conta os pixels da borda
        int minX = x, maxX = x; // Inicializa limites horizontais
        int minY = y, maxY = y; // Inicializa limites verticais

        // Direções de vizinhança (8 conexões)
        int[][] direcoes = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        // Executa flood fill até a pilha esvaziar
        while (!pilha.isEmpty()) {
            int[] atual = pilha.pop();
            int cx = atual[0], cy = atual[1];

            // Ignora se estiver fora dos limites
            if (cx < 0 || cy < 0 || cx >= largura || cy >= altura) continue;
            // Ignora se já foi visitado
            if (visitado[cy][cx]) continue;
            // Ignora se não for branco (objeto)
            if (!isBranco(matriz[cy][cx])) continue;

            visitado[cy][cx] = true; // Marca pixel como visitado
            area++; // Conta área

            // Atualiza limites do retângulo envolvente
            minX = Math.min(minX, cx);
            maxX = Math.max(maxX, cx);
            minY = Math.min(minY, cy);
            maxY = Math.max(maxY, cy);

            boolean isBorda = false; // Flag para saber se é borda

            // Verifica os 8 vizinhos do pixel atual
            for (int[] dir : direcoes) {
                int nx = cx + dir[0], ny = cy + dir[1];

                // Se vizinho for fora da imagem ou não for branco, é borda
                if (nx >= 0 && ny >= 0 && nx < largura && ny < altura) {
                    if (!isBranco(matriz[ny][nx])) {
                        isBorda = true;
                    } else if (!visitado[ny][nx]) {
                        pilha.push(new int[]{nx, ny}); // Adiciona à pilha se for branco e não visitado
                    }
                } else {
                    isBorda = true;
                }
            }

            // Se é borda, incrementa o perímetro
            if (isBorda) perimetro++;
        }

        // Retorna métricas: área, perímetro e limites da bounding box
        return new int[]{area, perimetro, minX, maxX, minY, maxY};
    }

    // Verifica se um pixel é branco (valores RGB altos)
    private static boolean isBranco(int pixel) {
        int r = (pixel >> 16) & 0xFF; // Extrai canal vermelho
        int g = (pixel >> 8) & 0xFF; // Extrai canal verde
        int b = pixel & 0xFF; // Extrai canal azul
        return r > 200 && g > 200 && b > 200; // Considera branco se todos os canais forem > 200
    }

    // Conta quantos comprimidos estão quebrados (área muito pequena)
    private static int contarQuebrados(List<Integer> areas) {
        if (areas.isEmpty()) return 0; // Evita erro em lista vazia
        double media = areas.stream().mapToInt(i -> i).average().orElse(0); // Calcula média das áreas
        return (int) areas.stream().filter(area -> area < media * 0.5).count(); // Conta quantas estão abaixo de 50% da média
    }

    // Classifica os objetos entre cápsulas e redondos
    private static int[] contarFormatos(List<Integer> areas, List<Integer> perimetros, List<int[]> boundingBoxes) {
    int capsulas = 0; // Contador de cápsulas
    int redondos = 0; // Contador de comprimidos redondos

    //  Calcula a média das áreas manualmente (sem stream)
    double soma = 0;
    for (int area : areas) {
        soma += area;
    }
    double mediaArea = areas.size() > 0 ? soma / areas.size() : 0;

    // 🔁 Para cada objeto detectado
    for (int i = 0; i < areas.size(); i++) {
        int area = areas.get(i); // Área do objeto
        int perimetro = perimetros.get(i); // Perímetro do objeto
        int[] box = boundingBoxes.get(i); // Bounding box: [minX, maxX, minY, maxY]

        int largura = box[1] - box[0] + 1; // Largura = maxX - minX + 1
        int altura  = box[3] - box[2] + 1; // Altura  = maxY - minY + 1

    
        // Se altura for maior, o valor será < 1, se largura for maior, será > 1
        double aspectRatio = (double) largura / altura;

        //  Calcula a circularidade usando a fórmula da morfologia matemática:
        // Circularidade = 4π * Área / Perímetro²
        double circularidade = 4 * Math.PI * area / (perimetro * perimetro);

        //  Mostra no terminal os dados do comprimido atual
        System.out.printf("Comprimido %d: Área = %d, Perímetro = %d, Circularidade = %.3f, AspectRatio = %.2f\n",
                i + 1, area, perimetro, circularidade, aspectRatio);

        // Ignora objetos quebrados (área menor que 50% da média)
        if (area < mediaArea * 0.5) {
            continue;
        }

        //  Classificação:
        // Se for bem circular e tiver aspect ratio perto de 1, é redondo
        //if (circularidade >= 0.80 && aspectRatio < 1.25 && aspectRatio > 0.75) {
          if(circularidade >= 0.35){
            redondos++; // Forma circular → redondo
        } else {
            capsulas++; // Forma alongada ou achatada → cápsula
        }
    }

    // 🔚 Retorna o total de cápsulas e redondos detectados
    return new int[]{capsulas, redondos};
}

}
