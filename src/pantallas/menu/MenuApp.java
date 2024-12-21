package pantallas.menu;

import idiomas.Idioma;
import pantallas.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * La clase {@code MenuApp} representa el panel principal del menú de la aplicación.
 * Contiene botones para navegar entre las opciones del menú, como iniciar un nuevo juego,
 * cargar un juego guardado, acceder a configuraciones y salir de la aplicación.
 *
 * Extiende {@code JPanel} y es parte de la interfaz gráfica de la aplicación.
 */
public class MenuApp extends JPanel {
    private final JPanel panelBotones = new JPanel(new BorderLayout()); // Panel central donde se colocan los botones
    private Image image; // Imagen de fondo para el menú
    private GUI gui; // Instancia de la interfaz gráfica principal (GUI)

    private JButton newGameButton; // Botón para iniciar un nuevo juego
    private JButton loadGameButton; // Botón para cargar un juego guardado
    private JButton settingsButton; // Botón para acceder a configuraciones
    private JButton exitButton; // Botón para salir de la aplicación

    /**
     * Establece la referencia a la instancia de la interfaz principal {@code GUI}.
     *
     * @param gui la instancia de {@code GUI}.
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Constructor de la clase {@code MenuApp}. Configura el panel y los botones
     * de la interfaz de usuario del menú principal.
     *
     * @param window la ventana principal donde se añadirá este panel.
     */
    public MenuApp(JFrame window) {
        configurarPanel(); // Configura el panel principal
        configurarBotones(window); // Configura los botones del menú
        accionesBotones(window); // Asocia acciones a los botones
        setImage("Menu");  // Establece la imagen de fondo del menú
        add(panelBotones, BorderLayout.CENTER); // Añade el panel central al panel principal
        repaint();
    }

    /**
     * Configura el layout y los márgenes del panel principal del menú.
     */
    private void configurarPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }

    /**
     * Configura los botones del menú principal, ajustando sus textos y añadiéndolos
     * al panel central.
     *
     * @param window la ventana principal donde se colocarán los botones.
     */
    private void configurarBotones(JFrame window) {
        // Botones con los textos correspondientes en el idioma seleccionado
        newGameButton = crearBoton(Idioma.getRb().getString("New_Game_Btn"));
        loadGameButton = crearBoton(Idioma.getRb().getString("Load_Game_Btn"));
        settingsButton = crearBoton(Idioma.getRb().getString("Setting_Btn"));
        exitButton = crearBoton(Idioma.getRb().getString("Exit_Btn"));
        //Panel de Botones
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setBackground(new Color(0, 0, 0, 0));
        //Configuracion de botones
        JButton[] botones = {newGameButton, loadGameButton, settingsButton, exitButton};
        for (JButton boton : botones) {
            panelBotones.add(boton);
            panelBotones.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        // Establece un margen superior en el panel de botones
        panelBotones.setBorder(BorderFactory.createEmptyBorder(window.getHeight() / 3 + 50, 0, 0, 0));
    }

    /**
     * Crea un botón con un texto específico y lo configura con algunos parámetros visuales.
     *
     * @param texto el texto que aparecerá en el botón.
     * @return el botón creado y configurado.
     */
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT); // Alinea el botón al centro
        boton.setMaximumSize(new Dimension(200, 50));
        boton.setFont(new Font("Arial", Font.PLAIN, 18));
        boton.setFocusable(false); // El botón no puede ser enfocado
        boton.setBackground(Color.white);
        return boton;
    }
    /**
     * Asocia las acciones a cada botón del menú.
     *
     * @param window la ventana principal donde se actualizarán los componentes.
     */
    private void accionesBotones(JFrame window) {
        newGameButton.addActionListener(e -> mostarNewGame(window)); //Boton Nuevo Juego
        loadGameButton.addActionListener(e -> mostrarLoadGame(window)); //Boton Cargar Juego
        settingsButton.addActionListener(e -> mostrarSettings(window)); //Boton Ajustes
        exitButton.addActionListener(e -> System.exit(0)); //Boton Salir
    }

    /**
     * Muestra el panel para iniciar un nuevo juego.
     *
     * @param window la ventana principal donde se actualizarán los componentes.
     */
    private void mostarNewGame(JFrame window) {
        panelBotones.setVisible(false); // Oculta el panel de botones
        try {
            gui.getNewgame().cargarNewGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        add(gui.getNewgame(), BorderLayout.CENTER);
        repaint();
        revalidate();
        window.repaint();
    }

    /**
     * Muestra el panel de configuraciones.
     *
     * @param window la ventana principal donde se actualizarán los componentes.
     */
    private void mostrarSettings(JFrame window) {
        panelBotones.setVisible(false); // Oculta el panel de botones
        setImage("Ajustes");
        gui.getSettings().setVisible(true);
        add(gui.getSettings(), BorderLayout.CENTER);
        repaint();
        revalidate();
        window.repaint();
    }

    /**
     * Muestra el panel para cargar un juego guardado.
     *
     * @param window la ventana principal donde se actualizarán los componentes.
     */
    private void mostrarLoadGame(JFrame window) {
        panelBotones.setVisible(false); // Oculta el panel de botones
        try {
            gui.getLoadgame().cargarLoadGame();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        add(gui.getLoadgame(), BorderLayout.CENTER);
        repaint();
        revalidate();
        window.repaint();
    }

    /**
     * Carga y actualiza los textos de los botones del menú según el idioma seleccionado.
     *
     * @throws IOException si ocurre un error al cargar la imagen de fondo.
     */
    public void cargarMenu() throws IOException {
        setImage("Menu");
        panelBotones.setVisible(true);
        newGameButton.setText((Idioma.getRb().getString("New_Game_Btn")));
        loadGameButton.setText((Idioma.getRb().getString("Load_Game_Btn")));
        settingsButton.setText((Idioma.getRb().getString("Setting_Btn")));
        exitButton.setText((Idioma.getRb().getString("Exit_Btn")));
    }

    /**
     * Establece la imagen de fondo del panel según la sección que se está mostrando.
     *
     * @param fondo el nombre de la sección para la que se debe cambiar la imagen de fondo.
     */
    void setImage(String fondo) {
        try {
            switch (fondo) {
                case "Ajustes":
                    image = ImageIO.read(Objects.requireNonNull(MenuApp.class.getResource(Idioma.getRb().getString("URL_Settings"))));
                    break;
                case "Juego":
                    image = ImageIO.read(Objects.requireNonNull(MenuApp.class.getResource(Idioma.getRb().getString("URL_NewGame"))));
                    break;
                default:
                    image = ImageIO.read(Objects.requireNonNull(MenuApp.class.getResource("/images/FondoMenu.jpg")));
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Dibuja la imagen de fondo en el panel de menú.
     *
     * @param g el objeto {@code Graphics} utilizado para dibujar la imagen.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), panelBotones);
    }

}
