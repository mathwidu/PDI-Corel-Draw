package br.com.pdi.menu;

import br.com.pdi.view.ImagePanel;
import br.com.pdi.model.ImageMatrix;
import br.com.pdi.transformacoes.TransformacoesGeometricas;

import javax.swing.*;
import java.awt.*;

// Classe que cria o menu "Transformações Geométricas" da aplicação
public class TransformacoesGeometricasMenu {
    private JMenu transformMenu; // Menu que será exibido na barra
    private ImagePanel imagePanel; // Painel onde as imagens são exibidas

    // Construtor que recebe o painel de imagens e já cria o menu
    public TransformacoesGeometricasMenu(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        transformMenu = new JMenu("Transformações Geométricas");
        criarItensMenu();
    }

    // Método que cria e adiciona os itens ao menu
    private void criarItensMenu() {
        // Criamos os itens de menu para cada transformação
        JMenuItem transladarItem = new JMenuItem("Transladar");
        JMenuItem rotacionarItem = new JMenuItem("Rotacionar");
        JMenuItem espelharItem = new JMenuItem("Espelhar");
        JMenuItem aumentarItem = new JMenuItem("Aumentar");
        JMenuItem diminuirItem = new JMenuItem("Diminuir");

        // Associa cada item a sua ação específica
        transladarItem.addActionListener(e -> abrirDialogTranslacao());
        rotacionarItem.addActionListener(e -> abrirDialogRotacao());
        espelharItem.addActionListener(e -> aplicarTransformacao("espelhamento"));
        aumentarItem.addActionListener(e -> abrirDialogEscalonamento(true));
        diminuirItem.addActionListener(e -> abrirDialogEscalonamento(false));

        // Adiciona todos os itens ao menu
        transformMenu.add(transladarItem);
        transformMenu.add(rotacionarItem);
        transformMenu.add(espelharItem);
        transformMenu.add(aumentarItem);
        transformMenu.add(diminuirItem);
    }

    // Método para abrir o diálogo de translação da imagem
    private void abrirDialogTranslacao() {
        if (imagePanel.getOriginalImageMatrix() == null) return;

        // Cria a janela de translação
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(imagePanel), "Translação", true);
        dialog.setSize(400, 200);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(imagePanel);

        // Painel para os sliders de X e Y
        JPanel slidersPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        // Cria slider e label para deslocamento em X
        JLabel dxLabel = new JLabel("X: 0");
        JSlider dxSlider = new JSlider(-500, 500, 0);

        // Cria slider e label para deslocamento em Y
        JLabel dyLabel = new JLabel("Y: 0");
        JSlider dySlider = new JSlider(-500, 500, 0);

        // Atualiza imagem enquanto deslizamos o slider de X
        dxSlider.addChangeListener(e -> {
            dxLabel.setText("X: " + dxSlider.getValue());
            atualizarPreviewTranslacao(dxSlider.getValue(), dySlider.getValue());
        });

        // Atualiza imagem enquanto deslizamos o slider de Y
        dySlider.addChangeListener(e -> {
            dyLabel.setText("Y: " + dySlider.getValue());
            atualizarPreviewTranslacao(dxSlider.getValue(), dySlider.getValue());
        });

        // Adiciona sliders no painel
        slidersPanel.add(dxLabel);
        slidersPanel.add(dxSlider);
        slidersPanel.add(dyLabel);
        slidersPanel.add(dySlider);

        // Botões para aplicar ou cancelar
        JPanel botoes = new JPanel();
        JButton aplicar = new JButton("Aplicar");
        JButton cancelar = new JButton("Cancelar");

        aplicar.addActionListener(ev -> dialog.dispose());
        cancelar.addActionListener(ev -> {
            imagePanel.setTransformedImageMatrix(null); // Cancela preview
            dialog.dispose();
        });

        botoes.add(aplicar);
        botoes.add(cancelar);

        // Monta a janela
        dialog.add(slidersPanel, BorderLayout.CENTER);
        dialog.add(botoes, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Atualiza o preview da imagem baseada nos valores de translação
    private void atualizarPreviewTranslacao(int dx, int dy) {
        ImageMatrix imagemOriginal = imagePanel.getOriginalImageMatrix();
        ImageMatrix transformada = TransformacoesGeometricas.transladar(imagemOriginal, dx, dy);
        imagePanel.setTransformedImageMatrix(transformada);
    }

    // Método para abrir o diálogo de rotação da imagem
    private void abrirDialogRotacao() {
        if (imagePanel.getOriginalImageMatrix() == null) return;

        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(imagePanel), "Rotacionar", true);
        dialog.setSize(400, 150);
        dialog.setLayout(new BorderLayout());
        dialog.setLocationRelativeTo(imagePanel);

        // Cria o label e o slider para o ângulo
        JLabel anguloLabel = new JLabel("Ângulo: 0°");
        JSlider anguloSlider = new JSlider(0, 360, 0);

        // Atualiza o preview conforme deslizamos o slider
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

        // Botões aplicar e cancelar
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

    // Método que abre o diálogo para definir fator de escala
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

        // Atualiza a imagem conforme mudamos o fator
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

        // Botões de aplicar e cancelar
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

    // Método que aplica uma transformação direta (no caso, espelhamento)
    private void aplicarTransformacao(String tipo) {
        if (imagePanel.getOriginalImageMatrix() == null) return;

        ImageMatrix imagemOriginal = imagePanel.getOriginalImageMatrix();
        ImageMatrix imagemTransformada = null;

        switch (tipo) {
            case "espelhamento":
                // Pergunta ao usuário se quer espelhar horizontal ou verticalmente
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

    // Retorna o menu pronto para ser adicionado à barra de menus
    public JMenu getTransformMenu() {
        return transformMenu;
    }
}
