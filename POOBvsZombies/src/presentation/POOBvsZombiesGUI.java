package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class POOBvsZombiesGUI extends JFrame {

    FondoPanel fondo = new FondoPanel();

    // Declaración de botones
    JButton buttonPVSM = new JButton();
    JButton buttonMVSM = new JButton();
    JButton buttonPVP = new JButton();

    public POOBvsZombiesGUI() {
        this.setContentPane(fondo);
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle("POOBvsZombies");

        // Crear barra de menús
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuArchivo = new JMenu("Archivo");
        menuBar.add(menuArchivo);

        JMenuItem menuItemExit = new JMenuItem("Salir");
        menuArchivo.add(menuItemExit);

        // Acción para salir
        menuItemExit.addActionListener(e -> confirmarSalida());

        // Configurar botones con imágenes
        configureButtonWithImage(buttonPVSM, "/images/PVSM.png", 200, 250);
        configureButtonWithImage(buttonMVSM, "/images/MvsM.png", 200, 250);
        configureButtonWithImage(buttonPVP, "/images/PVP.png", 200, 250);

        // Añadir acciones a los botones
        buttonPVSM.addActionListener(e -> abrirVentanaPvsM());
        buttonMVSM.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ventana MVSM aún no implementada"));
        buttonPVP.addActionListener(e -> JOptionPane.showMessageDialog(this, "Ventana PVP aún no implementada"));

        // Añadir botones al panel
        fondo.setLayout(null);
        fondo.add(buttonPVSM);
        fondo.add(buttonMVSM);
        fondo.add(buttonPVP);

        repositionButtons();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionButtons();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });
    }

    private void abrirVentanaPvsM() {
        VentanaPvsM ventanaPvsM = new VentanaPvsM(this);
        ventanaPvsM.setVisible(true);
    }

    private void confirmarSalida() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea salir?", "Confirmar salida",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void configureButtonWithImage(JButton button, String imagePath, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + imagePath);
        }
    }

    private void repositionButtons() {
        int buttonWidth = 200;
        int buttonHeight = 250;
        int spacing = 50;
        int windowWidth = fondo.getWidth();
        int windowHeight = fondo.getHeight();

        int startX = (windowWidth - (3 * buttonWidth + 2 * spacing)) / 2;
        int startY = windowHeight - 300;

        buttonPVSM.setBounds(startX, startY, buttonWidth, buttonHeight);
        buttonMVSM.setBounds(startX + buttonWidth + spacing, startY, buttonWidth, buttonHeight);
        buttonPVP.setBounds(startX + 2 * (buttonWidth + spacing), startY, buttonWidth, buttonHeight);
        fondo.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            POOBvsZombiesGUI frame = new POOBvsZombiesGUI();
            frame.setVisible(true);
        });
    }

    /**
     * Clase interna FondoPanel para manejar imágenes de fondo.
     */
    class FondoPanel extends JPanel {
        private Image imagen;

        public FondoPanel() {
            this("/images/menu.png");
        }

        public FondoPanel(String imagePath) {
            try {
                this.imagen = new ImageIcon(getClass().getResource(imagePath)).getImage();
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen de fondo: " + imagePath);
            }
        }

        @Override
        public void paint(Graphics g) {
            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
            setOpaque(false);
            super.paint(g);
        }
    }
}
