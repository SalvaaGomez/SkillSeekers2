package game.entidades.npc;


import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
/**
 * Representa a un NPC (Personaje No Jugable) de tipo "Bueno" en el juego.
 * Este tipo de NPC puede interactuar con el jugador mostrando diálogos que avanzan
 * cuando el jugador presiona la tecla correspondiente.
 */
public class NPCBueno extends NPC {

    /**
     * Constructor para crear un NPCBueno con una imagen (skin), coordenadas (X, Y)
     * y una lista de diálogos asociados a este NPC.
     *
     * @param skin La imagen que representa al NPC en el juego.
     * @param coordX La coordenada X del NPC en el mapa.
     * @param coordY La coordenada Y del NPC en el mapa.
     * @param dialogos La lista de diálogos que el NPC mostrará al jugador.
     */
    public NPCBueno(Image skin, int coordX, int coordY, List<String> dialogos) {
        super(skin, coordX, coordY, dialogos);
    }

    /**
     * Muestra el siguiente diálogo del NPC al jugador. Si ya no hay más diálogos
     * por mostrar, finaliza la interacción.
     */
    @Override
    public void mostrarDialogo() {
        if (indice < dialogos.size()) {
            juego.getDialogoPanel().setTextoDialogo(dialogos.get(indice));
            indice += 1;

        } else {
            finalizarInteraccion();
        }
    }

    /**
     * Devuelve un objeto {@link KeyAdapter} para gestionar las teclas del jugador durante la
     * interacción con el NPC. El diálogo avanza al presionar la tecla espacio, y
     * finaliza la interacción si se presiona la tecla escape.
     *
     * @return Un objeto {@link KeyAdapter} que maneja las teclas para avanzar en el diálogo o finalizar la interacción.
     */
    @Override
    public KeyAdapter avanzarDialogo() {
        KeyAdapter teclasDialogo = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (juego.getDialogoPanel().isVisible()){
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        finalizarInteraccion();

                    } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        mostrarDialogo();
                    }
                }

            }
        };
        return teclasDialogo;
    }
}