package br.com.pdi.menu;

import br.com.pdi.view.ImagePanel;
import br.com.pdi.model.ImageMatrix;
import br.com.pdi.transformacoes.TransformacoesGeometricas;

import javax.swing.*;
import java.awt.*;

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

        transladarItem.addActionListener(e -> abrirDialogTranslacao());
        rotacionarItem.addActionListener(e -> abrirDialogRotacao());
        espelharItem.addActionListener(e -> aplicarTransformacao("espelhamento"));
        aumentarItem.addActionListener(e -> abrirDialogEscalonamento(true));
        diminuirItem.addActionListener(e -> abrirDialogEscalonamento(false));

        transformMenu.add(transladarItem);
        transformMenu.add(rotacionarItem);
        transformMenu.add(espelharItem);
        transformMenu.add(aumentarItem);
        transformMenu.add(diminuirItem);
    }

    private void abrirDialogTranslacao() {
        if (imagePanel.getOriginalImageMatrix() == null) return;

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(imagePanel), "Translação", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(imagePanel);

        JPanel slidersPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        JLabel dxLabel = new JLabel("X: 0");
        JSlider dxSlider = new JSlider(-500, 500, 0);
        JLabel dyLabel = new JLabel("Y: 0");
        JSlider dySlider = new JSlider(-500, 500, 0);

        dxSlider.addChangeListener(e -> {
            dxLabel.setText("X: " + dxSlider.getValue());
            atualizarPreviewTranslacao(dxSlider.getValue(), dySlider.getValue());
        });

        dySlider.addChangeListener(e -> {
            dyLabel.setText("Y: " + dySlider.getValue());
            atualizarPreviewTranslacao(dxSlider.getValue(), dySlider.getValue());
        });

        slidersPanel.add(dxLabel);
        slidersPanel.add(dxSlider);
        slidersPanel.add(dyLabel);
        slidersPanel.add(dySlider);

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

    private void atualizarPreviewTranslacao(int dx, int dy) {
        ImageMatrix imagemOriginal = imagePanel.getOriginalImageMatrix();
        ImageMatrix transformada = TransformacoesGeometricas.transladar(imagemOriginal, dx, dy);
        imagePanel.setTransformedImageMatrix(transformada);
    }

    private void abrirDialogRotacao() {
        if (imagePanel.getOriginalImageMatrix() == null) return;

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(imagePanel), "Rotacionar", true);
        dialog.setSize(400, 150);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(imagePanel);

        JLabel anguloLabel = new JLabel("Ângulo: 0°");
        JSlider anguloSlider = new JSlider(0, 360, 0);

        anguloSlider.addChangeListener(e -> {
            int angulo = anguloSlider.getValue();
            anguloLabel.setText("Ângulo: " + angulo + "°");
            ImageMatrix imagemOriginal = imagePanel.getOriginalImageMatrix();
            ImageMatrix transformada = TransformacoesGeometricas.rotacionar(imagemOriginal, angulo);
            imagePanel.setTransformedImageMatrix(transformada);
        });

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(anguloLabel, BorderLayout.NORTH);
        painel.add(anguloSlider, BorderLayout.CENTER);

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

    private void abrirDialogEscalonamento(boolean aumento) {
        if (imagePanel.getOriginalImageMatrix() == null) return;

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(imagePanel), "Escalonamento", true);
        dialog.setSize(400, 150);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(imagePanel);

        JLabel fatorLabel = new JLabel();
        JSlider fatorSlider;

        if (aumento) {
            fatorLabel.setText("Fator: 1.0");
            fatorSlider = new JSlider(100, 300, 100);
        } else {
            fatorLabel.setText("Fator: 1.0");
            fatorSlider = new JSlider(10, 100, 100);
        }

        fatorSlider.addChangeListener(e -> {
            double fator = fatorSlider.getValue() / 100.0;
            fatorLabel.setText(String.format("Fator: %.2f", fator));
            ImageMatrix imagemOriginal = imagePanel.getOriginalImageMatrix();
            ImageMatrix transformada = TransformacoesGeometricas.escalar(imagemOriginal, fator, fator);
            imagePanel.setTransformedImageMatrix(transformada);
        });

        JPanel painel = new JPanel(new BorderLayout());
        painel.add(fatorLabel, BorderLayout.NORTH);
        painel.add(fatorSlider, BorderLayout.CENTER);

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

    private void aplicarTransformacao(String tipo) {
        if (imagePanel.getOriginalImageMatrix() == null) return;

        ImageMatrix imagemOriginal = imagePanel.getOriginalImageMatrix();
        ImageMatrix imagemTransformada = null;

        switch (tipo) {
            case "espelhamento":
                String[] opcoes = {"Horizontal", "Vertical"};
                int escolha = JOptionPane.showOptionDialog(
                        null,
                        "Escolha o tipo de espelhamento:",
                        "Espelhamento",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opcoes,
                        opcoes[0]
                );
                boolean horizontal = (escolha == 0);
                imagemTransformada = TransformacoesGeometricas.espelhar(imagemOriginal, horizontal);
                break;
        }

        if (imagemTransformada != null) {
            imagePanel.setTransformedImageMatrix(imagemTransformada);
        }
    }

    public JMenu getTransformMenu() {
        return transformMenu;
    }
}
