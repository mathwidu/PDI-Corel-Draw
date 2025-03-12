import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MenuBarHandler {
    private JMenuBar menuBar;
    private PDIInterface frame;
    private ImagePanel imagePanel;

    public MenuBarHandler(PDIInterface frame, ImagePanel imagePanel) {
        this.frame = frame;
        this.imagePanel = imagePanel;
        menuBar = new JMenuBar();

        createFileMenu();
        createTransformMenu();
        createFilterMenu();
        createMorphologyMenu();
        createFeatureMenu();
    }

    private void createFileMenu() {
        JMenu fileMenu = new JMenu("Arquivo");
        JMenuItem openItem = new JMenuItem("Abrir imagem");
        JMenuItem saveItem = new JMenuItem("Salvar imagem");
        JMenuItem aboutItem = new JMenuItem("Sobre");
        JMenuItem exitItem = new JMenuItem("Sair");

        openItem.addActionListener(e -> openImage());
        saveItem.addActionListener(e -> saveImage());
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Software de PDI desenvolvido por Matheus Duarte na disciplina de PDI ministrada pela professora Marta Rosecler Bez", "Sobre", JOptionPane.INFORMATION_MESSAGE));
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(aboutItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
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

    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(file);
                imagePanel.setOriginalImage(image);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Erro ao abrir imagem!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveImage() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                ImageIO.write(imagePanel.getOriginalImage(), "png", file);
                JOptionPane.showMessageDialog(frame, "Imagem salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Erro ao salvar imagem!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
