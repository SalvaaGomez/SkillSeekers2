package pantallas;

//Importo SWING

import pantallas.menu.MenuApp;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;

//Importo AWT
import java.awt.Image;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

/**
 * La clase {@code Carga} representa una pantalla de carga con una barra de progreso
 * que simula el inicio de una aplicación. Muestra una imagen, una barra de progreso
 * y al finalizar la carga, abre una nueva ventana de la interfaz de usuario principal.
 * <p>
 * Extiende {@code JFrame} y gestiona la lógica de la pantalla de carga y el
 * cambio de ventana.
 */
public class Carga extends JFrame {
    private JProgressBar barraProgreso;
    private Timer timer; // Temporizador para actualizar el progreso
    private int valorActual = 0; // Valor actual de la barra de progreso

    /**
     * Constructor de la clase {@code Carga}. Configura el marco y el panel,
     * y comienza el proceso de carga con la barra de progreso.
     *
     * @throws IOException si ocurre un error al cargar las imágenes.
     */
    public Carga() {
        //Configuro el Frame y el Panel
        try {
            configurarFrame();
            add(configuroPanel());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        empezarCarga(); // Inicio la barra de progreso
        setVisible(true);
    }

    /**
     * Inicia la simulación de la carga. Utiliza un temporizador para actualizar
     * el valor de la barra de progreso cada 40 milisegundos. Cuando la barra
     * alcanza el 100%, se detiene y se abre la interfaz gráfica de usuario (GUI).
     */
    private void empezarCarga() {
        timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valorActual += 2;
                barraProgreso.setValue(valorActual);
                if (valorActual >= 100) {
                    timer.stop();
                    try {
                        new GUI();
                        dispose(); //Elimino el Frame
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        timer.start();
    }

    /**
     * Configura el marco de la ventana de carga, incluyendo título, tamaño e icono.
     *
     * @throws IOException si ocurre un error al cargar el icono de la ventana.
     */
    private void configurarFrame() throws IOException {
        //Configuraciones Frame
        setTitle("Skill Seekers");
        setSize(600, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Centro la ventana en el centro
        setUndecorated(true);
        //Cargar imagen icono
        setIconImage(ImageIO.read(Objects.requireNonNull(MenuApp.class.getResource("/images/icon.png"))));
    }

    /**
     * Configura el panel principal de la ventana de carga. Agrega una imagen y la
     * barra de progreso al panel.
     *
     * @return un {@link JPanel} que contiene los componentes de la pantalla de carga.
     * @throws IOException si ocurre un error al cargar el logo.
     */
    private JPanel configuroPanel() throws IOException {
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS)); // Layout vertical
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(60, 20, 0, 20));  // Margen
        panelPrincipal.setBackground(new Color(83, 83, 83));

        // Cargo y redimensiono la imagen
        Image icon = ImageIO.read(Objects.requireNonNull(MenuApp.class.getResource("/images/title.png")));
        Image icon_reducido = icon.getScaledInstance(icon.getWidth(null) / 7, icon.getHeight(null) / 7, Image.SCALE_AREA_AVERAGING);
        ImageIcon icono = new ImageIcon(icon_reducido);

        // Creo un JLabel con la imagen
        JLabel etiquetaImagen = new JLabel(icono);
        etiquetaImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPrincipal.add(etiquetaImagen);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 100))); // Espacio vació entre la imagen y la barra

        configuroProgressBar(); // Configuro la barra de progreso
        panelPrincipal.add(barraProgreso); // Agrego la barra de progreso al panel
        return panelPrincipal;
    }

    /**
     * Configura la barra de progreso, incluyendo sus colores y tamaño.
     */
    private void configuroProgressBar() {
        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setBackground(Color.white);
        barraProgreso.setForeground(new Color(163, 184, 204));
        barraProgreso.setStringPainted(false); // No muestro el porcentaje en la barra
        barraProgreso.setMaximumSize(new Dimension(350, 20));
        barraProgreso.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
