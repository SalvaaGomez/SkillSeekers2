package game.entidades.npc;

import game.Juego;
import game.entidades.Entidad;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.util.List;
/**
 * Clase abstracta que representa a un NPC (personaje no jugable) en el juego.
 * Un NPC tiene una apariencia visual (skin), una posición en el espacio del juego,
 * y un conjunto de diálogos que pueden ser mostrados durante la interacción con el jugador.
 */
public abstract class NPC extends Entidad {

    protected Juego juego;
    protected List<String> dialogos;
    protected int indice;
    protected int id;

    /**
     * Constructor para crear un NPC con una imagen, coordenadas y un conjunto de diálogos.
     *
     * @param skin la imagen que representa al NPC.
     * @param coordX la coordenada X del NPC en el mundo del juego.
     * @param coordY la coordenada Y del NPC en el mundo del juego.
     * @param dialogos la lista de diálogos que el NPC puede mostrar.
     */
    public NPC(Image skin, int coordX, int coordY, List<String> dialogos) {
        super(skin, coordX, coordY);
        this.dialogos = dialogos;
    }
    /**
     * Establece el identificador del NPC.
     *
     * @param id el identificador único del NPC.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Establece la instancia del juego para la interacción del NPC.
     *
     * @param j la instancia del juego.
     */
    public void setJuego(Juego j) {
        this.juego = j;
    }
    /**
     * Método abstracto para mostrar el diálogo del NPC.
     * Este método debe ser especializado por cada tipo de NPC.
     */
    public abstract void mostrarDialogo(); // Especializado por tipo de NPC.
    /**
     * Método abstracto para avanzar en el diálogo.
     * Este método debe ser especializado por cada tipo de NPC.
     *
     * @return un {@link KeyAdapter} que gestiona el avance del diálogo mediante las teclas.
     */
    public abstract KeyAdapter avanzarDialogo();
    /**
     * Finaliza la interacción con el NPC, reseteando el índice del diálogo,
     * ocultando el panel de diálogo y deshabilitando las acciones relacionadas.
     */
    protected void finalizarInteraccion() {
        indice = 0;
        juego.getDialogoPanel().setVisible(false);
        juego.removeKeyListener(juego.getTeclas());
        juego.getControlNotis().deshabilitarNPC(false);
    }

}
