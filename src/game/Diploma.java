package game;

import idiomas.Idioma;
import pantallas.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

/**
 * Clase que maneja la pantalla de diplomas, donde el jugador puede ver su diploma,
 * guardarlo o regresar al menú principal.
 */
public class Diploma extends JPanel {
    int panelWidth = GUI.getAnchoPantalla();
    int panelHeight = GUI.getAltoPantalla();
    private BufferedImage finalDiploma;

    /**
     * Constructor de la clase Diploma.
     *
     * @param nombreJugador       Nombre del jugador que recibirá el diploma.
     * @param curso               El curso que el jugador ha completado.
     * @param menuPrincipalAction Acción que se ejecuta al regresar al menú principal.
     */
    public Diploma(String nombreJugador, String curso, ActionListener menuPrincipalAction) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        this.setBounds(0, 0, panelWidth, panelHeight);

        this.setBackground(new Color(0, 0, 0, 0));
        this.setOpaque(false);

        BufferedImage diplomaImage = cargarTemplate();

        if (diplomaImage != null) {
            add(Box.createVerticalGlue());

            finalDiploma = escribirDiploma(diplomaImage, nombreJugador, curso);
            JPanel pbo = new JPanel();
            pbo.setLayout(new BoxLayout(pbo, BoxLayout.X_AXIS));
            pbo.setVisible(true);
            pbo.setBackground(new Color(0, 0, 0, 0));
            pbo.add(Box.createRigidArea(new Dimension(20, 0)));


            JButton b1 = new JButton((Idioma.getRb().getString("guardarCert")));
            JButton b2 = new JButton((Idioma.getRb().getString("guardarSalir")));
            JButton[] botones = {b1, b2};
            for (JButton boton : botones) {
                boton.setAlignmentY(Component.CENTER_ALIGNMENT);
                boton.setPreferredSize(new Dimension(400, 75));
                boton.setMinimumSize(new Dimension(400, 75));
                boton.setMaximumSize(new Dimension(400, 75));

                boton.setFont(new Font("Arial", Font.PLAIN, 15));
                boton.setForeground(Color.white);
                boton.setBackground(new Color(0, 0, 0, 0));
                boton.setBorder(BorderFactory.createLineBorder(Color.white, 2, false));
                boton.setFocusPainted(false);
                boton.setVisible(true);
                pbo.add(boton);

                pbo.add(Box.createRigidArea(new Dimension(20, 0)));

            }
            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    guardarDiploma(finalDiploma, nombreJugador, curso);
                }
            });
            b2.addActionListener(menuPrincipalAction);

            this.add(pbo);
            add(Box.createRigidArea(new Dimension(0, 60)));
            revalidate();
            add(Box.createVerticalStrut(75));


        }
    }

    /**
     * Carga la imagen de plantilla del diploma.
     *
     * @return La imagen de plantilla cargada, o null si ocurre un error al cargarla.
     */
    public BufferedImage cargarTemplate() {
        try {
            return ImageIO.read(Objects.requireNonNull(Diploma.class.getResource((Idioma.getRb().getString("URLDiploma")))));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Escribe el nombre del jugador y el curso en la plantilla del diploma.
     *
     * @param template Imagen de plantilla donde se escribirá el texto.
     * @param nombre   Nombre del jugador que aparece en el diploma.
     * @param curso    Curso que el jugador ha completado.
     * @return La nueva imagen con el texto del diploma.
     */
    public BufferedImage escribirDiploma(BufferedImage template, String nombre, String curso) {

        BufferedImage newImage = new BufferedImage(
                template.getWidth(),
                template.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = newImage.createGraphics();

        ((Graphics2D) g).drawImage(template, 0, 0, null);
        Graphics2D g2d = (Graphics2D) g;


        g.setFont(new Font("Arial", Font.BOLD, 66));
        g.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics();
        int labelwith = fm.stringWidth(nombre);

        g.drawString(nombre, (template.getWidth() - labelwith) / 2, template.getHeight() / 2 - 60);
        g.drawString(curso, (template.getWidth() - labelwith) / 2, template.getHeight() / 2 + 160);

        g.dispose();

        return newImage;
    }

    /**
     * Permite al usuario guardar el diploma en una ubicación seleccionada en su ordenador.
     *
     * @param image         Imagen del diploma que se guardará.
     * @param nombreJugador Nombre del jugador que aparece en el diploma.
     * @param curso         El curso que ha completado el jugador.
     */
    public void guardarDiploma(BufferedImage image, String nombreJugador, String curso) {
        FileDialog fileDialog = new FileDialog((Frame) null, (Idioma.getRb().getString("guardarCert")), FileDialog.SAVE);

        // Configurar la extensión sugerida
        fileDialog.setFile("SKillSeekersDiploma_" + nombreJugador + "_" + curso + ".png");
        // Mostrar el cuadro de diálogo
        fileDialog.setVisible(true);

        // Obtener la ruta y el nombre seleccionados por el usuario
        String directory = fileDialog.getDirectory();
        String fileName = fileDialog.getFile();

        if (directory != null && fileName != null) {
            // Construir la ruta completa
            File fileToSave = new File(directory, fileName);

            // Asegurarse de que el archivo tenga la extensión correcta
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.endsWith(".png")) {
                filePath += ".png";
            }

            // Guardar la imagen
            try {
                ImageIO.write(image, "png", new File(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método para pintar el componente gráfico que representa la pantalla del diploma.
     *
     * @param g Objeto Graphics que se usa para dibujar en la pantalla.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        if (mostrarDialogo) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar el rectángulo redondeado
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(100, 100, panelWidth - 200, panelHeight - 200, 20, 20);

        // Dibujar el borde del rectángulo redondeado
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(100, 100, panelWidth - 200, panelHeight - 200, 20, 20);

        // Configurar fuente
        g2d.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics fm = g2d.getFontMetrics();
        int lineHeight = fm.getHeight();

        // Dibujar el número del NPC en la parte superior del cuadro
        String npcLabel;
        npcLabel = (Idioma.getRb().getString("fin"));
        int npcLabelWidth = fm.stringWidth(npcLabel);
        int npcLabelX = (panelWidth - npcLabelWidth) / 2;
        int npcLabelY = fm.getAscent() + 150; // 10px de margen desde la parte superior
        g2d.setColor(Color.WHITE); // Color del número del NPC
        g2d.drawString(npcLabel, npcLabelX, npcLabelY);

        // Dividir el texto por los saltos de línea explícitos
        g2d.drawImage(finalDiploma, panelWidth / 2 - finalDiploma.getWidth() / 4, panelHeight / 2 - finalDiploma.getHeight() / 4, finalDiploma.getWidth() / 2, finalDiploma.getHeight() / 2, null);

        revalidate();
        repaint();

    }
}
