package game.mapa;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que gestiona la visualización de un mapa en el juego. Esta clase se encarga
 * de renderizar las capas del mapa en el panel utilizando imágenes de los patrones (tilesets)
 * y aplica una escala para mejorar la visualización.
 * <p>
 * Se encarga de dibujar las baldosas del mapa, incluyendo la capacidad de mostrar áreas de colisión.
 */
public class MapaVisual extends JPanel {
    private final Mapa mapa;
    private static int Mapa_CoordX;
    private static int Mapa_CoordY;


    private List<int[][]> conjuntoCapas;
    private List<Mapa.Patron> conjuntoPatrones;
    private final Map<Mapa.Patron, BufferedImage> conjuntoImgPatrones = new HashMap<>();
    private int anchuraBaldosa;
    private int alturaBaldosa;
    private int scaledTileWidth;
    private int scaledTileHeight;
    private int scaledMapWidth;
    private int scaledMapHeight;

    /**
     * Constructor que inicializa la visualización del mapa.
     *
     * @param mapa El mapa a visualizar.
     */
    public MapaVisual(Mapa mapa) {
        this.mapa = mapa;
        setLayout(null);
        inicializacionAtributos(this.mapa);
    }

    /**
     * Establece las coordenadas del mapa que se deben dibujar en el panel.
     *
     * @param CoordX Coordenada X del mapa en el panel.
     * @param CoordY Coordenada Y del mapa en el panel.
     */
    public void setCordenadas(int CoordX, int CoordY) {
        Mapa_CoordX = CoordX;
        Mapa_CoordY = CoordY;
    }

    /**
     * Inicializa los atributos necesarios para la visualización del mapa, como las dimensiones
     * de las baldosas y las escalas de los tiles y del mapa.
     *
     * @param mapa El mapa desde el cual se obtiene la información.
     */
    private void inicializacionAtributos(Mapa mapa) {
        int escala = 2;
        conjuntoPatrones = mapa.getConjuntoPatrones();
        conjuntoCapas = mapa.getConjuntoCapas();
        anchuraBaldosa = mapa.getAnchoBaldosa();
        alturaBaldosa = mapa.getAltoBaldosa();
        scaledTileWidth = anchuraBaldosa * escala;
        scaledTileHeight = alturaBaldosa * escala;
        scaledMapWidth = (mapa.getAnchoMapa() * anchuraBaldosa * escala);
        scaledMapHeight = (mapa.getAltoMapa() * alturaBaldosa * escala);
        setConjuntoImgPatrones();
    }

    /**
     * Carga las imágenes de los patrones (tilesets) y las almacena en un mapa para su posterior uso.
     */
    private void setConjuntoImgPatrones() {
        for (Mapa.Patron tileset : conjuntoPatrones) {
            try {
                conjuntoImgPatrones.put(tileset, ImageIO.read(tileset.getRuta()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Dibuja las capas del mapa y, si es necesario, las áreas de colisión.
     *
     * @param g El objeto Graphics utilizado para dibujar.
     */
    public void paintComponent(Graphics g) {
        boolean mostrarColisiones = false; // Variable para que cambiar manualmente en fase de desarrollo
        super.paintComponent(g);
        int offsetX = ((getWidth() - scaledMapWidth) / 2) + Mapa_CoordX;
        int offsetY = ((getHeight() - scaledMapHeight) / 2) + Mapa_CoordY;

        if (conjuntoCapas != null && conjuntoImgPatrones != null) {
            // Dibuja cada capa del mapa
            for (int[][] capa : conjuntoCapas) {
                for (int y = 0; y < capa.length; y++) {
                    for (int x = 0; x < capa[0].length; x++) {
                        if (capa[y][x] > 0) {
                            Mapa.Patron p = null;
                            // Identifica el patrón (tileset) correspondiente a cada baldosa
                            for (Mapa.Patron patrones : conjuntoPatrones) {
                                if (capa[y][x] >= patrones.getFirstgid()) {
                                    p = patrones;
                                } else {
                                    break;
                                }
                            }
                            // Dibuja la baldosa correspondiente en el mapa
                            if (p != null) {
                                BufferedImage img = conjuntoImgPatrones.get(p);
                                if (img != null) {
                                    int id = capa[y][x] - p.getFirstgid();
                                    int columnasPatron = img.getWidth(null) / alturaBaldosa;
                                    int baldosaX = (id % columnasPatron) * anchuraBaldosa;
                                    int baldosaY = (id / columnasPatron) * alturaBaldosa;

                                    g.drawImage(img,
                                            offsetX + x * scaledTileWidth, offsetY + y * scaledTileHeight,
                                            offsetX + x * scaledTileWidth + scaledTileWidth, offsetY + y * scaledTileHeight + scaledTileHeight,
                                            baldosaX, baldosaY, baldosaX + anchuraBaldosa, baldosaY + alturaBaldosa,
                                            null);
                                }
                            }
                        }
                    }
                }
            }

            // Dibuja las áreas de colisión si se requiere
            if (mostrarColisiones) {
                for (int y = 0; y < mapa.getMatrizColisiones().length; y++) {
                    for (int x = 0; x < mapa.getMatrizColisiones()[y].length; x++) {
                        if (mapa.getMatrizColisiones()[y][x]) {
                            g.setColor(new Color(255, 0, 0, 53));
                            g.fillRect(offsetX + x * scaledTileWidth, offsetY + y * scaledTileHeight,
                                    scaledTileWidth, scaledTileHeight);
                        }
                    }
                }
            }
        }
    }
}
