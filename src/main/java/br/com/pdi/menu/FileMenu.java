package br.com.pdi.menu;

import br.com.pdi.view.PDIInterface;
import br.com.pdi.view.ImagePanel;
import br.com.pdi.model.ImageMatrix;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// Classe que cria o menu "Arquivo" da aplicação
public class FileMenu {
    private JMenu fileMenu; // Menu que será exibido na barra
    private PDIInterface frame; // Referência à janela principal
    private ImagePanel imagePanel; // Painel onde exibimos as imagens

    // Construtor que já cria o menu "Arquivo" com todos os seus itens
    public FileMenu(PDIInterface frame, ImagePanel imagePanel) {
        this.frame = frame;
        this.imagePanel = imagePanel;
        fileMenu = new JMenu("Arquivo");

        createFileMenuItems();
    }

    // Método que cria e configura todos os itens do menu "Arquivo"
    private void createFileMenuItems() {
        JMenuItem openItem = new JMenuItem("Abrir imagem");
        JMenuItem saveItem = new JMenuItem("Salvar imagem");
        JMenuItem aboutItem = new JMenuItem("Sobre");
        JMenuItem exitItem = new JMenuItem("Sair");

        // Define as ações para cada item
        openItem.addActionListener(e -> openImage());
        saveItem.addActionListener(e -> saveImage());
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, 
            "Software de PDI desenvolvido por Matheus Duarte na disciplina de PDI ministrada pela professora Marta Rosecler Bez", 
            "Sobre", JOptionPane.INFORMATION_MESSAGE));
        exitItem.addActionListener(e -> System.exit(0));

        // Adiciona os itens ao menu
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(aboutItem);
        fileMenu.add(exitItem);
    }

    // Método para abrir uma imagem do computador e exibir no painel
    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(file);
                imagePanel.setOriginalImageMatrix(new ImageMatrix(image)); // Carrega a imagem como matriz
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Erro ao abrir imagem!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para salvar a imagem atual (original ou transformada)
    private void saveImage() {
        ImageMatrix imagemParaSalvar = imagePanel.getTransformedImageMatrix();

        // Se não houver transformação, salva a imagem original
        if (imagemParaSalvar == null) {
            imagemParaSalvar = imagePanel.getOriginalImageMatrix();
        }

        // Se ainda não tiver imagem, avisa o usuário
        if (imagemParaSalvar == null) {
            JOptionPane.showMessageDialog(frame, "Nenhuma imagem para salvar!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(frame);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // Salva a imagem no formato PNG
                ImageIO.write(imagemParaSalvar.toBufferedImage(), "png", file);
                JOptionPane.showMessageDialog(frame, "Imagem salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Erro ao salvar imagem!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método que permite acessar o menu "Arquivo" já pronto
    public JMenu getFileMenu() {
        return fileMenu;
    }
}
