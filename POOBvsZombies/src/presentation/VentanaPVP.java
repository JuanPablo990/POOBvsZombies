package presentation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaPVP extends JFrame {

    private JTextField nombreJugador1;
    private JTextField nombreJugador2;
    private JSpinner solesIniciales;
    private JSpinner cerebrosIniciales;
    private JSpinner duracionPartida;

    private ArrayList<JButton> botonesPlantas = new ArrayList<>();
    private ArrayList<JButton> botonesZombies = new ArrayList<>();
    private ArrayList<String> plantasSeleccionadas = new ArrayList<>();
    private ArrayList<String> zombiesSeleccionados = new ArrayList<>();

    private Runnable stopMusicCallback; // Callback para detener la música

    public VentanaPVP(JFrame parent, Runnable stopMusicCallback) {
        this.stopMusicCallback = stopMusicCallback;

        // Configuración de la ventana
        setTitle("Player vs Player");
        setSize(900, 750); // Tamaño ajustado para más espacio
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        // Fondo personalizado con la imagen
        FondoPanel fondo = new FondoPanel("/presentation/images/windows/ventanapvp.png");
        fondo.setLayout(new BorderLayout());

        // Panel translúcido para toda la información
        JPanel panelTranslucido = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f)); // Opacidad 60%
                g2d.setColor(new Color(255, 255, 255));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelTranslucido.setOpaque(false);
        panelTranslucido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Bordes más amplios

        // Tiempo de partida en la parte superior
        JPanel tiempoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tiempoPanel.setOpaque(false);
        JLabel tiempoLabel = new JLabel("Match Duration (min):");
        tiempoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        duracionPartida = new JSpinner(new SpinnerNumberModel(5, 1, 60, 1)); // Duración en minutos
        tiempoPanel.add(tiempoLabel);
        tiempoPanel.add(duracionPartida);

        // Panel para Player 1 (Plants) en la izquierda
        JPanel panelPlayerPlants = createPlayerPanel("Player 1 (Plants):", true);

        // Panel para Player 2 (Zombies) en la derecha
        JPanel panelPlayerZombies = createPlayerPanel("Player 2 (Zombies):", false);

        // Panel de jugadores
        JPanel playersPanel = new JPanel(new GridLayout(1, 2, 20, 20)); // Espaciado ajustado
        playersPanel.setOpaque(false);
        playersPanel.add(panelPlayerPlants);  // Plantas a la izquierda
        playersPanel.add(panelPlayerZombies); // Zombies a la derecha

        // Panel de botones para iniciar o volver
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton iniciarPartida = new JButton("Start Game");
        iniciarPartida.setPreferredSize(new Dimension(200, 40)); // Botón más grande
        JButton volver = new JButton("Back");
        buttonPanel.add(iniciarPartida);
        buttonPanel.add(volver);

        // Acción para volver
        volver.addActionListener(e -> dispose());

        // Acción para iniciar partida
        iniciarPartida.addActionListener(e -> {
            String nombre1 = nombreJugador1.getText();
            String nombre2 = nombreJugador2.getText();

            if (nombre1.isEmpty() || nombre2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must enter both player names.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Detener la música
                if (stopMusicCallback != null) {
                    stopMusicCallback.run();
                }

                // Abrir VentanaJuegoPVP
                SwingUtilities.invokeLater(() -> new VentanaJuegoPVP(
                        nombre1,
                        nombre2,
                        (int) solesIniciales.getValue(),
                        (int) cerebrosIniciales.getValue(),
                        (int) duracionPartida.getValue()
                ));

                dispose(); // Cerrar VentanaPVP
            }
        });

        // Agregar componentes al panel translúcido
        panelTranslucido.add(tiempoPanel, BorderLayout.NORTH); // Tiempo en la parte superior
        panelTranslucido.add(playersPanel, BorderLayout.CENTER); // Jugadores en el centro
        panelTranslucido.add(buttonPanel, BorderLayout.SOUTH); // Botones en la parte inferior

        // Agregar el panel translúcido al fondo
        fondo.add(panelTranslucido, BorderLayout.CENTER);

        setContentPane(fondo);
    }

    private JPanel createPlayerPanel(String title, boolean isPlant) {
        // Panel principal para cada jugador
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), title, 0, 0, new Font("Arial", Font.BOLD, 18), Color.BLACK));

        // Subpanel para las configuraciones
        JPanel configPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 filas, 2 columnas
        configPanel.setOpaque(false);

        // Campo para el nombre del jugador
        JLabel nombreLabel = new JLabel("Player name:", JLabel.RIGHT);
        nombreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        configPanel.add(nombreLabel);
        JTextField nombreJugador = new JTextField();
        if (isPlant) {
            nombreJugador1 = nombreJugador;
        } else {
            nombreJugador2 = nombreJugador;
        }
        configPanel.add(nombreJugador);

        // Campo para soles o cerebros iniciales
        JLabel recursosLabel = new JLabel(isPlant ? "Initial Suns:" : "Initial Brains:", JLabel.RIGHT);
        recursosLabel.setFont(new Font("Arial", Font.BOLD, 14));
        configPanel.add(recursosLabel);
        JSpinner recursosIniciales = new JSpinner(new SpinnerNumberModel(100, 50, 500, 10));
        if (isPlant) {
            solesIniciales = recursosIniciales;
        } else {
            cerebrosIniciales = recursosIniciales;
        }
        configPanel.add(recursosIniciales);

        // Subpanel para la selección de plantas/zombies con imágenes
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        selectionPanel.setOpaque(false);
        String[] opciones = isPlant ? new String[]{"Sunflower", "Peashooter", "Wall-nut", "Potato Mine", "ECIplant"} : new String[]{"Basic Zombie", "Conehead", "Buckethead", "Brainstein", "ECIZombie"};
        ArrayList<JButton> botones = isPlant ? botonesPlantas : botonesZombies;
        for (String opcion : opciones) {
            JButton boton = new JButton();
            try {
                // Asignar imágenes específicas para cada opción
                String rutaImagen = isPlant ?
                        switch (opcion) {
                            case "Sunflower" -> "/presentation/images/images_Plants/girasolcarta.png";
                            case "Peashooter" -> "/presentation/images/images_Plants/tiracarta.png";
                            case "Wall-nut" -> "/presentation/images/images_Plants/nuescarta.png";
                            case "Potato Mine" -> "/presentation/images/images_Plants/pumcarta.png";
                            case "ECIplant" -> "/presentation/images/images_Plants/ecicarta.png";
                            default -> "/presentation/images/images_Plants/default.png";
                        } :
                        switch (opcion) {
                            case "Basic Zombie" -> "/presentation/images/images_Zombies/basicocarta.png";
                            case "Conehead" -> "/presentation/images/images_Zombies/conocarta.png";
                            case "Buckethead" -> "/presentation/images/images_Zombies/cubetacarta.png";
                            case "Brainstein" -> "/presentation/images/images_Zombies/cerebrocarta.png";
                            case "ECIZombie" -> "/presentation/images/images_Zombies/bombacarta.png";
                            default -> "/presentation/images/images_Zombies/default.png";
                        };
                ImageIcon icono = new ImageIcon(getClass().getResource(rutaImagen));
                Image imagenEscalada = icono.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH); // Cambiar tamaño a 120x120
                boton.setIcon(new ImageIcon(imagenEscalada));
            } catch (Exception e) {
                boton.setText(opcion);
            }
            boton.setFont(new Font("Arial", Font.BOLD, 12));
            boton.setOpaque(true);
            boton.setBackground(Color.WHITE);
            boton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            boton.addActionListener(e -> toggleSelection(boton, opcion, isPlant));
            botones.add(boton);
            selectionPanel.add(boton);
        }

        // Agregar subpaneles al panel principal
        panel.add(configPanel, BorderLayout.NORTH);
        panel.add(selectionPanel, BorderLayout.CENTER);

        return panel;
    }

    private void toggleSelection(JButton boton, String opcion, boolean isPlant) {
        ArrayList<String> seleccionadas = isPlant ? plantasSeleccionadas : zombiesSeleccionados;
        if (seleccionadas.contains(opcion)) {
            seleccionadas.remove(opcion);
            boton.setBackground(Color.WHITE);
        } else {
            seleccionadas.add(opcion);
            boton.setBackground(new Color(0, 255, 0, 128)); // Verde translúcido
        }
    }

    /**
     * Clase interna para el fondo con una imagen.
     */
    class FondoPanel extends JPanel {
        private Image imagen;

        public FondoPanel(String rutaImagen) {
            try {
                this.imagen = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen de fondo: " + rutaImagen);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPVP ventanaPVP = new VentanaPVP(null, null); // No detiene música en este caso
            ventanaPVP.setVisible(true);
        });
    }
}
