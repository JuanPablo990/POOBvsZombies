package presentation;

import javax.swing.*;
import java.awt.*;

public class FondoPanel extends JPanel {
    private Image imagen;

    public FondoPanel() {
        this("/images/menu.png");
    }

    public FondoPanel(String imagePath) {
        try {
            this.imagen = new ImageIcon(getClass().getResource(imagePath)).getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen de fondo: " + imagePath);
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
