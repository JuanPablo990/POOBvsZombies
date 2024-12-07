package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class VentanaJuegoPVP extends JFrame {

    public VentanaJuegoPVP(String nombreJugador1, String nombreJugador2, int solesIniciales, int cerebrosIniciales, int duracionPartida) {
        super("POOBvsZombies - Player vs Player");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        int iconSize = 80; // Tamaño específico para las imágenes

        // Crear barra de menús
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuArchivo = new JMenu("File");
        menuBar.add(menuArchivo);

        JMenuItem menuItemExit = new JMenuItem("Exit");
        menuArchivo.add(menuItemExit);

        menuItemExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        setContentPane(panelPrincipal);

        // Panel superior (NORTE)
        JPanel panelNorte = new JPanel(new GridLayout(1, 14));
        panelNorte.setPreferredSize(new Dimension(0, 100));

        // Info 1: Jugador 1 con imagen y contador
        JPanel panelJugador1 = createPlayerPanel(nombreJugador1, "/presentation/images/images_Plants/sol.png", solesIniciales, iconSize);
        panelNorte.add(panelJugador1);

        // Infos 2 a 6: Botones con imagen de maseta y fondo marrón
        for (int i = 2; i <= 6; i++) {
            JButton boton = createButtonWithImage("/presentation/images/images_Plants/maseta.png", new Color(139, 69, 19), iconSize);
            panelNorte.add(boton);
        }

        // Infos 7 y 8: Botones normales
        for (int i = 7; i <= 8; i++) {
            JButton boton = new JButton("Info " + i);
            boton.setBackground(Color.LIGHT_GRAY);
            panelNorte.add(boton);
        }

        // Infos 9 a 13: Botones con imagen de entrada y fondo azul
        for (int i = 9; i <= 13; i++) {
            JButton boton = createButtonWithImage("/presentation/images/images_Zombies/entrada.png", new Color(70, 130, 180), iconSize);
            panelNorte.add(boton);
        }

        // Info 14: Jugador 2 con imagen y contador
        JPanel panelJugador2 = createPlayerPanel(nombreJugador2, "/presentation/images/images_Zombies/cerebro.png", cerebrosIniciales, iconSize);
        panelNorte.add(panelJugador2);

        panelPrincipal.add(panelNorte, BorderLayout.NORTH);

        // Panel central (CENTRO)
        JPanel panelCentro = new JPanel(new GridLayout(5, 10));
        JButton[][] botones = new JButton[5][10];

        for (int fila = 0; fila < 5; fila++) {
            for (int col = 0; col < 10; col++) {
                JButton boton = new JButton();
                boton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                boton.setBackground(new Color(0, 100, 0)); // Verde oscuro
                boton.setOpaque(true);
                botones[fila][col] = boton;

                // Determinar la imagen según la posición
                String imagePath;
                if (col == 0) {
                    imagePath = "/presentation/images/images_Plants/pisocasa.png";
                } else if (col == 9) {
                    imagePath = "/presentation/images/images_Zombies/padimento_zombies.png";
                } else {
                    imagePath = "/presentation/images/images_Plants/pasto.png";
                }

                // Asignar la imagen al botón
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
                    Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                    boton.setIcon(new ImageIcon(scaledImage));
                } catch (Exception e) {
                    System.out.println("No se pudo cargar la imagen para el botón: " + e.getMessage());
                }

                panelCentro.add(boton);
            }
        }
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // Panel inferior (SUR)
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setPreferredSize(new Dimension(0, 100));

        // Info 15 con imagen de pala
        JButton info15 = createButtonWithImage("/presentation/images/images_Plants/pala.png", Color.WHITE, iconSize);
        panelSur.add(info15, BorderLayout.WEST);

        // Menú con imagen
        JButton menuButton = createButtonWithImage("/presentation/images/windows/menujuego.png", Color.WHITE, iconSize);
        panelSur.add(menuButton, BorderLayout.EAST);

        // Información en el centro del panel inferior
        JPanel panelInfoSur = new JPanel(new GridLayout(2, 1));
        JLabel labelDuracion = new JLabel("Duration: " + duracionPartida + " minutes", SwingConstants.CENTER);
        JLabel labelTemporizador = new JLabel("Time left: Calculating...", SwingConstants.CENTER);
        panelInfoSur.add(labelDuracion);
        panelInfoSur.add(labelTemporizador);
        panelSur.add(panelInfoSur, BorderLayout.CENTER);

        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        // Temporizador
        new Thread(() -> {
            int tiempoRestanteSegundos = duracionPartida * 60;
            try {
                while (tiempoRestanteSegundos > 0) {
                    int minutos = tiempoRestanteSegundos / 60;
                    int segundos = tiempoRestanteSegundos % 60;
                    labelTemporizador.setText("Time left: " + minutos + ":" + String.format("%02d", segundos));
                    Thread.sleep(1000);
                    tiempoRestanteSegundos--;
                }
                JOptionPane.showMessageDialog(this, "Time is up!");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();

        menuButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private JPanel createPlayerPanel(String playerName, String imagePath, int initialCount, int iconSize) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel nameLabel = new JLabel(playerName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(nameLabel, BorderLayout.NORTH);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.out.println("Image not found: " + e.getMessage());
        }
        panel.add(imageLabel, BorderLayout.CENTER);

        JLabel counterLabel = new JLabel(String.valueOf(initialCount), SwingConstants.CENTER);
        counterLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(counterLabel, BorderLayout.SOUTH);

        // Increment the counter based on the resource type
        new Thread(() -> {
            try {
                while (true) {
                    int increment = imagePath.contains("sol") ? 25 : 50;
                    int currentValue = Integer.parseInt(counterLabel.getText());
                    counterLabel.setText(String.valueOf(currentValue + increment));
                    Thread.sleep(10000);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();

        return panel;
    }

    private JButton createButtonWithImage(String imagePath, Color background, int iconSize) {
        JButton button = new JButton();
        button.setBackground(background);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.out.println("Image not found: " + e.getMessage());
        }
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaJuegoPVP("Player 1", "Player 2", 200, 300, 5));
    }
}
