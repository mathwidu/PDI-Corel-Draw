package br.com.pdi.view;

import br.com.pdi.model.ImageMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;

// Painel que exibe a imagem original e a imagem transformada
public class ImagePanel extends JPanel {
    private JLabel originalImageLabel, transformedImageLabel; // Labels para mostrar as imagens
    private ImageMatrix originalImageMatrix, transformedImageMatrix; // Matriz da imagem original e da imagem alterada
    private JButton confirmarEdicaoButton, desfazerEdicaoButton; // Botões para confirmar ou desfazer edições

    private Stack<ImageMatrix> historicoEdicoes; // Pilha para armazenar o histórico de edições

    // Construtor do painel de imagens
    public ImagePanel() {
        // Cria os labels para exibir as imagens
        originalImageLabel = new JLabel("Imagem Original", SwingConstants.CENTER);
        transformedImageLabel = new JLabel("Imagem Transformada", SwingConstants.CENTER);

        // Adiciona borda preta ao redor dos labels
        originalImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        transformedImageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Cria o botão para confirmar edições (começa invisível)
        confirmarEdicaoButton = new JButton("Confirmar Edição");
        confirmarEdicaoButton.setVisible(false);
        confirmarEdicaoButton.addActionListener(e -> confirmarEdicao());

        // Cria o botão para desfazer edições (começa invisível)
        desfazerEdicaoButton = new JButton("Desfazer");
        desfazerEdicaoButton.setVisible(false);
        desfazerEdicaoButton.addActionListener(e -> desfazerEdicao());

        // Inicializa a pilha de histórico
        historicoEdicoes = new Stack<>();

        // Define o layout do painel principal
        setLayout(new BorderLayout());

        // Painel interno que coloca a imagem original e transformada lado a lado
        JPanel imagensPanel = new JPanel(new GridLayout(1, 2));
        imagensPanel.add(originalImageLabel);
        imagensPanel.add(transformedImageLabel);
        add(imagensPanel, BorderLayout.CENTER);

        // Painel interno para os botões
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botoesPanel.add(confirmarEdicaoButton);
        botoesPanel.add(desfazerEdicaoButton);
        add(botoesPanel, BorderLayout.SOUTH);
    }

    // Define a imagem original e reseta o histórico de edições
    public void setOriginalImageMatrix(ImageMatrix imageMatrix) {
        this.originalImageMatrix = imageMatrix;
        this.transformedImageMatrix = null;
        historicoEdicoes.clear(); 
        atualizarExibicao(); // Atualiza o que está sendo exibido
    }

    // Define uma imagem transformada
    public void setTransformedImageMatrix(ImageMatrix imageMatrix) {
        this.transformedImageMatrix = imageMatrix;
        atualizarExibicao();
    }

    // Retorna a imagem original
    public ImageMatrix getOriginalImageMatrix() {
        return originalImageMatrix;
    }

    // Retorna a imagem transformada
    public ImageMatrix getTransformedImageMatrix() {
        return transformedImageMatrix;
    }

    // Método sobrescrito para pintar o componente (não fazemos nada especial aqui)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    // Atualiza a exibição das imagens no painel
    public void atualizarExibicao() {
        int panelWidth = getWidth() / 2;
        int panelHeight = getHeight();

        // Evita erro se ainda não tiver dimensão
        if (panelWidth == 0 || panelHeight == 0) return;

        // Exibe a imagem original
        if (originalImageMatrix != null) {
            originalImageLabel.setIcon(new ImageIcon(escalarImagem(originalImageMatrix.toBufferedImage(), panelWidth, panelHeight)));
            originalImageLabel.setText(null); // Remove o texto
        }

        // Exibe a imagem transformada
        if (transformedImageMatrix != null) {
            transformedImageLabel.setIcon(new ImageIcon(escalarImagem(transformedImageMatrix.toBufferedImage(), panelWidth, panelHeight)));
            transformedImageLabel.setText(null);
            confirmarEdicaoButton.setVisible(true); // Mostra botão de confirmar
        } else {
            transformedImageLabel.setIcon(null);
            transformedImageLabel.setText("Imagem Transformada");
            confirmarEdicaoButton.setVisible(false); // Esconde botão se não tem imagem transformada
        }

        // Mostra ou esconde botão de desfazer baseado no histórico
        desfazerEdicaoButton.setVisible(!historicoEdicoes.isEmpty());
    }

    // Método para escalar uma imagem para caber no painel
    private Image escalarImagem(BufferedImage image, int maxWidth, int maxHeight) {
        double aspectRatio = (double) image.getWidth() / image.getHeight();
        int newWidth, newHeight;

        if (maxWidth / aspectRatio <= maxHeight) {
            newWidth = maxWidth;
            newHeight = (int) (maxWidth / aspectRatio);
        } else {
            newWidth = (int) (maxHeight * aspectRatio);
            newHeight = maxHeight;
        }

        // Redimensiona a imagem suavemente
        return image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }

    // Confirma a edição feita: salva a imagem transformada como nova original
    private void confirmarEdicao() {
        if (transformedImageMatrix != null) {
            historicoEdicoes.push(originalImageMatrix); // Salva a imagem anterior no histórico
            originalImageMatrix = transformedImageMatrix; // Atualiza a imagem original
            transformedImageMatrix = null; // Limpa a imagem transformada
            atualizarExibicao();
            JOptionPane.showMessageDialog(this, "Edição confirmada. Agora você pode aplicar novos filtros.");
        }
    }

    // Desfaz a última edição feita
    private void desfazerEdicao() {
        if (!historicoEdicoes.isEmpty()) {
            originalImageMatrix = historicoEdicoes.pop(); // Recupera a última imagem salva
            transformedImageMatrix = null;
            JOptionPane.showMessageDialog(this, "Última edição desfeita.");
            atualizarExibicao();
        }
    }
}
