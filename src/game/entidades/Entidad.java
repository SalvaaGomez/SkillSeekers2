package game.entidades;


import java.awt.*;
/**
 * Clase abstracta que representa una entidad en el juego.
 * Las entidades pueden ser NPCs o un personaje jugable.
 * Todas entidades tendrán una representación visual (skin) y una posición en el mundo del juego.
 */
public abstract class Entidad {

    protected Image Skin;
    private int coordX;
    private int coordY;

    /**
     * Constructor para crear una entidad con su skin y coordenadas en el espacio del juego.
     *
     * @param skin la imagen que representa la entidad.
     * @param coordX la coordenada X de la entidad.
     * @param coordY la coordenada Y de la entidad.
     */
    public Entidad(Image skin, int coordX, int coordY) {
        this.Skin = skin;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    /**
     * Obtiene la coordenada Y de la entidad.
     *
     * @return la coordenada Y de la entidad.
     */
    public int getCoordY() {
        return coordY;
    }

    /**
     * Obtiene la coordenada X de la entidad.
     *
     * @return la coordenada X de la entidad.
     */
    public int getCoordX() {
        return coordX;
    }

    /**
     * Obtiene la imagen (skin) que representa a la entidad.
     *
     * @return La imagen de la entidad que es utilizada para mostrarla en la pantalla.
     */
    public Image getSkin() {
        return Skin;
    }
}