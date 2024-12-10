package presentation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaPVP extends JFrame {

    private JTextField nombreJugador1;
    private JTextField nombreJugador2;
    private JSpinner solesIniciales;
    private JSpinner cerebrosIniciales;
    private JSpinner duracionPartida;

    private ArrayList<Element> plantasSeleccionadas = new ArrayList<>();
    private ArrayList<Element> zombiesSeleccionados = new ArrayList<>();

    private Runnable stopMusicCallback; // Callback para detener la música

    public VentanaPVP(JFrame parent, Runnable stopMusicCallback) {
        this.stopMusicCallback = stopMusicCallback;

        // Configuración de la ventana
        setTitle("Player vs Player");
        setSize(900, 750);
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
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
                g2d.setColor(new Color(255, 255, 255));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelTranslucido.setOpaque(false);
        panelTranslucido.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tiempo de partida en la parte superior
        JPanel tiempoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tiempoPanel.setOpaque(false);
        JLabel tiempoLabel = new JLabel("Match Duration (min):");
        tiempoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        duracionPartida = new JSpinner(new SpinnerNumberModel(5, 1, 60, 1));
        tiempoPanel.add(tiempoLabel);
        tiempoPanel.add(duracionPartida);

        // Panel para Player 1 (Plants) y Player 2 (Zombies)
        JPanel panelPlayerPlants = createPlayerPanel("Player 1 (Plants):", true);
        JPanel panelPlayerZombies = createPlayerPanel("Player 2 (Zombies):", false);

        JPanel playersPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        playersPanel.setOpaque(false);
        playersPanel.add(panelPlayerPlants);
        playersPanel.add(panelPlayerZombies);

        // Botones para iniciar o volver
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton iniciarPartida = new JButton("Start Game");
        JButton volver = new JButton("Back");
        buttonPanel.add(iniciarPartida);
        buttonPanel.add(volver);

        volver.addActionListener(e -> dispose());
        iniciarPartida.addActionListener(e -> iniciarJuego());

        panelTranslucido.add(tiempoPanel, BorderLayout.NORTH);
        panelTranslucido.add(playersPanel, BorderLayout.CENTER);
        panelTranslucido.add(buttonPanel, BorderLayout.SOUTH);

        fondo.add(panelTranslucido, BorderLayout.CENTER);
        setContentPane(fondo);
    }

    private JPanel createPlayerPanel(String title, boolean isPlant) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), title));

        JPanel configPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        configPanel.setOpaque(false);

        JLabel nombreLabel = new JLabel("Player name:", JLabel.RIGHT);
        JTextField nombreJugador = new JTextField();
        if (isPlant) {
            nombreJugador1 = nombreJugador;
        } else {
            nombreJugador2 = nombreJugador;
        }
        configPanel.add(nombreLabel);
        configPanel.add(nombreJugador);

        JLabel recursosLabel = new JLabel(isPlant ? "Initial Suns:" : "Initial Brains:", JLabel.RIGHT);
        JSpinner recursosIniciales = new JSpinner(new SpinnerNumberModel(100, 50, 500, 10));
        if (isPlant) {
            solesIniciales = recursosIniciales;
        } else {
            cerebrosIniciales = recursosIniciales;
        }
        configPanel.add(recursosLabel);
        configPanel.add(recursosIniciales);

        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        selectionPanel.setOpaque(false);

        List<Element> elementos = isPlant ? getPlantasDisponibles() : getZombiesDisponibles();
        for (Element elemento : elementos) {
            JButton boton = createElementButton(elemento, isPlant);
            selectionPanel.add(boton);
        }

        panel.add(configPanel, BorderLayout.NORTH);
        panel.add(selectionPanel, BorderLayout.CENTER);
        return panel;
    }

    private JButton createElementButton(Element elemento, boolean isPlant) {
        JButton boton = new JButton();
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);

        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(elemento.getImagePath()));

            // Ajustar imagen al tamaño del botón dinámicamente
            boton.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    int width = boton.getWidth();
                    int height = boton.getHeight();
                    if (width > 0 && height > 0) {
                        Image imagenEscalada = icono.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        boton.setIcon(new ImageIcon(imagenEscalada));
                    }
                }
            });
        } catch (Exception e) {
            boton.setText(elemento.getName());
        }

        boton.addActionListener(e -> toggleSelection(boton, elemento, isPlant));
        return boton;
    }

    private void toggleSelection(JButton boton, Element elemento, boolean isPlant) {
        List<Element> seleccionados = isPlant ? plantasSeleccionadas : zombiesSeleccionados;
        if (seleccionados.contains(elemento)) {
            // Deseleccionar
            seleccionados.remove(elemento);
            boton.setBackground(null);
            boton.setOpaque(false);
        } else {
            // Seleccionar
            seleccionados.add(elemento);
            boton.setBackground(new Color(0, 255, 0, 128)); // Verde translúcido
            boton.setOpaque(true);
        }
    }

    private List<Element> getPlantasDisponibles() {
        return List.of(
                new Element("Sunflower", "/presentation/images/images_Plants/girasolcarta.png"),
                new Element("Peashooter", "/presentation/images/images_Plants/tiracarta.png"),
                new Element("Wall-nut", "/presentation/images/images_Plants/nuescarta.png"),
                new Element("Potato Mine", "/presentation/images/images_Plants/pumcarta.png"),
                new Element("ECIplant", "/presentation/images/images_Plants/ecicarta.png")
        );
    }

    private List<Element> getZombiesDisponibles() {
        return List.of(
                new Element("Basic Zombie", "/presentation/images/images_Zombies/basicocarta.png"),
                new Element("Conehead", "/presentation/images/images_Zombies/conocarta.png"),
                new Element("Buckethead", "/presentation/images/images_Zombies/cubetacarta.png"),
                new Element("Brainstein", "/presentation/images/images_Zombies/cerebrocarta.png"),
                new Element("ECIZombie", "/presentation/images/images_Zombies/bombacarta.png")
        );
    }

    private void iniciarJuego() {
        if (nombreJugador1.getText().isEmpty() || nombreJugador2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both player names are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (plantasSeleccionadas.isEmpty() || zombiesSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You must select at least one plant and one zombie.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (stopMusicCallback != null) {
            stopMusicCallback.run();
        }

        SwingUtilities.invokeLater(() -> new VentanaJuegoPVP(
                nombreJugador1.getText(),
                nombreJugador2.getText(),
                (int) solesIniciales.getValue(),
                (int) cerebrosIniciales.getValue(),
                (int) duracionPartida.getValue(),
                plantasSeleccionadas,
                zombiesSeleccionados
        ));
        dispose();
    }

    class FondoPanel extends JPanel {
        private final Image imagen;

        public FondoPanel(String rutaImagen) {
            this.imagen = new ImageIcon(getClass().getResource(rutaImagen)).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPVP(null, null).setVisible(true));
    }
}
