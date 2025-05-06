package br.com.pdi.menu;

import br.com.pdi.view.PDIInterface;
import br.com.pdi.view.ImagePanel;

import javax.swing.*;

// Classe que monta e organiza toda a barra de menu da nossa aplicação
public class MenuBarHandler {
    private JMenuBar menuBar; // Barra de menu principal
    private PDIInterface frame; // Janela principal do programa
    private ImagePanel imagePanel; // Painel de imagens que será manipulado

    // Construtor que já recebe a janela e o painel de imagens para conectar tudo
    public MenuBarHandler(PDIInterface frame, ImagePanel imagePanel) {
        this.frame = frame;
        this.imagePanel = imagePanel;
        menuBar = new JMenuBar();

        // Adiciona o menu de Arquivo (abrir, salvar, sair)
        menuBar.add(new FileMenu(frame, imagePanel).getFileMenu());

        // Cria e adiciona os menus restantes
        createTransformMenu();
        createFilterMenu();
        createMorphologyMenu();
        createFeatureMenu();
    }

    // Cria o menu de Transformações Geométricas (transladar, rotacionar, espelhar, etc)
    private void createTransformMenu() {
        TransformacoesGeometricasMenu transformacoesMenu = new TransformacoesGeometricasMenu(imagePanel);
        menuBar.add(transformacoesMenu.getTransformMenu());
    }

    // Cria o menu de Filtros (grayscale, passa baixa, passa alta, brilho/contraste)
    private void createFilterMenu() {
        FilterMenu filterMenu = new FilterMenu(imagePanel);
        menuBar.add(filterMenu.getFilterMenu());
    }

    // Cria o menu de Morfologia Matemática (dilatação, erosão, abertura, fechamento)
    private void createMorphologyMenu() {
        MorphologyMenu morphologyMenu = new MorphologyMenu(imagePanel);
        menuBar.add(morphologyMenu.getMenu());
    }

    // Cria o menu de Extração de Características (placeholder para futuros desafios)
    private void createFeatureMenu() {
        JMenu featureMenu = new JMenu("Extração de Características");
        featureMenu.add(new JMenuItem("Desafio")); // Ainda não implementado, apenas visual
        menuBar.add(featureMenu);
    }

    // Retorna a barra de menus montada
    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
