package presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class VentanaPVP extends JFrame {

    private JTextField nombreJugador1;
    private JTextField nombreJugador2;
    private JSpinner solesIniciales;
    private JSpinner cerebrosIniciales;
    private JSpinner duracionPartida;
    private List<String> plantasSeleccionadas = new ArrayList<>();
    private List<String> zombiesSeleccionados = new ArrayList<>();
    private Runnable stopMusicCallback;

    public VentanaPVP(JFrame parent, Runnable stopMusicCallback) {
        this.stopMusicCallback = stopMusicCallback;

        setTitle("Player vs Player");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);

        FondoPanel fondo = new FondoPanel("/presentation/images/windows/ventanapvp.png");
        fondo.setLayout(new BorderLayout());

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

        JPanel tiempoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        tiempoPanel.setOpaque(false);
        JLabel tiempoLabel = new JLabel("Match Duration (min):");
        tiempoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        duracionPartida = new JSpinner(new SpinnerNumberModel(5, 1, 60, 1));
        tiempoPanel.add(tiempoLabel);
        tiempoPanel.add(duracionPartida);

        JPanel panelPlayerPlants = createPlayerPanel("Player 1 (Plants):", true);
        JPanel panelPlayerZombies = createPlayerPanel("Player 2 (Zombies):", false);

        JPanel playersPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        playersPanel.setOpaque(false);
        playersPanel.add(panelPlayerPlants);
        playersPanel.add(panelPlayerZombies);

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

        List<String> elementos = isPlant ? getPlantasDisponibles() : getZombiesDisponibles();
        for (String elementoPath : elementos) {
            JButton boton = createImageButton(elementoPath, isPlant);
            selectionPanel.add(boton);
        }

        panel.add(configPanel, BorderLayout.NORTH);
        panel.add(selectionPanel, BorderLayout.CENTER);
        return panel;
    }

    private JButton createImageButton(String imagePath, boolean isPlant) {
        JButton boton = new JButton();
        boton.setOpaque(false);
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);

        try {
            ImageIcon icono = new ImageIcon(getClass().getResource(imagePath));

            boton.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    int width = boton.getWidth();
                    int height = boton.getHeight();
                    if (width > 0 && height > 0) {
                        Image imagenEscalada = icono.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        boton.setIcon(new ImageIcon(imagenEscalada));
                    }
                }
            });
        } catch (Exception e) {
            boton.setText("Error");
        }

        boton.addActionListener(e -> toggleSelection(boton, imagePath, isPlant));
        return boton;
    }

    private void toggleSelection(JButton boton, String imagePath, boolean isPlant) {
        List<String> seleccionados = isPlant ? plantasSeleccionadas : zombiesSeleccionados;
        if (seleccionados.contains(imagePath)) {
            seleccionados.remove(imagePath);
            boton.setBorderPainted(false);
            boton.setOpaque(false);
        } else {
            seleccionados.add(imagePath);
            boton.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
            boton.setOpaque(true);
        }
    }

    private List<String> getPlantasDisponibles() {
        return List.of(
            "/presentation/images/images_Plants/girasolcarta.png",
            "/presentation/images/images_Plants/tiracarta.png",
            "/presentation/images/images_Plants/nuescarta.png",
            "/presentation/images/images_Plants/pumcarta.png",
            "/presentation/images/images_Plants/ecicarta.png"
        );
    }

    private List<String> getZombiesDisponibles() {
        return List.of(
            "/presentation/images/images_Zombies/basicocarta.png",
            "/presentation/images/images_Zombies/conocarta.png",
            "/presentation/images/images_Zombies/cubetacarta.png",
            "/presentation/images/images_Zombies/cerebrocarta.png",
            "/presentation/images/images_Zombies/bombacarta.png"
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
