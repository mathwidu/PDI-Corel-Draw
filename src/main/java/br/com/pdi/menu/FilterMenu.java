package br.com.pdi.menu;

import br.com.pdi.view.ImagePanel;
import br.com.pdi.model.ImageMatrix;

import javax.swing.*;

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
    
        grayscaleItem.addActionListener(e -> aplicarFiltro("grayscale"));
        passaBaixaItem.addActionListener(e -> aplicarFiltro("passa_baixa"));
        passaAltaItem.addActionListener(e -> aplicarFiltro("passa_alta"));
        thresholdItem.addActionListener(e -> aplicarFiltro("threshold"));
        brilhoContrasteItem.addActionListener(e -> aplicarFiltro("brilho_contraste")); 
    
        filtrosMenu.add(grayscaleItem);
        filtrosMenu.add(passaBaixaItem);
        filtrosMenu.add(passaAltaItem);
        filtrosMenu.add(thresholdItem);
        filtrosMenu.add(brilhoContrasteItem); 
    }
    

    private void aplicarFiltro(String tipo) {
        if (imagePanel.getOriginalImageMatrix() == null) {
            JOptionPane.showMessageDialog(null, "Nenhuma imagem carregada!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        ImageMatrix imagemOriginal = imagePanel.getOriginalImageMatrix();
        ImageMatrix imagemFiltrada = null;
    
        try {
            switch (tipo) {
                case "grayscale":
                    imagemFiltrada = br.com.pdi.filters.Filter.aplicarGrayscale(imagemOriginal);
                    break;
    
                case "passa_baixa":
                    imagemFiltrada = br.com.pdi.filters.Filter.aplicarPassaBaixa(imagemOriginal);
                    break;
    
                case "passa_alta":
                    ImageMatrix imagemCinza = br.com.pdi.filters.Filter.aplicarGrayscale(imagemOriginal);
                    imagemFiltrada = br.com.pdi.filters.Filter.aplicarPassaAlta(imagemCinza);
                    break;
    
                case "threshold":
                    JOptionPane.showMessageDialog(null, "Filtro Threshold ainda não implementado.");
                    break;
    
                case "brilho_contraste": 
                    String inputContraste = JOptionPane.showInputDialog("Informe o contraste (1 = sem mudança, >1 aumenta, <1 reduz):");
                    String inputBrilho = JOptionPane.showInputDialog("Informe o brilho (0 = sem mudança, positivo aumenta, negativo reduz):");
    
                    if (inputContraste != null && inputBrilho != null) {
                        double contraste = Double.parseDouble(inputContraste);
                        int brilho = Integer.parseInt(inputBrilho);
    
                        imagemFiltrada = br.com.pdi.filters.Filter.ajustarBrilhoContraste(imagemOriginal, contraste, brilho);
                    } else {
                        return;
                    }
                    break;
    
                default:
                    JOptionPane.showMessageDialog(null, "Filtro não reconhecido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
            }
    
            if (imagemFiltrada != null) {
                imagePanel.setTransformedImageMatrix(imagemFiltrada);
            }
    
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao aplicar filtro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public JMenu getFilterMenu() {
        return filtrosMenu;
    }
}
