package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class VentanaJuego extends JFrame {
    public VentanaJuego(String nombreJugador, String rol, int tiempo, int hordas) {
        super("POOBvsZombies - Juego");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear barra de menús
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menuArchivo = new JMenu("Archivo");
        menuBar.add(menuArchivo);

        JMenuItem menuItemNew = new JMenuItem("Nuevo");
        JMenuItem menuItemOpen = new JMenuItem("Abrir");
        JMenuItem menuItemSave = new JMenuItem("Salvar");
        JMenuItem menuItemImport = new JMenuItem("Importar");
        JMenuItem menuItemExport = new JMenuItem("Exportar");
        JMenuItem menuItemExit = new JMenuItem("Salir");

        menuArchivo.add(menuItemNew);
        menuArchivo.add(menuItemOpen);
        menuArchivo.add(menuItemSave);
        menuArchivo.add(menuItemImport);
        menuArchivo.add(menuItemExport);
        menuArchivo.addSeparator();
        menuArchivo.add(menuItemExit);

        menuItemExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea salir?", "Confirmar salida",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        });

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        setContentPane(panelPrincipal);

        // Panel superior (NORTE)
        JPanel panelNorte = new JPanel(new GridLayout(1, 11));
        panelNorte.setPreferredSize(new Dimension(0, 100));
        for (int i = 1; i <= 11; i++) {
            JLabel label = new JLabel("Info " + i, SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panelNorte.add(label);
        }
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
                    imagePath = "/images/piso_casa.png";
                } else if (col == 9) {
                    imagePath = "/images/padimento_zombies.png";
                } else {
                    imagePath = "/images/pasto.png";
                }

                // Asignar la imagen al botón
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
                    boton.addComponentListener(new ComponentAdapter() {
                        @Override
                        public void componentResized(ComponentEvent e) {
                            if (boton.getWidth() > 0 && boton.getHeight() > 0) {
                                Image scaledImage = icon.getImage().getScaledInstance(boton.getWidth(), boton.getHeight(),
                                        Image.SCALE_SMOOTH);
                                boton.setIcon(new ImageIcon(scaledImage));
                            }
                        }
                    });
                } catch (Exception e) {
                    System.out.println("No se pudo cargar la imagen para el botón: " + e.getMessage());
                }

                panelCentro.add(boton);
            }
        }
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);

        // Panel inferior (SUR)
        JPanel panelSur = new JPanel(new GridLayout(1, 2));
        panelSur.setPreferredSize(new Dimension(0, 100));

        JButton botonMenu = new JButton("Menú");
        panelSur.add(botonMenu);

        JPanel panelSurDerecho = new JPanel(new GridLayout(2, 1));
        JLabel labelHordaActual = new JLabel("Horda: 1 de " + hordas, SwingConstants.CENTER);
        JLabel labelTiempoHorda = new JLabel("Tiempo por horda: Calculando...", SwingConstants.CENTER);
        panelSurDerecho.add(labelHordaActual);
        panelSurDerecho.add(labelTiempoHorda);
        panelSur.add(panelSurDerecho);

        panelPrincipal.add(panelSur, BorderLayout.SOUTH);

        // Lógica para cronómetros
        new Thread(() -> {
            int tiempoTotalSegundos = tiempo * 60;
            int tiempoPorHorda = tiempoTotalSegundos / hordas;
            int tiempoRestanteHorda = tiempoPorHorda;
            int hordaActual = 1;

            try {
                for (int i = tiempoTotalSegundos; i >= 0; i--) {
                    if (tiempoRestanteHorda == 0 && hordaActual < hordas) {
                        hordaActual++;
                        tiempoRestanteHorda = tiempoPorHorda;
                        labelHordaActual.setText("Horda: " + hordaActual + " de " + hordas);
                    }

                    tiempoRestanteHorda--;
                    labelTiempoHorda.setText("Tiempo por horda: " + tiempoRestanteHorda / 60 + ":"
                            + tiempoRestanteHorda % 60);
                    Thread.sleep(1000);
                }
                JOptionPane.showMessageDialog(this, "¡Juego terminado!");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();

        setVisible(true);
    }
}
