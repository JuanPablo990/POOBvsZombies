package presentation;

import javax.swing.*;
import java.awt.*;

public class VentanaPvsM extends JFrame {
    public VentanaPvsM(JFrame parent) {
        super("Player vs Machine");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        FondoPanel fondo = new FondoPanel("/presentation/images/ventana1.png");
        setContentPane(fondo);
        setLayout(null);

        JPanel panelTransparente = new JPanel();
        panelTransparente.setBounds(30, 30, 340, 300);
        panelTransparente.setBackground(new Color(255, 255, 255, 200));
        panelTransparente.setLayout(null);
        add(panelTransparente);

        JLabel labelNombre = new JLabel("Nombre del jugador:");
        labelNombre.setBounds(20, 20, 150, 30);
        labelNombre.setFont(new Font("Arial", Font.BOLD, 14));
        panelTransparente.add(labelNombre);

        JTextField textNombre = new JTextField();
        textNombre.setBounds(180, 20, 140, 30);
        panelTransparente.add(textNombre);

        JLabel labelRol = new JLabel("¿Qué quieres ser?");
        labelRol.setBounds(20, 70, 150, 30);
        labelRol.setFont(new Font("Arial", Font.BOLD, 14));
        panelTransparente.add(labelRol);

        JRadioButton radioPlanta = new JRadioButton("Planta");
        radioPlanta.setBounds(180, 70, 70, 30);
        panelTransparente.add(radioPlanta);

        JRadioButton radioZombi = new JRadioButton("Zombi");
        radioZombi.setBounds(250, 70, 70, 30);
        panelTransparente.add(radioZombi);

        ButtonGroup grupoRol = new ButtonGroup();
        grupoRol.add(radioPlanta);
        grupoRol.add(radioZombi);

        JLabel labelTiempo = new JLabel("Tiempo de partida (min):");
        labelTiempo.setBounds(20, 120, 200, 30);
        labelTiempo.setFont(new Font("Arial", Font.BOLD, 14));
        panelTransparente.add(labelTiempo);

        JTextField textTiempo = new JTextField();
        textTiempo.setBounds(180, 120, 140, 30);
        panelTransparente.add(textTiempo);

        JLabel labelHordas = new JLabel("Cantidad de hordas:");
        labelHordas.setBounds(20, 170, 150, 30);
        labelHordas.setFont(new Font("Arial", Font.BOLD, 14));
        panelTransparente.add(labelHordas);

        JTextField textHordas = new JTextField();
        textHordas.setBounds(180, 170, 140, 30);
        panelTransparente.add(textHordas);

        JButton botonEmpezar = new JButton("Empezar a Jugar");
        botonEmpezar.setBounds(90, 230, 150, 40);
        panelTransparente.add(botonEmpezar);

        botonEmpezar.addActionListener(e -> {
            String nombreJugador = textNombre.getText();
            String rol = radioPlanta.isSelected() ? "Planta" : radioZombi.isSelected() ? "Zombi" : "";
            String tiempoTexto = textTiempo.getText();
            String hordasTexto = textHordas.getText();

            if (nombreJugador.isEmpty() || rol.isEmpty() || tiempoTexto.isEmpty() || hordasTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.");
            } else {
                try {
                    int tiempo = Integer.parseInt(tiempoTexto);
                    int hordas = Integer.parseInt(hordasTexto);
                    if (tiempo <= 0 || hordas <= 0) {
                        JOptionPane.showMessageDialog(this, "El tiempo y las hordas deben ser mayores a 0.");
                    } else {
                        dispose();
                        new VentanaJuego(nombreJugador, rol, tiempo, hordas);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Por favor, ingresa valores válidos.");
                }
            }
        });

        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
