package game;

import idiomas.Idioma;
import pantallas.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que maneja la visualización de diálogos en el panel del juego.
 * Permite mostrar texto de diálogo y opciones interactivas para el jugador.
 */
public class Dialogos extends JPanel {

    private String textoDialogo;
    private int numNPC;

    private JButton seleccionActual;
    private final List<JButton> respuestas = new ArrayList<>();
    int panelWidth = GUI.getAnchoPantalla() / 2;
    int panelHeight = GUI.getAltoPantalla() / 4;

    /**
     * Constructor de la clase Dialogos.
     * Inicializa el panel con un diseño de caja vertical y establece su tamaño y visibilidad.
     */
    public Dialogos() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        int boxX = (GUI.getAnchoPantalla() - panelWidth) / 2;
        int boxY = GUI.getAltoPantalla() - panelHeight - 80;

        this.setBounds(boxX, boxY, panelWidth, panelHeight);

        this.setBackground(new Color(0, 0, 0, 0));
        this.setVisible(false);
        setearBotones();
        revalidate();
    }

    /**
     * Establece el texto del diálogo y lo prepara para ser mostrado en el panel.
     *
     * @param textoDialogo El texto que se mostrará en el cuadro de diálogo.
     */
    public void setTextoDialogo(String textoDialogo) {
        this.seleccionActual = null;
        this.textoDialogo = textoDialogo;
        this.removeAll();
        revalidate();
    }

    /**
     * Establece las opciones de respuesta que el jugador puede seleccionar durante el diálogo.
     * Las opciones son aleatorizadas.
     *
     * @param opciones Una lista de opciones de respuesta para el jugador.
     */
    public void setOpciones(List<String> opciones) {
        respuestas.clear();
        for (String opcion : opciones) {
            respuestas.add(new JButton(opcion));
        }
        Collections.shuffle(respuestas);
        setearBotones();
        repaint();
        revalidate();
    }

    /**
     * Establece el número de NPC con el que el jugador está interactuando.
     *
     * @param numNPC El número del NPC.
     */
    public void setNumNPC(int numNPC) {
        this.numNPC = numNPC;
    }

    /**
     * Obtiene el botón de la opción seleccionada por el jugador.
     *
     * @return El botón de la opción seleccionada.
     */
    public JButton getSeleccionActual() {
        return seleccionActual;
    }

    /**
     * Configura los botones de las opciones de respuesta en el panel de diálogo.
     * Añade los botones con el texto de cada opción y asigna la acción de selección.
     */
    private void setearBotones() {
        if (!respuestas.isEmpty()) {
            this.add(Box.createVerticalGlue());
            JPanel pbo = new JPanel();
            pbo.setLayout(new BoxLayout(pbo, BoxLayout.X_AXIS));
            pbo.setBackground(new Color(0, 0, 0, 0));
            pbo.add(Box.createRigidArea(new Dimension(20, 0)));
            pbo.setVisible(true);
            for (JButton boton : respuestas) {
                boton.setAlignmentY(Component.CENTER_ALIGNMENT);
                boton.setMaximumSize(new Dimension(400, 100));
                boton.setFont(new Font("Arial", Font.PLAIN, 15));
                boton.setForeground(Color.white);
                boton.setBackground(new Color(0, 0, 0, 0));
                boton.setBorder(BorderFactory.createLineBorder(Color.white, 2, false));
                boton.setFocusPainted(false);
                boton.setVisible(true);
                pbo.add(boton);

                pbo.add(Box.createRigidArea(new Dimension(20, 0)));
                boton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (seleccionActual != null) {
                            seleccionActual.setBackground(new Color(0, 0, 0, 0));
                            seleccionActual.setForeground(Color.white);
                        }
                        boton.setForeground(Color.black);
                        boton.setBackground(new Color(132, 255, 251));
                        seleccionActual = boton;
                    }
                });
            }

            this.add(pbo);
            add(Box.createRigidArea(new Dimension(0, 60)));
            revalidate();
        }
    }

    /**
     * Sobrescribe el método paintComponent para dibujar el cuadro de diálogo y sus opciones.
     *
     * @param g El objeto Graphics usado para dibujar en el panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        if (mostrarDialogo) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar el rectángulo redondeado
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(0, 0, panelWidth - 2, panelHeight - 2, 20, 20);

        // Dibujar el borde del rectángulo redondeado
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(0, 0, panelWidth - 2, panelHeight - 2, 20, 20);

        // Configurar fuente
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        FontMetrics fm = g2d.getFontMetrics();
        int lineHeight = fm.getHeight();

        // Dibujar el número del NPC en la parte superior del cuadro
        String npcLabel;
        if (respuestas.isEmpty()) {
            if (numNPC != -1) {
                npcLabel = Idioma.getRb().getString("NPC") + " #" + (numNPC + 1);
            } else {
                npcLabel = Idioma.getRb().getString("Sabio");
            }
        } else {
            if (numNPC != -1) {
                npcLabel = "Bug" + " #" + (numNPC + 1);
            } else {
                npcLabel = "Bugger";
            }
        }
        int npcLabelWidth = fm.stringWidth(npcLabel);
        int npcLabelX = (panelWidth - npcLabelWidth) / 2;
        int npcLabelY = fm.getAscent() + 10; // 10px de margen desde la parte superior
        g2d.setColor(Color.YELLOW); // Color del número del NPC
        g2d.drawString(npcLabel, npcLabelX, npcLabelY);

        // Dividir el texto por los saltos de línea explícitos
        String[] manualLines = textoDialogo.split("\n");
        List<String> finalLines = new ArrayList<>();

        int maxLineWidth = panelWidth - 80; // Ancho máximo permitido dentro del cuadro (con margen)

        for (String paragraph : manualLines) {
            StringBuilder currentLine = new StringBuilder();
            int currentLineWidth = 0; // Ancho acumulado de la línea actual
            for (String word : paragraph.split(" ")) {
                if (word.contains("\t")) {
                    // Manejar tabulaciones dentro de la palabra
                    word = word.replace("\t", "    ");
                }
                int wordWidth = fm.stringWidth(word + " "); // Ancho de la palabra con espacio
                if (currentLineWidth + wordWidth <= maxLineWidth) {
                    currentLine.append(word).append(" ");
                    currentLineWidth += wordWidth;
                } else {
                    // Línea completa, agregarla y comenzar una nueva
                    finalLines.add(currentLine.toString().trim());
                    currentLine = new StringBuilder(word).append(" ");
                    currentLineWidth = wordWidth;
                }
            }
            // Agregar cualquier texto restante de la línea actual
            if (!currentLine.toString().isEmpty()) {
                finalLines.add(currentLine.toString().trim());
            }
        }
        // Dibujar las líneas desde el margen izquierdo
        int startX = 40; // 20px de margen desde el lado izquierdo
        int startY = 50; // Comenzar debajo del título del NPC
        for (int i = 0; i < finalLines.size(); i++) {
            String line = finalLines.get(i);
            int textY = startY + (i * lineHeight);
            g2d.setColor(Color.WHITE);
            g2d.drawString(line, startX, textY);
        }
        String exitMessage;
        if (respuestas.isEmpty()) {
            exitMessage = Idioma.getRb().getString("continuar");
        } else {
            exitMessage = (Idioma.getRb().getString("validar"));
        }
        int exitTextWidth = fm.stringWidth(exitMessage);
        int exitTextX = (panelWidth - exitTextWidth) / 2;
        int exitTextY = panelHeight + fm.getHeight() - 40;
        g2d.drawString(exitMessage, exitTextX, exitTextY);
    }
}

//}


