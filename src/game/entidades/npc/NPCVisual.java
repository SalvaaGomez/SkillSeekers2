package game.entidades.npc;

import game.Juego;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Clase encargada de manejar la visualización de los NPCs en el mapa del juego.
 * Extiende {@link JPanel} y sobrecarga el método {@link #paintComponent(Graphics)} para dibujar los NPCs
 * en la pantalla en función de sus coordenadas y las del mapa.
 *
 * La posición de los NPCs es ajustada en base a las coordenadas del mapa, permitiendo que se muestren correctamente
 * en la vista actual del jugador.
 */
public class NPCVisual extends JPanel {
    private Juego juego;
    private List<NPC> npcs;

    /**
     * Constructor de la clase NPCVisual.
     *
     * @param e Lista de NPCs a ser visualizados.
     * @param j Instancia del juego, que contiene las coordenadas del mapa y otros parámetros.
     */
    public NPCVisual (List<NPC> e, Juego j){
        juego = j;
        npcs = e;
    }

    /**
     * Sobrescribe el método {@link JPanel#paintComponent(Graphics)} para dibujar los NPCs en la pantalla.
     * Calcula las coordenadas de cada NPC en base a las coordenadas actuales del mapa,
     * y luego dibuja su imagen (skin) en la ubicación correcta.
     *
     * @param g El objeto {@link Graphics} que se utiliza para dibujar en el panel.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (NPC n : npcs){
            // Calcula las coordenadas relativas a la vista actual del mapa
            int cX = -(n.getCoordX() - juego.getMapa_CoordX()) + 960;
            int cY = -(n.getCoordY() - juego.getMapa_CoordY()) + 540;
            g.drawImage(n.getSkin(), cX-20,cY-40,75,75,null);
        }
    }
}
