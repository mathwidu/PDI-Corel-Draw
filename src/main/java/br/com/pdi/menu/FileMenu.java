package br.com.pdi.menu;


import br.com.pdi.view.PDIInterface;
import br.com.pdi.view.ImagePanel;
import br.com.pdi.model.ImageMatrix;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FileMenu {
    private JMenu fileMenu;
    private PDIInterface frame;
    private ImagePanel imagePanel;

    public FileMenu(PDIInterface frame, ImagePanel imagePanel) {
        this.frame = frame;
        this.imagePanel = imagePanel;
        fileMenu = new JMenu("Arquivo");

        createFileMenuItems();
    }

    private void createFileMenuItems() {
        JMenuItem openItem = new JMenuItem("Abrir imagem");
        JMenuItem saveItem = new JMenuItem("Salvar imagem");
        JMenuItem aboutItem = new JMenuItem("Sobre");
        JMenuItem exitItem = new JMenuItem("Sair");

        openItem.addActionListener(e -> openImage());
        saveItem.addActionListener(e -> saveImage());
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, 
            "Software de PDI desenvolvido por Matheus Duarte na disciplina de PDI ministrada pela professora Marta Rosecler Bez", 
            "Sobre", JOptionPane.INFORMATION_MESSAGE));
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(aboutItem);
        fileMenu.add(exitItem);
    }

    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(file);
                imagePanel.setOriginalImageMatrix(new ImageMatrix(image)); // Agora usamos a original
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Erro ao abrir imagem!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveImage() {
        ImageMatrix imagemParaSalvar = imagePanel.getTransformedImageMatrix();
        
        if (imagemParaSalvar == null) {
            imagemParaSalvar = imagePanel.getOriginalImageMatrix();
        }
    
        if (imagemParaSalvar == null) {
            JOptionPane.showMessageDialog(frame, "Nenhuma imagem para salvar!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                ImageIO.write(imagemParaSalvar.toBufferedImage(), "png", file);
                JOptionPane.showMessageDialog(frame, "Imagem salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Erro ao salvar imagem!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    public JMenu getFileMenu() {
        return fileMenu;
    }
}
