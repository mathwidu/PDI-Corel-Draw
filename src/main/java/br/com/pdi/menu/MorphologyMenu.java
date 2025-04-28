package br.com.pdi.menu;

import br.com.pdi.model.ImageMatrix;
import br.com.pdi.morfologia.Morfologia;
import br.com.pdi.view.ImagePanel;
import br.com.pdi.filters.Filter;



import javax.swing.*;

public class MorphologyMenu {
    private JMenu menu;
    private ImagePanel imagePanel;

    public MorphologyMenu(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        menu = new JMenu("Morfologia Matemática");
        criarItens();
    }

    private void criarItens() {
        JMenuItem dilatacaoItem = new JMenuItem("Dilatação");
        dilatacaoItem.addActionListener(e -> {
            if (imagePanel.getOriginalImageMatrix() != null) {
                ImageMatrix imagemCinza = br.com.pdi.filters.Filter.aplicarGrayscale(imagePanel.getOriginalImageMatrix());
                ImageMatrix resultado = br.com.pdi.morfologia.Morfologia.dilatar(imagemCinza);
                imagePanel.setTransformedImageMatrix(resultado);
            }
        });
    
        JMenuItem erosaoItem = new JMenuItem("Erosão");
        erosaoItem.addActionListener(e -> {
            if (imagePanel.getOriginalImageMatrix() != null) {
                ImageMatrix imagemCinza = br.com.pdi.filters.Filter.aplicarGrayscale(imagePanel.getOriginalImageMatrix());
                ImageMatrix resultado = br.com.pdi.morfologia.Morfologia.erodir(imagemCinza);
                imagePanel.setTransformedImageMatrix(resultado);
            }
        });
    
        JMenuItem aberturaItem = new JMenuItem("Abertura");
        aberturaItem.addActionListener(e -> {
            if (imagePanel.getOriginalImageMatrix() != null) {
                ImageMatrix imagemCinza = br.com.pdi.filters.Filter.aplicarGrayscale(imagePanel.getOriginalImageMatrix());
                ImageMatrix resultado = br.com.pdi.morfologia.Morfologia.abrir(imagemCinza);
                imagePanel.setTransformedImageMatrix(resultado);
            }
        });

        JMenuItem fechamentoItem = new JMenuItem("Fechamento");
        fechamentoItem.addActionListener(e -> {
            if (imagePanel.getOriginalImageMatrix() != null) {
                ImageMatrix imagemCinza = br.com.pdi.filters.Filter.aplicarGrayscale(imagePanel.getOriginalImageMatrix());
                ImageMatrix resultado = br.com.pdi.morfologia.Morfologia.fechar(imagemCinza);
                imagePanel.setTransformedImageMatrix(resultado);
            }
        });
        menu.add(dilatacaoItem);
        menu.add(erosaoItem);
        menu.add(aberturaItem);
        menu.add(fechamentoItem);
    }
    

    public JMenu getMenu() {
        return menu;
    }
}
