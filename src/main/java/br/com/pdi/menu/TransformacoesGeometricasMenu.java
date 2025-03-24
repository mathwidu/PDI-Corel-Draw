package br.com.pdi.menu;

import br.com.pdi.view.ImagePanel;
import br.com.pdi.model.ImageMatrix;
import br.com.pdi.transformacoes.TransformacoesGeometricas;

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

        ImageMatrix imagemOriginal = imagePanel.getOriginalImageMatrix();
        ImageMatrix imagemTransformada = null;

        try {
            switch (tipo) {
                case "translacao":
                    int dx = Integer.parseInt(JOptionPane.showInputDialog("Deslocamento em X:"));
                    int dy = Integer.parseInt(JOptionPane.showInputDialog("Deslocamento em Y:"));
                    imagemTransformada = TransformacoesGeometricas.transladar(imagemOriginal, dx, dy);
                    break;

                case "rotacao":
                    double angulo = Double.parseDouble(JOptionPane.showInputDialog("Ângulo de rotação (graus):"));
                    imagemTransformada = TransformacoesGeometricas.rotacionar(imagemOriginal, angulo);
                    break;

                // Os outros casos ainda serão implementados
                default:
                    JOptionPane.showMessageDialog(null, "Transformação não reconhecida.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            if (imagemTransformada != null) {
                imagePanel.setTransformedImageMatrix(imagemTransformada);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao aplicar transformação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JMenu getTransformMenu() {
        return transformMenu;
    }
}
