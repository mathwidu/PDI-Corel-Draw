package br.com.pdi.menu;

import br.com.pdi.model.ImageMatrix;
import br.com.pdi.morfologia.Morfologia;
import br.com.pdi.view.ImagePanel;
import br.com.pdi.filters.Filter;

import javax.swing.*;

// Classe que cria o menu "Morfologia Matemática" da aplicação
public class MorphologyMenu {
    private JMenu menu; // Menu que será exibido na barra
    private ImagePanel imagePanel; // Painel que exibe as imagens manipuladas

    // Construtor que recebe o painel e inicializa o menu
    public MorphologyMenu(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        menu = new JMenu("Morfologia Matemática");
        criarItens(); // Cria os botões do menu
    }

    // Cria os itens do menu e define o que cada um faz
    private void criarItens() {
        // Botão de Dilatação
        JMenuItem dilatacaoItem = new JMenuItem("Dilatação");
        dilatacaoItem.addActionListener(e -> {
            // Só aplica se houver imagem carregada
            if (imagePanel.getOriginalImageMatrix() != null) {
                // Converte para escala de cinza antes de aplicar
                ImageMatrix imagemCinza = Filter.aplicarGrayscale(imagePanel.getOriginalImageMatrix());
                // Aplica a dilatação
                ImageMatrix resultado = Morfologia.dilatar(imagemCinza);
                // Atualiza o painel com o resultado
                imagePanel.setTransformedImageMatrix(resultado);
            }
        });

        // Botão de Erosão
        JMenuItem erosaoItem = new JMenuItem("Erosão");
        erosaoItem.addActionListener(e -> {
            if (imagePanel.getOriginalImageMatrix() != null) {
                ImageMatrix imagemCinza = Filter.aplicarGrayscale(imagePanel.getOriginalImageMatrix());
                ImageMatrix resultado = Morfologia.erodir(imagemCinza);
                imagePanel.setTransformedImageMatrix(resultado);
            }
        });

        // Botão de Abertura
        JMenuItem aberturaItem = new JMenuItem("Abertura");
        aberturaItem.addActionListener(e -> {
            if (imagePanel.getOriginalImageMatrix() != null) {
                ImageMatrix imagemCinza = Filter.aplicarGrayscale(imagePanel.getOriginalImageMatrix());
                ImageMatrix resultado = Morfologia.abrir(imagemCinza);
                imagePanel.setTransformedImageMatrix(resultado);
            }
        });

        // Botão de Fechamento
        JMenuItem fechamentoItem = new JMenuItem("Fechamento");
        fechamentoItem.addActionListener(e -> {
            if (imagePanel.getOriginalImageMatrix() != null) {
                ImageMatrix imagemCinza = Filter.aplicarGrayscale(imagePanel.getOriginalImageMatrix());
                ImageMatrix resultado = Morfologia.fechar(imagemCinza);
                imagePanel.setTransformedImageMatrix(resultado);
            }
        });

        // Adiciona todos os itens ao menu
        menu.add(dilatacaoItem);
        menu.add(erosaoItem);
        menu.add(aberturaItem);
        menu.add(fechamentoItem);
    }

    // Retorna o menu pronto para ser adicionado à barra de menus
    public JMenu getMenu() {
        return menu;
    }
}