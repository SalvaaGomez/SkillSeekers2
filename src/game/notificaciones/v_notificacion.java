package game.notificaciones;

import idiomas.Idioma;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que maneja la visualización de notificaciones en el panel del juego.
 * Esta clase extiende JPanel y muestra diferentes tipos de notificaciones,
 * como notificaciones temporales y mensajes relacionados con la interacción del jugador.
 */
public class v_notificacion extends JPanel {
    private boolean mostrarNotificacion = false;
    private boolean notiTemporal = false;
    private String accion = "";
    private String aviso = "";

    /**
     * Muestra una notificación cuando el jugador entra a una casa.
     *
     * @param id El identificador de la casa que el jugador está entrando.
     */
    public void mostrarNotificacionEntrarCasa(int id) {
        this.mostrarNotificacion = true;
        accion = (Idioma.getRb().getString("entrarCabaña")) + " #" + id;
        repaint();
    }

    /**
     * Muestra una notificación cuando el jugador sale de una casa.
     *
     * @param mostrar Si es true, muestra la notificación de salida; de lo contrario, la oculta.
     */
    public void mostrarNotificacionSalirCasa(boolean mostrar) {
        this.mostrarNotificacion = mostrar;
        accion = (Idioma.getRb().getString("salirCabaña"));
        repaint();
    }

    /**
     * Muestra una notificación cuando el jugador entra a una mazmorras.
     *
     * @param mostrar Si es true, muestra la notificación de entrada a la mazmorras; de lo contrario, la oculta.
     */
    public void mostrarNotificacionEntrarMazmorra(boolean mostrar) {
        this.mostrarNotificacion = mostrar;
        accion = (Idioma.getRb().getString("entrarMazmorra"));
        repaint();
    }

    /**
     * Muestra una notificación cuando el jugador sale de una mazmorras.
     *
     * @param mostrar Si es true, muestra la notificación de salida; de lo contrario, la oculta.
     */
    public void mostrarNotificacionSalirMazmorra(boolean mostrar) {
        this.mostrarNotificacion = mostrar;
        accion = (Idioma.getRb().getString("salirMazmorra"));
        repaint();
    }

    /**
     * Muestra una notificación cuando el jugador interactúa con un NPC.
     *
     * @param id   El identificador del NPC con el que el jugador está interactuando.
     * @param tipo El tipo de NPC (por ejemplo, aldeano, sabio, etc.) que determina el mensaje.
     */
    public void mostrarNotificacionConNPC(int id, String tipo) {
        this.mostrarNotificacion = true;
        switch (tipo) {
            case "aldeano":
                accion = (Idioma.getRb().getString("interactuarNPC")) + " #" + (id + 1);
                break;
            case "bug":
                accion = (Idioma.getRb().getString("interactuarBug")) + " #" + (id + 1);
                break;
            case "sabio":
                accion = (Idioma.getRb().getString("interactuarSabio"));
                break;
            case "bugger":
                accion = (Idioma.getRb().getString("interactuarBugger"));
                break;
        }
        repaint();
    }

    /**
     * Muestra una notificación temporal en el panel, la cual se oculta después de un tiempo.
     *
     * @param texto  El texto que se mostrará en la notificación temporal.
     * @param tiempo El tiempo (en segundos) que la notificación temporal permanecerá visible.
     */
    public void mostrarNotiTemporal(String texto, int tiempo) {
        new Thread(() -> {
            this.notiTemporal = true;
            aviso = texto;
            repaint();
            try {
                Thread.sleep((long) 1000 * tiempo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            notiTemporal = false;
            repaint();
        }).start();
    }

    /**
     * Oculta la notificación actual.
     */
    public void noMostrarNotificacion() {
        this.mostrarNotificacion = false;
    }

    /**
     * Dibuja las notificaciones en el panel.
     *
     * @param g El objeto Graphics usado para dibujar en el panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mostrarNotificacion || notiTemporal) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Obtener las dimensiones del panel
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            // Dimensiones del cuadro de notificación
            int boxWidth = 300;
            int boxHeight = 50;
            int boxX = 0;
            int boxY = 0;
            if (!notiTemporal) {
                // Calcular las coordenadas para que el cuadro esté centrado en la parte inferior
                boxX = (panelWidth - boxWidth) / 2; // Centrado horizontal
                boxY = panelHeight - boxHeight - 20; // Parte inferior con un margen de 20 píxeles
            } else {

                boxX = (panelWidth - boxWidth) - 20; // Centrado horizontal
                boxY = boxHeight + 40; // Parte inferior con un margen de 20 píxeles
            }
            // Dibujar el rectángulo redondeado
            g2d.setColor(new Color(0, 0, 0, 150)); // Fondo negro translúcido
            g2d.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

            // Dibujar el borde del rectángulo redondeado
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);
            String message = "";
            String highlight = "";
            String rest = "";
            if (!notiTemporal) {
                // Definir el mensaje y la letra resaltada
                message = (Idioma.getRb().getString("pulsa"));
                highlight = " E ";
                rest = accion;
            } else {
                rest = aviso;
            }

            // Configurar la fuente
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));

            // Medir el ancho del texto
            FontMetrics fm = g2d.getFontMetrics();

            // Calcular el ancho total del texto
            int messageWidth = fm.stringWidth(message);
            int highlightWidth = fm.stringWidth(highlight);
            int restWidth = fm.stringWidth(rest);
            int totalWidth = messageWidth + highlightWidth + restWidth;

            // Calcular la posición X para centrar el texto en el cuadro
            int textX = boxX + (boxWidth - totalWidth) / 2;
            // Posición Y para centrar el texto verticalmente
            int textY = boxY + (boxHeight - fm.getHeight()) / 2 + fm.getAscent();

            // Dibujar el texto normal
            g2d.setColor(Color.WHITE);
            g2d.drawString(message, textX, textY);

            // Dibujar la "E" resaltada en amarillo
            g2d.setColor(Color.WHITE); // Resaltar la letra "E"
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString(highlight, textX + messageWidth, textY);

            // Dibujar el resto del texto
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            g2d.drawString(rest, textX + messageWidth + highlightWidth, textY);
        }
    }


}
