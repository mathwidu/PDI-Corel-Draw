package br.com.pdi.menu;

import br.com.pdi.view.ImagePanel;
import br.com.pdi.model.ImageMatrix;


import javax.swing.*;

public class TransformacoesGeometricasMenu {
    private JMenu transformMenu;
    private ImagePanel imagePanel;

    public TransformacoesGeometricasMenu(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        transformMenu = new JMenu("Transformações Geométricas");
        criarItensMenu();
    }

    private void criarItensMenu() {
        JMenuItem transladarItem = new JMenuItem("Transladar");
        JMenuItem rotacionarItem = new JMenuItem("Rotacionar");
        JMenuItem espelharItem = new JMenuItem("Espelhar");
        JMenuItem aumentarItem = new JMenuItem("Aumentar");
        JMenuItem diminuirItem = new JMenuItem("Diminuir");

        transladarItem.addActionListener(e -> aplicarTransformacao("translacao"));
        rotacionarItem.addActionListener(e -> aplicarTransformacao("rotacao"));
        espelharItem.addActionListener(e -> aplicarTransformacao("espelhamento"));
        aumentarItem.addActionListener(e -> aplicarTransformacao("escalonamento_aumento"));
        diminuirItem.addActionListener(e -> aplicarTransformacao("escalonamento_diminuicao"));

        transformMenu.add(transladarItem);
        transformMenu.add(rotacionarItem);
        transformMenu.add(espelharItem);
        transformMenu.add(aumentarItem);
        transformMenu.add(diminuirItem);
    }

    private void aplicarTransformacao(String tipo) {
        if (imagePanel.getOriginalImageMatrix() == null) {
            JOptionPane.showMessageDialog(null, "Nenhuma imagem carregada!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ImageMatrix novaImagem = new ImageMatrix(imagePanel.getOriginalImageMatrix().toBufferedImage());
        
        // Aqui aplicamos a transformação na nova imagem (não implementada ainda)
        JOptionPane.showMessageDialog(null, "Aplicando " + tipo + " na imagem.");
        
        imagePanel.setTransformedImageMatrix(novaImagem);
    }

    public JMenu getTransformMenu() {
        return transformMenu;
    }
}
