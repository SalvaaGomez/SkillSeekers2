package game.entidades.pj;

import javax.swing.*;
import java.awt.*;
/**
 * Clase encargada de representar gráficamente al personaje en el panel de visualización del juego.
 * Esta clase extiende {@link JPanel} y se encarga de dibujar la imagen del personaje
 * en su posición actual dentro de la ventana del juego.
 */
public class PersonajeVisual extends JPanel {

    private final Personaje pj;
    /**
     * Constructor de la clase {@link PersonajeVisual}.
     *
     * @param e Instancia del personaje que será visualizado.
     */
    public PersonajeVisual (Personaje e){
        pj = e;
    }
    /**
     * Método encargado de dibujar la imagen del personaje en el panel.
     * Este método se llama automáticamente cuando el panel necesita ser repintado.
     *
     * @param g El objeto {@link Graphics} utilizado para dibujar la imagen.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(pj.getSkin(), pj.getCoordX() - 20, pj.getCoordY() - 40, 75, 75, null);
    }

}
