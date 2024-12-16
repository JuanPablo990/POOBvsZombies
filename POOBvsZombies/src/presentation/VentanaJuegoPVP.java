package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class VentanaJuegoPVP extends JFrame {

    private JLabel puntosSolesLabel;
    private JLabel puntosCerebrosLabel;
    private JLabel contadorSolesJugador1Label;
    private JLabel contadorCerebrosJugador2Label;
    private JLabel labelTemporizador;
    private JLabel labelRonda;
    private int puntosSoles = 0;
    private int puntosCerebros = 0;
    private int contadorSolesJugador1 = 0;
    private int contadorCerebrosJugador2 = 0;
    private int tiempoRestante;
    private int rondaActual = 1;
    private final int maxRondas = 5;
    private boolean primeraRondaDeColocacion = true;
    private Timer timer;
    private JButton[][] botones;

    public VentanaJuegoPVP(String nombreJugador1, String nombreJugador2, int solesIniciales, int cerebrosIniciales, int duracionPartida, List<String> plantasSeleccionadas, List<String> zombiesSeleccionados) {
        super("POOBvsZombies - Player vs Player");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tiempoRestante = 2 * 60;
        contadorSolesJugador1 = solesIniciales;
        contadorCerebrosJugador2 = cerebrosIniciales;

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuArchivo = new JMenu("Archive");
        menuBar.add(menuArchivo);

        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuArchivo.add(menuItemExit);

        menuItemExit.addActionListener(e -> confirmExit());

        JPanel panelPrincipal = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon fondo = new ImageIcon(getClass().getResource("/presentation/images/windows/fondojuego.jpg"));
                g.drawImage(fondo.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        setContentPane(panelPrincipal);

        JPanel panelNorte = new JPanel(new GridLayout(1, 14));
        panelNorte.setOpaque(false);
        panelNorte.setPreferredSize(new Dimension(0, 150));

        JLabel[] contadorSolesLabelRef = new JLabel[1];
        JPanel panelJugador1 = createPlayerPanel(
                nombreJugador1, "/presentation/images/images_Plants/sol.png", contadorSolesJugador1, 80, new Color(245, 245, 220), contadorSolesLabelRef
        );
        contadorSolesJugador1Label = contadorSolesLabelRef[0];
        panelNorte.add(panelJugador1);

        for (String plantaPath : plantasSeleccionadas) {
            JButton boton = createButtonWithDynamicImage(plantaPath, new Color(205, 133, 63));
            panelNorte.add(boton);
        }

        puntosSolesLabel = createSquareLabelWithImageAndCenteredText(
                "/presentation/images/images_Plants/puntos.png",
                String.valueOf(puntosSoles), 150, new Color(0, 0, 0, 128)
        );
        panelNorte.add(puntosSolesLabel);

        puntosCerebrosLabel = createSquareLabelWithImageAndCenteredText(
                "/presentation/images/images_Zombies/puntoszombies.png",
                String.valueOf(puntosCerebros), 150, new Color(0, 0, 0, 128)
        );
        panelNorte.add(puntosCerebrosLabel);

        for (String zombiePath : zombiesSeleccionados) {
            JButton boton = createButtonWithDynamicImage(zombiePath, new Color(70, 130, 180));
            panelNorte.add(boton);
        }

        JLabel[] contadorCerebrosLabelRef = new JLabel[1];
        JPanel panelJugador2 = createPlayerPanel(
                nombreJugador2, "/presentation/images/images_Zombies/cerebro.png", contadorCerebrosJugador2, 80, new Color(245, 245, 220), contadorCerebrosLabelRef
        );
        contadorCerebrosJugador2Label = contadorCerebrosLabelRef[0];
        panelNorte.add(panelJugador2);

        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(5, 10));
        panelCentro.setOpaque(false);
        botones = new JButton[5][10];

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 10; col++) {
                String imagePath = (col == 0) ?
                        "/presentation/images/images_Plants/pisocasa.png" :
                        (col == 9) ?
                                "/presentation/images/images_Zombies/padimento_zombies.png" :
                                "/presentation/images/images_Plants/pasto.png";

                JButton boton = createButtonWithDynamicImage(imagePath, new Color(0, 100, 0));
                botones[fila][col] = boton;
                panelCentro.add(boton);
            }
        }
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setOpaque(false);
        panelSur.setPreferredSize(new Dimension(0, 120));

        JButton info15 = createButtonWithFixedImage("/presentation/images/images_Plants/pala.png", new Color(139, 69, 19), 100);
        panelSur.add(info15, BorderLayout.WEST);

        JButton menuButton = createButtonWithFixedImage("/presentation/images/windows/menujuego.png", new Color(245, 245, 220), 100);
        menuButton.addActionListener(e -> returnToMainMenu());
        panelSur.add(menuButton, BorderLayout.EAST);

        JButton nextRoundButton = new JButton("Next Round");
        nextRoundButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextRoundButton.setBackground(new Color(0, 128, 0));
        nextRoundButton.setForeground(Color.WHITE);
        panelSur.add(nextRoundButton, BorderLayout.SOUTH);

        JPanel panelInfoSur = new JPanel(new GridLayout(3, 1));
        panelInfoSur.setOpaque(false);

        JLabel labelDuracion = new JLabel("Duration: " + duracionPartida + " minutes", SwingConstants.CENTER);
        labelDuracion.setFont(new Font("Arial", Font.BOLD, 16));
        labelDuracion.setForeground(Color.WHITE);

        labelTemporizador = new JLabel("Time left: " + formatTime(tiempoRestante), SwingConstants.CENTER);
        labelTemporizador.setFont(new Font("Arial", Font.BOLD, 48));
        labelTemporizador.setForeground(Color.WHITE);

        labelRonda = new JLabel("Round: " + rondaActual, SwingConstants.CENTER);
        labelRonda.setFont(new Font("Arial", Font.BOLD, 16));
        labelRonda.setForeground(Color.WHITE);

        panelInfoSur.add(labelDuracion);
        panelInfoSur.add(labelTemporizador);
        panelInfoSur.add(labelRonda);
        panelSur.add(panelInfoSur, BorderLayout.CENTER);

        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void returnToMainMenu() {
        int confirm = JOptionPane.showConfirmDialog(this, "Do you want to return to the main menu?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new POOBvsZombiesGUI().setVisible(true));
        }
    }

    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm exit",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (timer != null) {
                timer.stop();
            }
            System.exit(0);
        }
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private JPanel createPlayerPanel(String playerName, String imagePath, int initialCount, int iconSize, Color backgroundColor, JLabel[] counterLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);

        JLabel nameLabel = new JLabel(playerName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(nameLabel, BorderLayout.NORTH);

        JLabel imageLabel = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            imageLabel.setText("Image");
        }
        panel.add(imageLabel, BorderLayout.CENTER);

        JLabel counter = new JLabel(String.valueOf(initialCount), SwingConstants.CENTER);
        counter.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(counter, BorderLayout.SOUTH);

        counterLabel[0] = counter;
        return panel;
    }

    private JButton createButtonWithDynamicImage(String imagePath, Color background) {
        JButton button = new JButton();
        button.setBackground(background);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            button.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (button.getWidth() > 0 && button.getHeight() > 0) {
                        Image scaledImage = icon.getImage().getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
                        button.setIcon(new ImageIcon(scaledImage));
                    }
                }
            });
        } catch (Exception e) {
            button.setText("Error");
        }
        return button;
    }

    private JLabel createSquareLabelWithImageAndCenteredText(String imagePath, String text, int size, Color backgroundColor) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(backgroundColor);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            label.setText("Error");
        }

        label.setPreferredSize(new Dimension(size, size));
        return label;
    }

    private JButton createButtonWithFixedImage(String imagePath, Color background, int size) {
        JButton button = new JButton();
        button.setBackground(background);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            button.setText("Error");
        }
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                List<String> plantasSeleccionadas = List.of(
                        "/presentation/images/images_Plants/girasolcarta.png",
                        "/presentation/images/images_Plants/tiracarta.png"
                );

                List<String> zombiesSeleccionados = List.of(
                        "/presentation/images/images_Zombies/basicocarta.png"
                );

                new VentanaJuegoPVP("Player 1", "Player 2", 200, 300, 5, plantasSeleccionadas, zombiesSeleccionados);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
