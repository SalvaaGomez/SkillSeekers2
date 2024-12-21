package game.entidades.pj;

import game.Juego;
import game.KeysConfig;
import game.entidades.Entidad;
import game.mapa.Mapa;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;
/**
 * Clase que representa al personaje del jugador en el juego.
 * Esta clase extiende la clase {@link Entidad} y maneja la lógica de movimiento, animaciones y cambios de nivel
 * del personaje. El personaje puede moverse en las direcciones: avanzar, retroceder, izquierda, derecha.
 */
public class Personaje extends Entidad {

    private static KeysConfig k;
    private static Image avanza01, avanza02, atras01, atras02, izq01, izq02, dcha01, dcha02, quieto_atras, quieto_frente, quieto_dcha, quieto_izq;
    private static int alternar = 1;
    private static int velocidad = 2;
    private String direccion;
    private int nivel;
    private Mapa mapa;
    private final Juego j;
    /**
     * Obtiene el nivel del personaje.
     *
     * @return El nivel del personaje.
     */
    public int getNivel() {
        return nivel;
    }

    /**
     * Sube el nivel del personaje en uno.
     */
    public void subirNivel() {
        this.nivel += 1;
    }

    /**
     * Constructor de la clase {@link Personaje}.
     *
     * @param j Instancia del juego que contiene la configuración de teclas y otros detalles importantes del juego.
     */
    public Personaje(Juego j) {
        super(avanza01, 960,540);
        direccion = "Avanzar";
        this.j = j;

        Personaje.k = j.getKeysConfig();
        setearSkins();

        nivel = j.getPartidaActual().getNivel();
        setMapa();
    }

    /**
     * Establece el mapa en el que el personaje se encuentra.
     */
    public void setMapa() {
        mapa = j.getMapaActual();
    }

    /**
     * Carga las imágenes de los diferentes skins para las animaciones del personaje.
     */
    public void setearSkins() {
        try {
            avanza01 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/skins/pj/pj_avanza_01.png")));
            avanza02 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/skins/pj/pj_avanza_02.png")));
            atras01 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/skins/pj/pj_atras_01.png")));
            atras02 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/skins/pj/pj_atras_02.png")));
            izq01 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/skins/pj/pj_izq_01.png")));
            izq02 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/skins/pj/pj_izq_02.png")));
            dcha01 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/skins/pj/pj_dcha_01.png")));
            dcha02 = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/skins/pj/pj_dcha_02.png")));
            quieto_atras = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/skins/pj/pj_quieto_atras.png")));
            quieto_frente = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/skins/pj/pj_quieto_frente.png")));
            quieto_izq = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/skins/pj/pj_quieto_izq.png")));
            quieto_dcha = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/skins/pj/pj_quieto_dcha.png")));

        } catch (IOException e) {
            System.out.println("Error.");
        }
    }

    /**
     * Actualiza el estado del personaje, realizando movimientos según las teclas presionadas.
     * Calcula las nuevas posiciones del personaje basándose en las teclas y las colisiones del mapa.
     */
    public void updatePJ() {
        int anchoBaldosa = mapa.getAnchoBaldosa(); // Ancho de una baldosa
        int altoBaldosa = mapa.getAltoBaldosa(); // Alto de una baldosa

        boolean[][] colisiones = mapa.getMatrizColisiones();
        double baldosaX_Dec = (double) ((mapa.getAnchoMapa()*16)- j.getMapa_CoordX()) / (anchoBaldosa * 2);
        double baldosaY_Dec = (double) ((mapa.getAltoMapa()*16)- j.getMapa_CoordY())/ (altoBaldosa * 2);
        int baldosaX;
        int baldosaY;
        // Movimiento basado en las teclas presionadas
        if (k.isAvanzar()) {
            baldosaY = (int) Math.floor(baldosaY_Dec);
            baldosaX = (int) Math.floor(baldosaX_Dec);
            int baldosaY_Objetivo = baldosaY - 1;
            Avanzar(baldosaY_Objetivo, colisiones, baldosaX);
        } else if (k.isAtras()) {
            baldosaX = (int) Math.floor(baldosaX_Dec);
            baldosaY = (int) Math.floor(baldosaY_Dec);
            int baldosaY_Objetivo = baldosaY + 1;
            Atras(baldosaY_Objetivo, colisiones, baldosaX);
        } else if (k.isIzquierda()) {
            baldosaX = (int) Math.ceil(baldosaX_Dec);
            baldosaY = (int) Math.round(baldosaY_Dec);
            int baldosaX_Objetivo = baldosaX - 1;
            Izquierda(baldosaX_Objetivo, colisiones, baldosaY);
        } else if (k.isDerecha()) {
            baldosaX = (int) Math.ceil(baldosaX_Dec);
            baldosaY = (int) Math.round(baldosaY_Dec);
            int baldosaX_Objetivo = baldosaX + 1;
            Derecha(baldosaX_Objetivo, colisiones, baldosaY);
        } else {
            switch (direccion) {
                case "Avanzar":
                    Skin = quieto_frente;
                    break;
                case "Atras":
                    Skin = quieto_atras;
                    break;
                case "Izq":
                    Skin = quieto_izq;
                    break;
                case "Dcha":
                    Skin = quieto_dcha;
                    break;
            }
        }
    }

    /**
     * Mueve al personaje hacia adelante si no hay colisiones en la dirección.
     *
     * @param baldosaY_Objetivo La coordenada Y del objetivo en el mapa.
     * @param colisiones La matriz de colisiones del mapa.
     * @param baldosaX La coordenada X del personaje en el mapa.
     */
    private void Avanzar(int baldosaY_Objetivo, boolean[][] colisiones, int baldosaX) {
        if (baldosaY_Objetivo >= 2 && !colisiones[baldosaY_Objetivo][baldosaX]) {
            j.setMapa_CoordY(j.getMapa_CoordY() + velocidad);
        }
        Skin = alternar > 20 ? atras01 : atras02;
        alternar = (alternar + 1) % 40;
        direccion = "Atras";
    }
    /**
     * Mueve al personaje hacia atrás si no hay colisiones en la dirección.
     *
     * @param baldosaY_Objetivo La coordenada Y del objetivo en el mapa.
     * @param colisiones La matriz de colisiones del mapa.
     * @param baldosaX La coordenada X del personaje en el mapa.
     */
    private void Atras(int baldosaY_Objetivo, boolean[][] colisiones, int baldosaX) {
        if (baldosaY_Objetivo < mapa.getAltoMapa()-2 && !colisiones[baldosaY_Objetivo][baldosaX]) {
            j.setMapa_CoordY(j.getMapa_CoordY() - velocidad);
        }
        Skin = alternar > 20 ? avanza01 : avanza02;
        alternar = (alternar + 1) % 40;
        direccion = "Avanzar";
    }
    /**
     * Mueve al personaje hacia la izquierda si no hay colisiones en la dirección.
     *
     * @param baldosaX_Objetivo La coordenada X del objetivo en el mapa.
     * @param colisiones La matriz de colisiones del mapa.
     * @param baldosaY La coordenada Y del personaje en el mapa.
     */
    private void Izquierda(int baldosaX_Objetivo, boolean[][] colisiones, int baldosaY) {
        if (baldosaX_Objetivo >= 2 && !colisiones[baldosaY][baldosaX_Objetivo]) {
            j.setMapa_CoordX(j.getMapa_CoordX() + velocidad);
        }
        Skin = alternar > 20 ? izq01 : izq02;
        alternar = (alternar + 1) % 40;
        direccion = "Izq";
    }

    /**
     * Mueve al personaje hacia la derecha si no hay colisiones en la dirección.
     *
     * @param baldosaX_Objetivo La coordenada X del objetivo en el mapa.
     * @param colisiones La matriz de colisiones del mapa.
     * @param baldosaY La coordenada Y del personaje en el mapa.
     */
    private void Derecha(int baldosaX_Objetivo, boolean[][] colisiones, int baldosaY) {
        if (baldosaX_Objetivo < mapa.getAnchoMapa()-1 && !colisiones[baldosaY][baldosaX_Objetivo]) {
            j.setMapa_CoordX(j.getMapa_CoordX() - velocidad);
        }
        Skin = alternar > 20 ? dcha01 : dcha02;
        alternar = (alternar + 1) % 40;
        direccion = "Dcha";
    }
}



