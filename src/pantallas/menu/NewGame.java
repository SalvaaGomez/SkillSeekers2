package pantallas.menu;

import basedatos.gestorPartidas;
import game.Juego;
import idiomas.Idioma;
import pantallas.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

/**
 * La clase {@code NewGame} representa una pantalla donde los usuarios pueden iniciar una nueva partida.
 * Permite al usuario ingresar su nombre, seleccionar un lenguaje de programación y comenzar el juego.
 * También incluye validaciones y navegación hacia otras pantallas del sistema.<p>
 * Extiende {@code JPanel} y es parte de la interfaz gráfica de la aplicación.
 */
public class NewGame extends JPanel {

    private static JTextField nombre = new JTextField();
    private static String[] listaLenguajes = {(Idioma.getRb().getString("Lng_Txt")),"Java", "Python", "C++"};
    private static JComboBox comboBoxLenguajes = new JComboBox(listaLenguajes);
    private static JButton empezarBtn = new JButton((Idioma.getRb().getString("Strt_Btn")));
    private static JButton atrasBtn = new JButton((Idioma.getRb().getString("Atras_Btn")));
    private gestorPartidas gestorP = new gestorPartidas();

    private GUI gui;

    /**
     * Establece la referencia a la instancia de la interfaz principal {@code GUI}.
     *
     * @param gui la instancia de {@code GUI}.
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }
    /**
     * Constructor de la clase NewGame.
     * Inicializa los componentes visuales y configura los listeners para los eventos de los botones.
     *
     * @param window el marco principal donde se mostrará esta pantalla.
     */
    public NewGame(JFrame window) {
        new BorderLayout();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0,0,0,0));

        nombre = crearCampoTexto(Idioma.getRb().getString("Nombre_Txt"));

        configurarItems(window);

        empezarBtn.addActionListener(e -> accionBtnEmpezar(window));
        atrasBtn.addActionListener(e -> accionBtnAtras(window));

        window.repaint();
    }
    /**
     * Acción ejecutada al presionar el botón "Atrás".
     * Oculta la pantalla actual y muestra el menú principal.
     *
     * @param window el marco principal de la aplicación.
     */
    private void accionBtnAtras(JFrame window) {
        gui.getNewgame().setVisible(false);
        try {
            gui.getMenu().cargarMenu();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        window.repaint();
    }
    /**
     * Acción ejecutada al presionar el botón "Empezar".
     * Valida los datos ingresados, inicializa una nueva partida y redirige al juego.
     *
     * @param window el marco principal de la aplicación.
     */
    private void accionBtnEmpezar(JFrame window) {
        String nombre = NewGame.nombre.getText().trim();
        String lenguajeSeleccionado = comboBoxLenguajes.getSelectedItem().toString();
        if (nombre.equals((Idioma.getRb().getString("Nombre_Txt"))) || lenguajeSeleccionado.equals((Idioma.getRb().getString("Lng_Txt")))) {
            JOptionPane.showMessageDialog(null, (Idioma.getRb().getString("CompletaTodo")), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!lenguajeSeleccionado.equals("Java")){
            JOptionPane.showMessageDialog(null, (Idioma.getRb().getString("prox")), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        gestorP.nuevaPartida(nombre, lenguajeSeleccionado);
        Juego juego = new Juego(gestorP.getPartidas());
        gui.setJuego(juego);

        window.getContentPane().removeAll();
        window.add(juego);
        window.revalidate();
        window.repaint();
    }

    /**
     * Configura y estiliza los elementos visuales de la pantalla.
     *
     * @param window el marco principal de la aplicación.
     */
    private void configurarItems(JFrame window) {
        JComponent[] items = {nombre, comboBoxLenguajes, empezarBtn, atrasBtn};
        for (JComponent item : items) {
            item.setAlignmentX(Component.CENTER_ALIGNMENT);
            item.setMaximumSize(new Dimension(400, 50));
            if(item == nombre){
                item.setFont(new Font("Arial", Font.ITALIC, 18));
                item.setForeground(Color.GRAY);
            }else{
                item.setFont(new Font("Arial", Font.PLAIN, 18));
            }
            item.setBackground(new Color(236, 236, 236));
            add(item);
            add(Box.createRigidArea(new Dimension(0, 20)));
        }

        setBorder(BorderFactory.createEmptyBorder((window.getHeight()/3+50), 0, 0, 15));
        // Botón para iniciar el juego
        comboBoxLenguajes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                label.setHorizontalAlignment(SwingConstants.CENTER); // Centrar el texto
                window.repaint(); // Este window lo he puesto para que cuando se despliegue las opciones del combobox se repinte el fondo pq si no se quita la imagen en lo q ocupa la lista de items.

                return label;
            }
        });
    }
    /**
     * Crea un campo de texto configurado con estilos dinámicos.
     *
     * @param texto el texto inicial del campo de texto.
     * @return un JTextField configurado.
     */
    private JTextField crearCampoTexto(String texto) {
        JTextField campo = new JTextField(texto);
        campo.setFont(new Font("Arial", Font.ITALIC, 18));
        campo.setForeground(Color.GRAY);
        campo.setMaximumSize(new Dimension(400, 50));
        campo.setMargin(new Insets(0, 15, 0, 0));
        campo.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if(campo.getText().equals(texto)){
                    campo.setText("");
                    campo.setFont(new Font("Arial", Font.PLAIN, 18));
                    campo.setForeground(new Color(51,51,51));
                }
            }
            public void focusLost(FocusEvent e) {
                if(campo.getText().isEmpty()){
                    campo.setText(texto);
                    campo.setFont(new Font("Arial", Font.ITALIC, 18));
                    campo.setForeground(Color.GRAY);
                }else{
                    campo.setFont(new Font("Arial", Font.PLAIN, 18));
                    campo.setForeground(new Color(51,51,51));
                }
            }
        });
        campo.setMargin(new Insets(0,15,0,0));
        return campo;
    }
    /**
     * Restaura y prepara la pantalla NewGame con valores iniciales.
     * También actualiza los textos de los componentes según el idioma actual.
     *
     * @throws IOException si ocurre un error al cargar los recursos necesarios.
     */
    public void cargarNewGame() throws IOException {
        gui.getNewgame().setVisible(true);
        gui.getMenu().setImage("Juego");
        nombre.setText((Idioma.getRb().getString("Nombre_Txt")));
        listaLenguajes[0] = (Idioma.getRb().getString("Lng_Txt"));
        comboBoxLenguajes.removeAllItems();
        for (String item: listaLenguajes){
            comboBoxLenguajes.addItem(item);
        }
        empezarBtn.setText((Idioma.getRb().getString("Strt_Btn")));
        atrasBtn.setText((Idioma.getRb().getString("Atras_Btn")));
    }
};
