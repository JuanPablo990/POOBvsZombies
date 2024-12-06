package presentation;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class POOBvsZombiesGUI extends JFrame {

    FondoPanel fondo = new FondoPanel();
    private Clip backgroundMusic;

    // Declaración de botones
    JButton buttonPVSM = new JButton();
    JButton buttonMVSM = new JButton();
    JButton buttonPVP = new JButton();

    public POOBvsZombiesGUI() {
        this.setContentPane(fondo);
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle("POOBvsZombies");

        // Iniciar música de fondo
        startBackgroundMusic("/presentation/songs/menu.wav");

        // Crear barra de menús
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuFile = new JMenu("Archive");
        menuBar.add(menuFile);

        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuFile.add(menuItemExit);

        // Acción para salir
        menuItemExit.addActionListener(e -> confirmExit());

        // Configurar botones con imágenes
        configureButtonWithImage(buttonPVSM, "/presentation/images/windows/PVSM.png", 200, 250);
        configureButtonWithImage(buttonMVSM, "/presentation/images/windows/MvsM.png", 200, 250);
        configureButtonWithImage(buttonPVP, "/presentation/images/windows/PVP.png", 200, 250);

        // Añadir acciones a los botones
        buttonPVSM.addActionListener(e -> openPvsMWindow());
        buttonMVSM.addActionListener(e -> JOptionPane.showMessageDialog(this, "MVSM window not yet implemented"));
        buttonPVP.addActionListener(e -> openPVPWindow());

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
                confirmExit();
            }
        });
    }

    private void startBackgroundMusic(String musicPath) {
        try {
            File audioFile = new File(getClass().getResource(musicPath).getFile());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Repetir continuamente
            backgroundMusic.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error loading background music: " + e.getMessage());
        }
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }

    private void openPvsMWindow() {
        VentanaPvsM ventanaPvsM = new VentanaPvsM(this);
        ventanaPvsM.setVisible(true);
    }

    private void openPVPWindow() {
        VentanaPVP ventanaPVP = new VentanaPVP(this, this::stopBackgroundMusic); // Paso el método para detener la música
        ventanaPVP.setVisible(true);
    }

    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm exit",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            stopBackgroundMusic(); // Detener música al salir
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
            JOptionPane.showMessageDialog(this, "Error loading image: " + imagePath);
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
            this("/presentation/images/windows/menu.png");
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
