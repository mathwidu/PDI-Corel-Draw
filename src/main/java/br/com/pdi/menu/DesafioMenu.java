package br.com.pdi.menu;

import br.com.pdi.analysis.DesafioDetector;
import br.com.pdi.model.ImageMatrix;
import br.com.pdi.view.ImagePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DesafioMenu extends JMenu {

    private final ImagePanel imagePanel;

    public DesafioMenu(ImagePanel imagePanel) {
        super("Desafio");
        this.imagePanel = imagePanel;

        JMenuItem analisarItem = new JMenuItem("Analisar Imagem da Esteira");
        analisarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarAnalise();
            }
        });

        add(analisarItem);
    }

    private void executarAnalise() {
        ImageMatrix imagem = imagePanel.getOriginalImageMatrix();

        if (imagem == null) {
            JOptionPane.showMessageDialog(null, "Nenhuma imagem transformada disponível.");
            return;
        }

        // Chamada para a classe de análise
        DesafioDetector.analisarImagem(imagem);
    }
}
