package pantallas.menu;

import basedatos.Partidas;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.Juego;
import idiomas.Idioma;
import pantallas.GUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * La clase {@code LoadGame} representa la pantalla para cargar un juego guardado.
 * Permite al usuario seleccionar una partida guardada, iniciar el juego o eliminar una partida existente.
 * Esta clase gestiona la carga de las partidas guardadas desde un archivo JSON,
 * mostrando la información de las partidas en botones, y permitiendo la selección y eliminación de partidas.
 * <p>
 * Extiende {@code JPanel} y es parte de la interfaz gráfica de la aplicación.
 */
public class LoadGame extends JPanel {
    private final ObjectMapper mapper = new ObjectMapper(); // Objeto para manipular archivos JSON
    private File archivo; // Archivo JSON que contiene las partidas guardadas

    private List<Partidas> listaPartidas = new ArrayList<>(); // Lista de partidas guardadas
    private JButton btnSeleccionado; // El botón de la partida seleccionada
    private Partidas partidaSelccionada; // La partida seleccionada

    private final JLabel noPartidas = new JLabel((Idioma.getRb().getString("NoPartidas")), SwingConstants.CENTER); // Mensaje cuando no hay partidas guardadas
    private final JButton btnJugar = new JButton((Idioma.getRb().getString("Strt_Btn"))); // Botón para iniciar el juego
    private final JButton btnEliminar = new JButton((Idioma.getRb().getString("EliminarPartida"))); // Botón para eliminar una partida
    private final JButton btnAtras = new JButton((Idioma.getRb().getString("Atras_Btn"))); // Botón para regresar al menú anterior

    private GUI gui;// Instancia de la interfaz principal (GUI)

    /**
     * Establece la referencia a la instancia de la interfaz principal {@code GUI}.
     *
     * @param gui la instancia de {@code GUI}.
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Constructor de la clase {@code LoadGame}. Inicializa los componentes y configura los botones de la pantalla.
     *
     * @param window la ventana principal donde se añadirá este panel.
     */
    public LoadGame(JFrame window) {

        new BorderLayout();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0, 0, 0, 0)); // Establece un fondo transparente
        setBorder(BorderFactory.createEmptyBorder((window.getHeight() / 3 - 50), 0, 0, 15));

        URL url_json = Objects.requireNonNull(this.getClass().getResource("/basedatos/partidas/partidas.json"));
        try {
            archivo = Paths.get(url_json.toURI()).toFile(); // Obtiene el archivo JSON como File
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        configurarBotones(window);
        window.repaint();
    }

    /**
     * Configura y añade los botones principales (Jugar, Eliminar, Atrás) al panel.
     * Los botones se añaden dependiendo de si hay partidas disponibles o no.
     */
    private void setearBotones() {
        JComponent[] items = {noPartidas, btnJugar, btnEliminar, btnAtras};
        for (JComponent item : items) {
            item.setAlignmentX(Component.CENTER_ALIGNMENT);
            item.setMaximumSize(new Dimension(400, 50));
            item.setFont(new Font("Arial", Font.PLAIN, 18));
            item.setBackground(new Color(236, 236, 236));
            item.setFocusable(false);

            if (item != noPartidas) {
                if (!listaPartidas.isEmpty()) {
                    add(item);
                } else {
                    add(btnAtras);
                }
                add(Box.createRigidArea(new Dimension(0, 20)));
            }
        }
    }

    /**
     * Configura las acciones de los botones de la pantalla.
     *
     * @param window la ventana principal donde se actualizarán los componentes.
     */
    private void configurarBotones(JFrame window) {
        //Acción para el botón de "Jugar"
        btnJugar.addActionListener(e -> {
            if (btnSeleccionado == null) {
                JOptionPane.showMessageDialog(null, (Idioma.getRb().getString("SeleccionaPartida")), (Idioma.getRb().getString("Strt_Btn")), JOptionPane.ERROR_MESSAGE);
            } else {
                iniciarJuego(window); // Inicia el juego con la partida seleccionada
            }
        });
        // Acción para el botón "Eliminar"
        btnEliminar.addActionListener(e -> {
            if (btnSeleccionado == null) {
                JOptionPane.showMessageDialog(null, (Idioma.getRb().getString("SeleccionaPartida")), (Idioma.getRb().getString("EliminarPartida")), JOptionPane.ERROR_MESSAGE);
            } else {
                confirmarEliminado(window); // Confirma si el usuario desea eliminar la partida
            }
        });
        // Acción para el botón "Atrás"
        btnAtras.addActionListener(e -> irAtras(window));
    }

    /**
     * Inicia el juego con la partida seleccionada.
     *
     * @param window la ventana principal donde se actualizarán los componentes.
     */
    private void iniciarJuego(JFrame window) {
        Juego juego = new Juego(partidaSelccionada); // Crea una nueva instancia de juego con la partida seleccionada
        gui.setJuego(juego); // Establece el juego en la interfaz
        // Elimina los componentes anteriores de la ventana y añade el juego a la ventana
        window.getContentPane().removeAll();
        window.add(juego);
        window.revalidate();
        window.repaint();
    }

    /**
     * Muestra una confirmación para eliminar la partida seleccionada.
     *
     * @param window la ventana principal donde se actualizarán los componentes.
     */
    private void confirmarEliminado(JFrame window) {
        String[] opciones = {(Idioma.getRb().getString("Atras_Btn")), (Idioma.getRb().getString("Eliminar"))}; // Opciones de confirmación
        int respuesta = JOptionPane.showOptionDialog(window, (Idioma.getRb().getString("EliminarSeguro")), (Idioma.getRb().getString("EliminarPartida"))
                , JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opciones, opciones[0]);
        if (respuesta == 1) {
            borrarPartida(window); // Elimina la partida si el usuario confirma
        } else {
            btnSeleccionado.setBackground(new Color(236, 236, 236)); // Restablece el color del botón si se cancela la eliminación
            btnSeleccionado = null;
            partidaSelccionada = null;
        }
    }

    /**
     * Elimina la partida seleccionada de la lista y actualiza el archivo JSON.
     *
     * @param window la ventana principal donde se actualizarán los componentes.
     */
    private void borrarPartida(JFrame window) {
        btnSeleccionado.setBackground(new Color(236, 236, 236));
        btnSeleccionado = null;
        for (Partidas p : listaPartidas) {
            if (Objects.equals(p.getId(), partidaSelccionada.getId())) {
                listaPartidas.remove(p); // Elimina la partida de la lista
                break;
            }
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, listaPartidas); // Guarda los cambios en el archivo JSON
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        removeAll();
        crearContenido();
        window.revalidate();
        window.repaint();
    }

    /**
     * Regresa al menú anterior (pantalla de inicio).
     *
     * @param window la ventana principal donde se actualizarán los componentes.
     */
    private void irAtras(JFrame window) {
        gui.getLoadgame().setVisible(false); // Oculta la pantalla de LoadGame
        try {
            gui.getMenu().cargarMenu();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        window.repaint();
    }

    /**
     * Verifica si hay partidas guardadas en el archivo JSON.
     *
     * @return {@code true} si existen partidas guardadas, {@code false} en caso contrario.
     */
    private boolean hayPartidas() {
        try {
            if (archivo.exists() && archivo.length() != 0) {
                listaPartidas = mapper.readValue(archivo, new TypeReference<List<Partidas>>() {
                });
            } else {
                listaPartidas = new ArrayList<>(); // Crear una nueva lista si el archivo está vacío o no existe
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return !listaPartidas.isEmpty();
    }

    /**
     * Crea y organiza los componentes visuales para mostrar las partidas guardadas.
     * Si no hay partidas, muestra un mensaje indicando que no existen partidas guardadas.
     */
    private void crearContenido() {
        if (hayPartidas()) {
            JPanel panelPartidas = crearPanelPartidas(); // Crea el panel con las partidas
            JScrollPane scrollPane = new JScrollPane(panelPartidas); // Envuelve el panel en un JScrollPane (Patrón de diseño Decorator)
            configurarScrollPane(scrollPane); // Configura el JScrollPane
            add(scrollPane, BorderLayout.CENTER);
            add(Box.createRigidArea(new Dimension(0, 20)));
        } else {
            add(noPartidas); //Añade texto de que no hay partidas si no existen partidas guardadas
            add(Box.createRigidArea(new Dimension(0, 20)));
        }
        add(Box.createRigidArea(new Dimension(0, 20)));
        setearBotones();
    }

    /**
     * Crea un panel que contiene los botones para cada partida guardada.
     *
     * @return un panel con los botones correspondientes a cada partida guardada.
     */
    private JPanel crearPanelPartidas() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(93, 156, 163));
        for (int i = listaPartidas.size() - 1; i >= 0; i--) { // Recorre las partidas desde la más reciente a la más antigua
            Partidas p = listaPartidas.get(i);
            JButton p_b = crearBotonPartida(p); // Crea un botón para cada partida
            panel.add(p_b); // Añade el botón al panel
            if (i != 0) {
                panel.add(Box.createRigidArea(new Dimension(0, 20)));
            }
            p_b.addActionListener(e -> seleccionarPartida(p_b, p)); // Asigna un ActionListener a cada botón
        }
        return panel;
    }

    /**
     * Crea un botón que representa una partida guardada.
     *
     * @param p la partida que se representará en el botón.
     * @return un botón configurado con los detalles de la partida.
     */
    private JButton crearBotonPartida(Partidas p) {
        JButton p_b = new JButton(p.getUser() + " - " + p.getLenguaje() + " | lvl:" + p.getNivel());
        p_b.setAlignmentX(Component.CENTER_ALIGNMENT);
        p_b.setMaximumSize(new Dimension(400, 50));
        p_b.setPreferredSize(new Dimension(400, 50));
        p_b.setFont(new Font("Arial", Font.PLAIN, 18));
        p_b.setBackground(new Color(236, 236, 236));
        p_b.setFocusable(false);
        return p_b;
    }

    /**
     * Maneja la selección de una partida cuando se hace clic en su botón correspondiente.
     * Cambia el color del botón seleccionado y almacena la partida seleccionada.
     *
     * @param p_b el botón que representa la partida seleccionada.
     * @param p   la partida seleccionada.
     */
    private void seleccionarPartida(JButton p_b, Partidas p) {
        if (btnSeleccionado != null) {
            btnSeleccionado.setBackground(new Color(236, 236, 236));
        }
        p_b.setBackground(new Color(255, 132, 132)); // Resalta el botón seleccionado
        btnSeleccionado = p_b; // Almacena el botón seleccionado
        partidaSelccionada = p; // Almacena la partida seleccionada
    }

    /**
     * Configura el estilo y el comportamiento del JScrollPane que contiene las partidas.
     *
     * @param scrollPane el JScrollPane que será configurado.
     */
    private void configurarScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setMaximumSize(new Dimension(400, 120));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUI(createScrollBarUI());
    }

    /**
     * Crea un estilo personalizado para la barra de desplazamiento vertical del JScrollPane.
     *
     * @return una instancia de {@code BasicScrollBarUI} personalizada.
     */
    private BasicScrollBarUI createScrollBarUI() {
        return new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(126, 216, 228); // Color del desplazador
                this.trackColor = new Color(78, 132, 138); // Color del fondo de la barra
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return botonVacio(); // Botón vacío para la flecha de desplazamiento
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return botonVacio(); // Botón vacío para la flecha de desplazamiento
            }

            private JButton botonVacio() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0)); // Tamaño nulo para ocultar los botones
                return button;
            }
        };
    }

    /**
     * Carga y muestra la pantalla de carga de partidas.
     * Restablece la selección anterior y actualiza el contenido con los datos actuales.
     *
     * @throws IOException si ocurre un error al cargar el contenido.
     */
    public void cargarLoadGame() throws IOException {
        gui.getLoadgame().setVisible(true); // Muestra la pantalla de carga de partidas
        if (btnSeleccionado != null) {
            btnSeleccionado.setBackground(new Color(236, 236, 236));
            btnSeleccionado = null;
        }
        partidaSelccionada = null;
        this.removeAll();
        crearContenido();
        updateTextos();
    }

    /**
     * Actualiza los textos de los componentes de la interfaz con base en el idioma seleccionado.
     */
    private void updateTextos() {
        noPartidas.setText((Idioma.getRb().getString("NoPartidas")));
        btnJugar.setText((Idioma.getRb().getString("Strt_Btn")));
        btnEliminar.setText((Idioma.getRb().getString("EliminarPartida")));
        btnAtras.setText((Idioma.getRb().getString("Atras_Btn")));
    }
}
