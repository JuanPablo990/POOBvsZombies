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

        // Almacenar soles y cerebros iniciales, pero no mostrarlos por ahora
        int inicialesPlantas = solesIniciales;
        int inicialesZombies = cerebrosIniciales;

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
        JPanel panelNorte = new JPanel(new GridLayout(1, 14));
        panelNorte.setPreferredSize(new Dimension(0, 100));

        // Info 1: Nombre del jugador de las plantas convertido en botón
        JButton botonJugadorPlantas = new JButton("Jugador 1: " + nombreJugador1);
        botonJugadorPlantas.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelNorte.add(botonJugadorPlantas);

        // Info 2 a Info 13 como botones
        for (int i = 2; i <= 13; i++) {
            JButton botonInfo = new JButton("Info " + i);
            botonInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panelNorte.add(botonInfo);
        }

        // Info 14: Nombre del jugador de los zombies convertido en botón
        JButton botonJugadorZombies = new JButton("Jugador 2: " + nombreJugador2);
        botonJugadorZombies.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelNorte.add(botonJugadorZombies);

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
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setPreferredSize(new Dimension(0, 100));

        // Botón de Menú y "Info 15" como botones del mismo tamaño
        JPanel panelMenu = new JPanel(new GridLayout(1, 2));
        JButton botonMenu = new JButton("Menú");
        JButton botonInfo15 = new JButton("Info 15");
        botonMenu.setPreferredSize(new Dimension(0, 100));
        botonInfo15.setPreferredSize(new Dimension(0, 100));
        panelMenu.add(botonMenu);
        panelMenu.add(botonInfo15);
        panelSur.add(panelMenu, BorderLayout.WEST);

        // Información en el centro del panel inferior
        JPanel panelInfoSur = new JPanel(new GridLayout(2, 1));
        JLabel labelDuracion = new JLabel("Duración: " + duracionPartida + " minutos", SwingConstants.CENTER);
        JLabel labelTemporizador = new JLabel("Tiempo restante: Calculando...", SwingConstants.CENTER);
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
                    labelTemporizador.setText("Tiempo restante: " + minutos + ":" + String.format("%02d", segundos));
                    Thread.sleep(1000);
                    tiempoRestanteSegundos--;
                }
                JOptionPane.showMessageDialog(this, "¡Tiempo agotado!");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();

        botonMenu.addActionListener(e -> dispose());

        setVisible(true);
    }
}
