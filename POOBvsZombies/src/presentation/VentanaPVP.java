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

    public VentanaPVP(JFrame parent) {
        // Configuración de la ventana
        setTitle("Player vs Player");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        // Fondo personalizado con la imagen
        FondoPanel fondo = new FondoPanel("/presentation/images/ventanapvp.jpeg");
        fondo.setLayout(new BorderLayout());

        // Panel translúcido para toda la información
        JPanel panelTranslucido = new JPanel(new GridLayout(1, 2, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f)); // Opacidad 90%
                g2d.setColor(new Color(255, 255, 255));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelTranslucido.setOpaque(false);
        panelTranslucido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para Player 1 (Plants) en la izquierda
        JPanel panelPlayerPlants = createPlayerPanel("Player 1 (Plants):", true);

        // Panel para Player 2 (Zombies) en la derecha
        JPanel panelPlayerZombies = createPlayerPanel("Player 2 (Zombies):", false);

        // Agregar los paneles al panel translúcido
        panelTranslucido.add(panelPlayerPlants);  // Plantas a la izquierda
        panelTranslucido.add(panelPlayerZombies); // Zombies a la derecha

        // Panel para "Tiempo de partida" y botones de acción
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);

        // Tiempo de partida centrado
        JPanel tiempoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tiempoPanel.setOpaque(false);
        JLabel tiempoLabel = new JLabel("Tiempo de partida (min):");
        tiempoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        duracionPartida = new JSpinner(new SpinnerNumberModel(5, 1, 60, 1)); // Duración en minutos
        tiempoPanel.add(tiempoLabel);
        tiempoPanel.add(duracionPartida);

        // Panel de botones para iniciar o volver
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton iniciarPartida = new JButton("Empezar a Jugar");
        iniciarPartida.setPreferredSize(new Dimension(200, 40)); // Botón más grande
        JButton volver = new JButton("Volver");
        buttonPanel.add(iniciarPartida);
        buttonPanel.add(volver);

        // Acción para volver
        volver.addActionListener(e -> dispose());

        // Acción para iniciar partida
        iniciarPartida.addActionListener(e -> {
            String nombre1 = nombreJugador1.getText();
            String nombre2 = nombreJugador2.getText();
            if (nombre1.isEmpty() || nombre2.isEmpty() || plantasSeleccionadas.size() != 5 || zombiesSeleccionados.size() != 5) {
                JOptionPane.showMessageDialog(this, "Debe ingresar los nombres y seleccionar 5 plantas y 5 zombies.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Partida configurada:\n" +
                                "Player 1: " + nombre1 + " (Soles iniciales: " + solesIniciales.getValue() + ")\n" +
                                "Player 2: " + nombre2 + " (Cerebros iniciales: " + cerebrosIniciales.getValue() + ")\n" +
                                "Duración: " + duracionPartida.getValue() + " minutos\n" +
                                "Plantas seleccionadas: " + plantasSeleccionadas + "\n" +
                                "Zombies seleccionados: " + zombiesSeleccionados);
                dispose();
            }
        });

        // Agregar tiempo de partida y botones al panel inferior
        bottomPanel.add(tiempoPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Agregar los paneles al fondo
        fondo.add(panelTranslucido, BorderLayout.CENTER);
        fondo.add(bottomPanel, BorderLayout.SOUTH);

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
        JLabel nombreLabel = new JLabel("Nombre del jugador:", JLabel.RIGHT);
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
        JLabel recursosLabel = new JLabel(isPlant ? "Soles iniciales:" : "Cerebros iniciales:", JLabel.RIGHT);
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
        String[] opciones = isPlant ? new String[]{"Girasol", "Lanzaguisantes", "Nuez", "Papa Explosiva", "ECIPlant"} : new String[]{"Zombie Básico", "Conehead", "Buckethead", "Brainstein", "ECIZombie"};
        ArrayList<JButton> botones = isPlant ? botonesPlantas : botonesZombies;
        for (String opcion : opciones) {
            JButton boton = new JButton();
            try {
                // Reemplaza "/ruta/a/imagen.png" con la imagen correspondiente
                ImageIcon icono = new ImageIcon(getClass().getResource("/presentation/images/" + opcion.toLowerCase().replace(" ", "_") + ".png"));
                Image imagenEscalada = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
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
        } else if (seleccionadas.size() < 5) {
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
            VentanaPVP ventanaPVP = new VentanaPVP(null);
            ventanaPVP.setVisible(true);
        });
    }
}
