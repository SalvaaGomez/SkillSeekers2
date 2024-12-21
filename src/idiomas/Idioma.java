package idiomas;

import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Clase que gestiona los idiomas de la aplicación mediante la configuración
 * de locales y paquetes de recursos.
 * <p>
 * Permite cambiar el idioma activo de la aplicación dinámicamente y proporciona
 * acceso a los textos localizados a través de un {@link ResourceBundle}.
 */
public class Idioma extends Properties {

    private static Locale[] idiomas = {new Locale("es", "ESP"),new Locale("en", "US"),new Locale("fr", "FR"),new Locale("de", "DE")};
    private static Locale i = idiomas[0];
    private static ResourceBundle rb = ResourceBundle.getBundle("idioma",i);

    /**
     * Obtiene el paquete de recursos actual, que contiene los textos localizados
     * según el idioma activo.
     *
     * @return el {@link ResourceBundle} actual.
     */
    public static ResourceBundle getRb() {
        return rb;
    }
    /**
     * Cambia el idioma activo de la aplicación según el nombre del idioma proporcionado.
     * <p>
     * Actualiza la localización y recarga el paquete de recursos asociado.
     * Si el nombre del idioma no coincide con ninguno de los idiomas soportados,
     * se establece por defecto el Español.
     *
     * @param idioma el nombre del idioma a configurar. Idiomas posibles: "Español", "English", "Français", "Deutsch".
     */
    public static void setI(String idioma) {

        switch (idioma) {
            case "Español":
                i = idiomas[0];
                break;
            case "English":
                i = idiomas[1];
                break;
            case "Français":
                i = idiomas[2];
                break;
            case "Deutsch":
                i = idiomas[3];
                break;
            default:
                i = idiomas[0];
                break;
        }
        resetrb();
    }
    /**
     * Recarga el paquete de recursos {@link ResourceBundle} según la localización activa.
     * Este método se llama automáticamente cada vez que se cambia el idioma.
     */
    private static void resetrb() {
        rb = ResourceBundle.getBundle("idioma",i);
    }
}
