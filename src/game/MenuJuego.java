package game;

import idiomas.Idioma;
import pantallas.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
/**
 * Clase que representa el menú de pausa del juego, donde el jugador puede elegir entre
 * regresar al juego, ir al menú principal o guardar y salir del juego.
 */
public class MenuJuego extends JPanel {
    /**
     * Constructor de la clase MenuJuego.
     *
     * @param volverAction Acción a ejecutar al presionar el botón "Volver".
     * @param menuPrincipalAction Acción a ejecutar al presionar el botón "Menú Principal".
     * @param guardarSalirAction Acción a ejecutar al presionar el botón "Guardar y Salir".
     */
    public MenuJuego(ActionListener volverAction, ActionListener menuPrincipalAction, ActionListener guardarSalirAction) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBounds(GUI.getAnchoPantalla() / 3, GUI.getAltoPantalla() / 3, GUI.getAnchoPantalla() / 3, GUI.getAltoPantalla() / 3);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setVisible(false);
        this.add(Box.createVerticalGlue());
        JLabel s = new JLabel(Idioma.getRb().getString("pausa"));

        s.setAlignmentX(Component.CENTER_ALIGNMENT);
        s.setFont(new Font("Arial", Font.BOLD, 25));
        s.setForeground(Color.WHITE);
        add(s);
        JButton btnVolver = new JButton(Idioma.getRb().getString("volver"));
        JButton btnMenuPrincipal = new JButton(Idioma.getRb().getString("menu"));
        JButton btnGuardarSalir = new JButton(Idioma.getRb().getString("guardarSalir"));
        JButton[] buttons = {btnVolver, btnMenuPrincipal, btnGuardarSalir};


        for (JButton boton : buttons) {
            boton.setAlignmentX(Component.CENTER_ALIGNMENT);
            boton.setPreferredSize(new Dimension(400, 50));
            boton.setFont(new Font("Arial", Font.PLAIN, 18));
            boton.setBackground(Color.white);
            boton.setFocusPainted(false);
            add(Box.createRigidArea(new Dimension(0, 20)));
            add(boton);
        }

        this.add(Box.createVerticalGlue());

        btnVolver.addActionListener(volverAction);
        btnMenuPrincipal.addActionListener(menuPrincipalAction);
        btnGuardarSalir.addActionListener(guardarSalirAction);

    }
    /**
     * Método para dibujar el fondo y los bordes del menú de pausa.
     *
     * @param g El objeto Graphics que se utiliza para pintar en el panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Obtener las dimensiones del panel
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Dibujar el rectángulo redondeado
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(1, 1, panelWidth - 2, panelHeight - 2, 20, 20);

        // Dibujar el borde del rectángulo redondeado
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(1, 1, panelWidth - 2, panelHeight - 2, 20, 20);
    }
}
