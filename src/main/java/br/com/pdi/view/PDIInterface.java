package br.com.pdi.view;


import br.com.pdi.menu.MenuBarHandler;

import javax.swing.*;
import java.awt.*;

public class PDIInterface extends JFrame {
    private ImagePanel imagePanel;
    private MenuBarHandler menuBarHandler;

    public PDIInterface() {
        setTitle("Software de Processamento de Imagens - Autor: Matheus Duarte");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        imagePanel = new ImagePanel();
        menuBarHandler = new MenuBarHandler(this, imagePanel);

        setJMenuBar(menuBarHandler.getMenuBar());
        add(imagePanel, BorderLayout.CENTER);

        // Atualiza o layout ao redimensionar
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                imagePanel.repaint();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PDIInterface::new);
    }
}
