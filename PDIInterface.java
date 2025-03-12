import javax.swing.*;

public class PDIInterface extends JFrame {

    private ImagePanel imagePanel;
    private MenuBarHandler menuBarHandler;

    public PDIInterface() {
        setTitle("Software de Processamento de Imagens - Autor: Matheus Duarte");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        imagePanel = new ImagePanel();
        menuBarHandler = new MenuBarHandler(this, imagePanel);

        setJMenuBar(menuBarHandler.getMenuBar());
        add(imagePanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PDIInterface::new);
    }
}

