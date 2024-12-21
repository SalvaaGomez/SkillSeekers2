package pantallas;

import game.Juego;
import pantallas.menu.LoadGame;
import pantallas.menu.MenuApp;
import pantallas.menu.NewGame;
import pantallas.menu.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * La clase {@code GUI} representa la interfaz gráfica principal de la aplicación,
 * gestionando los diferentes paneles del menú: (menú, nuevo juego, cargar juego, configuraciones)
 * y el panel principal donde se derarrolla el juego. Esta clase se encarga de configurar la ventana y de cambiar
 * entre los diferentes paneles según la interacción del usuario.
 * <p>
 * Extiende de {@code JFrame} para ser donde se pongan todos los paneles.
 */
public class GUI extends JFrame {

    private final MenuApp menu; // Panel de menú principal
    private final NewGame newgame; // Panel para iniciar un nuevo juego
    private final LoadGame loadgame; // Panel para cargar un juego guardado
    private final Settings settings; // Panel de configuraciones
    private static Juego juego; // Panel del juego

    private static int anchoPantalla; // Ancho de la pantalla
    private static int altoPantalla; // Alto de la pantalla

    /**
     * Devuelve el ancho de la pantalla.
     *
     * @return el ancho de la pantalla.
     */
    public static int getAnchoPantalla() {
        return anchoPantalla;
    }

    /**
     * Devuelve el alto de la pantalla.
     *
     * @return el alto de la pantalla.
     */
    public static int getAltoPantalla() {
        return altoPantalla;
    }

    /**
     * Devuelve el panel del menú principal.
     *
     * @return el panel {@code MenuApp}.
     */
    public MenuApp getMenu() {
        return menu;
    }

    /**
     * Devuelve el panel para cargar un juego.
     *
     * @return el panel {@code LoadGame}.
     */
    public LoadGame getLoadgame() {
        return loadgame;
    }

    /**
     * Devuelve el panel para iniciar un nuevo juego.
     *
     * @return el panel {@code NewGame}.
     */
    public NewGame getNewgame() {
        return newgame;
    }

    /**
     * Devuelve el panel de configuraciones.
     *
     * @return el panel {@code Settings}.
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Devuelve el panel del juego.
     *
     * @return el panel {@code Juego}.
     */
    public Juego getJuego() {
        return juego;
    }

    /**
     * Constructor de la clase {@code GUI}. Configura la ventana principal, inicializa
     * los diferentes paneles y agrega el panel del menú a la ventana.
     *
     * @throws IOException si ocurre un error al cargar los recursos (imágenes, etc.).
     */
    public GUI() throws IOException {
        // Configuraciones de la ventana
        confiracionFrame();

        //Inicializa los paneles de la interfaz
        settings = new Settings(this);
        newgame = new NewGame(this);
        loadgame = new LoadGame(this);
        menu = new MenuApp(this);

        //Establece la instancia de GUI en cada uno de los paneles
        menu.setGui(this);
        newgame.setGui(this);
        loadgame.setGui(this);
        settings.setGui(this);

        // Obtiene el tamaño de la pantalla
        anchoPantalla = this.getWidth();
        altoPantalla = this.getHeight();

        // Agrega el panel del menú a la ventana
        add(menu);
    }

    /**
     * Configura las propiedades de la ventana principal, como el título, tamaño, icono,
     * y otros parámetros relacionados con la apariencia de la ventana.
     *
     * @throws IOException si ocurre un error al cargar el icono de la ventana.
     */
    private void confiracionFrame() throws IOException {
        setTitle("Skill Seekers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setPreferredSize(new Dimension(1024, 768));
        setResizable(false);
        setLocationRelativeTo(null);
        Image logo = ImageIO.read(Objects.requireNonNull(MenuApp.class.getResource("/images/icon.png")));
        setIconImage(logo);
        setUndecorated(true);
        setVisible(true);
    }

    /**
     * Establece la instancia del juego en la clase {@code GUI}.
     *
     * @param j la instancia del juego {@code Juego} que se establecerá.
     */
    public void setJuego(Juego j) {
        juego = j;
        juego.setGui(this);
    }

    /**
     * Muestra el panel del menú principal en la ventana, ocultando los demás paneles
     * (nuevo juego, cargar juego) si están visibles.
     */
    public void Menu() {
        getContentPane().removeAll();
        add(menu);
        //Ocultar el resto de los paneles
        newgame.setVisible(false);
        loadgame.setVisible(false);
        //Cargar el menu
        try {
            menu.cargarMenu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        menu.repaint();
    }
}
