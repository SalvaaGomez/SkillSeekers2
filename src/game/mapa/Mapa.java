package game.mapa;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que representa un mapa en el juego. Esta clase carga y gestiona la información
 * del mapa a partir de un archivo XML (.tmx) que describe el mapa y sus capas.
 * <p>
 * El mapa se compone de varias capas de tiles (baldosas) y patrones (conjuntos de tiles),
 * además de una capa específica de colisiones.
 */
public class Mapa {
    /**
     * Clase interna que representa un patrón (tileset) utilizado en el mapa.
     */
    public static class Patron {
        private File ruta;
        private int firstgid;

        /**
         * Obtiene la ruta del archivo del patrón.
         *
         * @return Ruta del archivo del patrón.
         */
        public File getRuta() {
            return ruta;
        }

        /**
         * Obtiene el primer ID de este patrón.
         *
         * @return El primer ID del patrón.
         */
        public int getFirstgid() {
            return firstgid;
        }
    }

    private int anchoMapa, altoMapa;
    private final static int anchoBaldosa = 16, altoBaldosa = 16;

    private List<Patron> conjuntoPatrones = new ArrayList<>();
    private final List<int[][]> conjuntoCapas = new ArrayList<>();
    private final List<boolean[][]> conjuntoColisiones = new ArrayList<>();
    private boolean[][] matrizColisiones;


    /**
     * Constructor que carga el mapa desde un archivo TMX.
     *
     * @param path Ruta del archivo TMX que describe el mapa.
     * @throws RuntimeException Si ocurre algún error al cargar o parsear el archivo.
     */
    public Mapa(String path) {
        try {
            File plano = Paths.get(Objects.requireNonNull(Mapa.class.getResource(path)).toURI()).toFile();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document tmx = builder.parse(plano);
            //Selecciono Mapa
            Element map = (Element) tmx.getElementsByTagName("map").item(0);

            anchoMapa = Integer.parseInt(map.getAttribute("width"));
            altoMapa = Integer.parseInt(map.getAttribute("height"));
            //Recorro todos los patrones y los guardo en ConjuntoPatrones
            guardarPatrones(tmx);
            //Recorro cada capa para guardar la info en conjuntoCapas y conjuntoColisiones
            guardarCapas(tmx);
            //Comprimo el ArrayList conjuntoColisiones en matrizColisiones
            comprimirColisiones();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Guarda los patrones (tilesets) que se encuentran en el archivo TMX.
     *
     * @param doc El documento XML que contiene la información del mapa.
     * @throws URISyntaxException Si hay un error al obtener la URI de un recurso.
     */
    private void guardarPatrones(Document doc) throws URISyntaxException {
        conjuntoPatrones.clear();
        for (int i = 0; i < doc.getElementsByTagName("tileset").getLength(); i++) {
            Element tilesetElement = (Element) doc.getElementsByTagName("tileset").item(i);

            Patron nuevo_patron = new Patron();
            nuevo_patron.firstgid = Integer.parseInt(tilesetElement.getAttribute("firstgid"));

            nuevo_patron.ruta = Paths.get(Objects.requireNonNull(Mapa.class.getResource("/mapas/patrones/" + tilesetElement.getAttribute("source"))).toURI()).toFile();
            conjuntoPatrones.add(nuevo_patron);

        }
    }

    /**
     * Guarda las capas del mapa y determina las áreas de colisión.
     *
     * @param tmx El documento XML que contiene la información del mapa.
     */
    private void guardarCapas(Document tmx) {
        conjuntoColisiones.clear();
        conjuntoCapas.clear();
        for (int i = 0; i < tmx.getElementsByTagName("layer").getLength(); i++) {
            boolean[][] colisiones = new boolean[altoMapa][anchoMapa];
            int[][] ArrayCapa = new int[altoMapa][anchoMapa];
            String[] capa = ((Element) tmx.getElementsByTagName("layer").item(i)).getElementsByTagName("data").item(0).getTextContent().split(",");
            for (int y = 0; y < altoMapa; y++) {
                for (int x = 0; x < anchoMapa; x++) {
                    ArrayCapa[y][x] = Integer.parseInt(capa[y * anchoMapa + x].trim());
                    if ("Colisiones".equals(((Element) tmx.getElementsByTagName("layer").item(i)).getAttribute("name"))) {
                        colisiones[y][x] = ArrayCapa[y][x] != 0;
                    }
                }
            }
            conjuntoCapas.add(ArrayCapa);
            conjuntoColisiones.add(colisiones);
        }
    }

    /**
     * Comprime la información de las capas de colisiones en una matriz de colisiones única.
     */
    private void comprimirColisiones() {
        matrizColisiones = new boolean[altoMapa][anchoMapa];
        for (boolean[][] capaColision : conjuntoColisiones) {
            for (int y = 0; y < capaColision.length; y++) {
                for (int x = 0; x < capaColision[y].length; x++) {
                    if (capaColision[y][x]) {
                        matrizColisiones[y][x] = true;
                    }
                }
            }
        }
    }

    /**
     * Obtiene el ancho de una baldosa del mapa.
     *
     * @return El ancho de una baldosa.
     */
    public int getAnchoBaldosa() {
        return anchoBaldosa;
    }

    /**
     * Obtiene el alto de una baldosa del mapa.
     *
     * @return El alto de una baldosa.
     */
    public int getAltoBaldosa() {
        return altoBaldosa;
    }

    /**
     * Obtiene el ancho total del mapa en baldosas.
     *
     * @return El ancho del mapa.
     */
    public int getAnchoMapa() {
        return anchoMapa;
    }

    /**
     * Obtiene el alto total del mapa en baldosas.
     *
     * @return El alto del mapa.
     */
    public int getAltoMapa() {
        return altoMapa;
    }


    /**
     * Obtiene el conjunto de capas del mapa.
     *
     * @return Una lista de matrices que representan las capas del mapa.
     */
    public List<int[][]> getConjuntoCapas() {
        return conjuntoCapas;
    }

    /**
     * Obtiene el conjunto de patrones (tilesets) utilizados en el mapa.
     *
     * @return Una lista de objetos {@link Patron} que representan los patrones del mapa.
     */
    public List<Patron> getConjuntoPatrones() {
        return conjuntoPatrones;
    }

    /**
     * Obtiene la matriz de colisiones del mapa.
     *
     * @return Una matriz booleana que indica las áreas de colisión del mapa.
     */
    public boolean[][] getMatrizColisiones() {
        return matrizColisiones;
    }
}