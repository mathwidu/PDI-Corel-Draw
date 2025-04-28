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


        menuBar.add(new FileMenu(frame, imagePanel).getFileMenu());
        createTransformMenu();
        createFilterMenu();
        createMorphologyMenu();
        createFeatureMenu();
    }

    private void createTransformMenu() {
        TransformacoesGeometricasMenu transformacoesMenu = new TransformacoesGeometricasMenu(imagePanel);
        menuBar.add(transformacoesMenu.getTransformMenu());
    }


    private void createFilterMenu() {
        FilterMenu filterMenu = new FilterMenu(imagePanel);
        menuBar.add(filterMenu.getFilterMenu());
    }

    private void createMorphologyMenu() {
        MorphologyMenu morphologyMenu = new MorphologyMenu(imagePanel);
        menuBar.add(morphologyMenu.getMenu());
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
