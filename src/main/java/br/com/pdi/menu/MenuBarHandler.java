package br.com.pdi.menu;

import br.com.pdi.view.PDIInterface;
import br.com.pdi.view.ImagePanel;



import javax.swing.*;

public class MenuBarHandler {
    private JMenuBar menuBar;
    private PDIInterface frame;
    private ImagePanel imagePanel;

    public MenuBarHandler(PDIInterface frame, ImagePanel imagePanel) {
        this.frame = frame;
        this.imagePanel = imagePanel;
        menuBar = new JMenuBar();

        // Adicionando menus
        menuBar.add(new FileMenu(frame, imagePanel).getFileMenu());
        createTransformMenu();
        createFilterMenu();
        createMorphologyMenu();
        createFeatureMenu();
    }

    private void createTransformMenu() {
        JMenu transformMenu = new JMenu("Transformações Geométricas");
        transformMenu.add(new JMenuItem("Transladar"));
        transformMenu.add(new JMenuItem("Rotacionar"));
        transformMenu.add(new JMenuItem("Espelhar"));
        transformMenu.add(new JMenuItem("Aumentar"));
        transformMenu.add(new JMenuItem("Diminuir"));
        menuBar.add(transformMenu);
    }

    private void createFilterMenu() {
        JMenu filterMenu = new JMenu("Filtros");
        filterMenu.add(new JMenuItem("Grayscale"));
        filterMenu.add(new JMenuItem("Passa Baixa"));
        filterMenu.add(new JMenuItem("Passa Alta"));
        filterMenu.add(new JMenuItem("Threshold"));
        menuBar.add(filterMenu);
    }

    private void createMorphologyMenu() {
        JMenu morphologyMenu = new JMenu("Morfologia Matemática");
        morphologyMenu.add(new JMenuItem("Dilatação"));
        morphologyMenu.add(new JMenuItem("Erosão"));
        morphologyMenu.add(new JMenuItem("Abertura"));
        morphologyMenu.add(new JMenuItem("Fechamento"));
        menuBar.add(morphologyMenu);
    }

    private void createFeatureMenu() {
        JMenu featureMenu = new JMenu("Extração de Características");
        featureMenu.add(new JMenuItem("Desafio"));
        menuBar.add(featureMenu);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
