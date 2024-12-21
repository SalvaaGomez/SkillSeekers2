package pantallas.menu;

import idiomas.Idioma;
import pantallas.GUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * La clase Settings representa la pantalla de configuración del juego.
 * Permite al usuario modificar las opciones del idioma, pantalla completa y sonido,
 * además de regresar al menú principal.
 */
public class Settings extends JPanel{
    private GUI gui;

    /**
     * Configura la referencia de GUI para que esta clase pueda interactuar con el marco principal.
     *
     * @param gui instancia de {@link GUI}.
     */
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Constructor de la clase Settings.
     * Inicializa los componentes visuales y configura los listeners para los eventos de los botones.
     *
     * @param window el marco principal donde se mostrará esta pantalla.
     */
    public Settings(JFrame window){
        new BorderLayout();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0,0,0,0));
        setBorder(BorderFactory.createEmptyBorder((window.getHeight()/3)+50, 0, 0, 15));

        //CONFIGURACIÓN IDIOMA
        String[] idi = {"Español", "English", "Français", "Deutsch"};
        JComboBox idioma = new JComboBox(idi);

        //RESTO DE BOSTONES
        JButton pantalla_completa = new JButton();
        JButton sonido = new JButton();
        JButton atras = new JButton("Atrás");
        pantalla_completa.setText((Idioma.getRb().getString("P_C_Btn")));
        sonido.setText((Idioma.getRb().getString("Sound_Btn")));
        atras.setText((Idioma.getRb().getString("Atras_Btn")));

        //LOS AÑADIMOS AL PANEL
        JComponent[] items = {idioma, pantalla_completa,sonido, atras};
        seteamosBotonesPanel(items);

        //ACCIONES DE CADA UNO
        idioma.addActionListener(e -> resetTextoBotones(window, idioma, pantalla_completa, sonido, atras));
        idioma.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER); // Centrar el texto
                window.repaint(); // Este window lo he puesto para que cuando se despliegue las opciones del combobox se repinte el fondo pq si no, se quita la imagen en lo q ocupa la lista de items.

                return label;
            }
        });

        pantalla_completa.addActionListener(e -> JOptionPane.showMessageDialog(null, (Idioma.getRb().getString("NoDispo"))));
        sonido.addActionListener(e -> JOptionPane.showMessageDialog(null, (Idioma.getRb().getString("NoDispo"))));
        atras.addActionListener(e -> accionBtnAtras(window));
        window.repaint();
    }
    /**
     * Configura los estilos visuales y el diseño de los componentes proporcionados
     * y los agrega al panel actual.
     * <p>
     * Este método es útil para mantener una apariencia consistente entre los botones
     * y otros elementos interactivos dentro de un panel.
     *
     * @param items un arreglo de componentes {@link JComponent} que se configurarán y agregarán al panel.
     */
    private void seteamosBotonesPanel(JComponent[] items) {
        for (JComponent item : items) {
            item.setAlignmentX(Component.CENTER_ALIGNMENT);
            item.setMaximumSize(new Dimension(200, 50));
            item.setFont(new Font("Arial", Font.PLAIN, 18));
            item.setFocusable(false);
            item.setBackground(new Color(236, 236, 236));

            add(item);
            add(Box.createRigidArea(new Dimension(0, 20)));
        }
    }
    /**
     * Cambia los textos de los botones y otros elementos según el idioma seleccionado.
     *
     * @param window el marco principal de la aplicación.
     * @param idioma el JComboBox de selección de idioma.
     * @param pantalla_completa el botón para cambiar la configuración de pantalla completa.
     * @param sonido el botón para cambiar la configuración de sonido.
     * @param atras el botón para regresar al menú principal.
     */
    private void resetTextoBotones(JFrame window, JComboBox idioma, JButton pantalla_completa, JButton sonido, JButton atras) {
        Idioma.setI((String) Objects.requireNonNull(idioma.getSelectedItem()));

        pantalla_completa.setText((Idioma.getRb().getString("P_C_Btn")));
        sonido.setText((Idioma.getRb().getString("Sound_Btn")));
        atras.setText((Idioma.getRb().getString("Atras_Btn")));

        gui.getMenu().setImage("Ajustes");

        window.repaint();
    }
    /**
     * Acción ejecutada al presionar el botón "Atrás".
     * Oculta la pantalla actual y muestra el menú principal.
     *
     * @param window el marco principal de la aplicación.
     */
    private void accionBtnAtras(JFrame window) {
        gui.getSettings().setVisible(false);
        try {
            gui.getMenu().cargarMenu();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        window.repaint();
    }
}
