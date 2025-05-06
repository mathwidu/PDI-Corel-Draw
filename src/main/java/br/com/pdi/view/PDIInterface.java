package br.com.pdi.view;

import br.com.pdi.menu.MenuBarHandler;

import javax.swing.*;
import java.awt.*;

// Classe principal da interface gráfica do nosso app de PDI
public class PDIInterface extends JFrame {
    private ImagePanel imagePanel; // Painel onde as imagens serão exibidas
    private MenuBarHandler menuBarHandler; // Responsável por gerenciar a barra de menus

    // Construtor da nossa interface
    public PDIInterface() {
        // Define o título da janela
        setTitle("Software de Processamento de Imagens - Autor: Matheus Duarte");

        // Garante que o programa será encerrado ao fechar a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Define o tamanho inicial da janela (largura x altura)
        setSize(1280, 720);

        // Faz a janela abrir centralizada na tela
        setLocationRelativeTo(null);

        // Cria o painel de imagens
        imagePanel = new ImagePanel();

        // Cria o menu e associa com esta janela e o painel de imagens
        menuBarHandler = new MenuBarHandler(this, imagePanel);

        // Seta a barra de menu na janela
        setJMenuBar(menuBarHandler.getMenuBar());

        // Adiciona o painel de imagens no centro da janela
        add(imagePanel, BorderLayout.CENTER);

        // Listener que atualiza o tamanho da imagem na tela quando a janela for redimensionada
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                imagePanel.atualizarExibicao(); 
            }
        });

        // Deixa a janela visível para o usuário
        setVisible(true);
    }

    // Método principal que inicia a execução do app
    public static void main(String[] args) {
        // Garante que a criação da interface ocorra na thread correta do Swing
        SwingUtilities.invokeLater(PDIInterface::new);
    }
}
