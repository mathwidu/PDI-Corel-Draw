package br.com.pdi.menu;

import br.com.pdi.view.ImagePanel;
import br.com.pdi.model.ImageMatrix;
import br.com.pdi.filters.Filter;

import javax.swing.*;
import java.awt.*;

public class FilterMenu {
    private JMenu filtrosMenu;
    private ImagePanel imagePanel;

    public FilterMenu(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        filtrosMenu = new JMenu("Filtros");
        criarItensMenu();
    }

    private void criarItensMenu() {
        JMenuItem grayscaleItem = new JMenuItem("Grayscale");
        JMenuItem passaBaixaItem = new JMenuItem("Passa Baixa");
        JMenuItem passaAltaItem = new JMenuItem("Passa Alta");
        JMenuItem thresholdItem = new JMenuItem("Threshold");
        JMenuItem brilhoContrasteItem = new JMenuItem("Brilho e Contraste");

        grayscaleItem.addActionListener(e -> aplicarFiltroSimples("grayscale"));
        passaBaixaItem.addActionListener(e -> aplicarPassaBaixaInterativo());
        passaAltaItem.addActionListener(e -> aplicarPassaAltaInterativo());
        thresholdItem.addActionListener(e -> aplicarThresholdInterativo());
        brilhoContrasteItem.addActionListener(e -> aplicarBrilhoContrasteInterativo());

        filtrosMenu.add(grayscaleItem);
        filtrosMenu.add(passaBaixaItem);
        filtrosMenu.add(passaAltaItem);
        filtrosMenu.add(thresholdItem);
        filtrosMenu.add(brilhoContrasteItem);
    }

    private void aplicarFiltroSimples(String tipo) {
        if (imagePanel.getOriginalImageMatrix() == null) return;

        ImageMatrix imagemOriginal = imagePanel.getOriginalImageMatrix();
        ImageMatrix imagemFiltrada = null;

        if ("grayscale".equals(tipo)) {
            imagemFiltrada = Filter.aplicarGrayscale(imagemOriginal);
        }

        if (imagemFiltrada != null) {
            imagePanel.setTransformedImageMatrix(imagemFiltrada);
        }
    }

    private void aplicarBrilhoContrasteInterativo() {
        if (imagePanel.getOriginalImageMatrix() == null) return;

        ImageMatrix imagemOriginal = imagePanel.getOriginalImageMatrix();

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(imagePanel), "Brilho e Contraste", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(imagePanel);

        JPanel slidersPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JLabel contrasteLabel = new JLabel("Contraste: 1.0");
        JSlider contrasteSlider = new JSlider(10, 300, 100);
        JLabel brilhoLabel = new JLabel("Brilho: 0");
        JSlider brilhoSlider = new JSlider(-255, 255, 0);

        contrasteSlider.addChangeListener(e -> {
            double contraste = contrasteSlider.getValue() / 100.0;
            int brilho = brilhoSlider.getValue();
            contrasteLabel.setText(String.format("Contraste: %.2f", contraste));
            imagePanel.setTransformedImageMatrix(Filter.ajustarBrilhoContraste(imagemOriginal, contraste, brilho));
        });

        brilhoSlider.addChangeListener(e -> {
            int brilho = brilhoSlider.getValue();
            double contraste = contrasteSlider.getValue() / 100.0;
            brilhoLabel.setText("Brilho: " + brilho);
            imagePanel.setTransformedImageMatrix(Filter.ajustarBrilhoContraste(imagemOriginal, contraste, brilho));
        });

        slidersPanel.add(contrasteLabel);
        slidersPanel.add(contrasteSlider);
        slidersPanel.add(brilhoLabel);
        slidersPanel.add(brilhoSlider);

        JPanel botoes = new JPanel();
        JButton aplicar = new JButton("Aplicar");
        JButton cancelar = new JButton("Cancelar");

        aplicar.addActionListener(ev -> dialog.dispose());
        cancelar.addActionListener(ev -> {
            imagePanel.setTransformedImageMatrix(null);
            dialog.dispose();
        });

        botoes.add(aplicar);
        botoes.add(cancelar);
        dialog.add(slidersPanel, BorderLayout.CENTER);
        dialog.add(botoes, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void aplicarPassaBaixaInterativo() {
        if (imagePanel.getOriginalImageMatrix() == null) return;

        ImageMatrix imagemOriginal = imagePanel.getOriginalImageMatrix();

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(imagePanel), "Filtro Passa Baixa", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 130);
        dialog.setLocationRelativeTo(imagePanel);

        JLabel label = new JLabel("Kernel: 3x3");
        JSlider slider = new JSlider(1, 3, 1);

        int valorInicial = slider.getValue();
        int kernelSizeInicial = valorInicial * 2 + 1;
        imagePanel.setTransformedImageMatrix(Filter.aplicarPassaBaixaComKernel(imagemOriginal, kernelSizeInicial));

        slider.addChangeListener(e -> {
            int valor = slider.getValue();
            int kernelSize = valor * 2 + 1;
            label.setText("Kernel: " + kernelSize + "x" + kernelSize);
            imagePanel.setTransformedImageMatrix(Filter.aplicarPassaBaixaComKernel(imagemOriginal, kernelSize));
        });

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(label, BorderLayout.NORTH);
        painel.add(slider, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        JButton aplicar = new JButton("Aplicar");
        JButton cancelar = new JButton("Cancelar");

        aplicar.addActionListener(ev -> dialog.dispose());
        cancelar.addActionListener(ev -> {
            imagePanel.setTransformedImageMatrix(null);
            dialog.dispose();
        });

        botoes.add(aplicar);
        botoes.add(cancelar);
        dialog.add(painel, BorderLayout.CENTER);
        dialog.add(botoes, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void aplicarPassaAltaInterativo() {
        if (imagePanel.getOriginalImageMatrix() == null) return;

        ImageMatrix imagemCinza = Filter.aplicarGrayscale(imagePanel.getOriginalImageMatrix());

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(imagePanel), "Filtro Passa Alta", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 130);
        dialog.setLocationRelativeTo(imagePanel);

        JLabel label = new JLabel("Threshold: 128");
        JSlider slider = new JSlider(0, 255, 128);

        int thresholdInicial = slider.getValue();
        imagePanel.setTransformedImageMatrix(Filter.aplicarPassaAltaComThreshold(imagemCinza, thresholdInicial));

        slider.addChangeListener(e -> {
            int threshold = slider.getValue();
            label.setText("Threshold: " + threshold);
            imagePanel.setTransformedImageMatrix(Filter.aplicarPassaAltaComThreshold(imagemCinza, threshold));
        });

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(label, BorderLayout.NORTH);
        painel.add(slider, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        JButton aplicar = new JButton("Aplicar");
        JButton cancelar = new JButton("Cancelar");

        aplicar.addActionListener(ev -> dialog.dispose());
        cancelar.addActionListener(ev -> {
            imagePanel.setTransformedImageMatrix(null);
            dialog.dispose();
        });

        botoes.add(aplicar);
        botoes.add(cancelar);
        dialog.add(painel, BorderLayout.CENTER);
        dialog.add(botoes, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void aplicarThresholdInterativo() {
        if (imagePanel.getOriginalImageMatrix() == null) return;

        ImageMatrix imagemCinza = Filter.aplicarGrayscale(imagePanel.getOriginalImageMatrix());

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(imagePanel), "Limiarização", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 130);
        dialog.setLocationRelativeTo(imagePanel);

        JLabel label = new JLabel("Limiar: 128");
        JSlider slider = new JSlider(0, 255, 128);

        int limiarInicial = slider.getValue();
        imagePanel.setTransformedImageMatrix(Filter.aplicarThreshold(imagemCinza, limiarInicial));

        slider.addChangeListener(e -> {
            int limiar = slider.getValue();
            label.setText("Limiar: " + limiar);
            imagePanel.setTransformedImageMatrix(Filter.aplicarThreshold(imagemCinza, limiar));
        });

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(label, BorderLayout.NORTH);
        painel.add(slider, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        JButton aplicar = new JButton("Aplicar");
        JButton cancelar = new JButton("Cancelar");

        aplicar.addActionListener(ev -> dialog.dispose());
        cancelar.addActionListener(ev -> {
            imagePanel.setTransformedImageMatrix(null);
            dialog.dispose();
        });

        botoes.add(aplicar);
        botoes.add(cancelar);
        dialog.add(painel, BorderLayout.CENTER);
        dialog.add(botoes, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    public JMenu getFilterMenu() {
        return filtrosMenu;
    }
}