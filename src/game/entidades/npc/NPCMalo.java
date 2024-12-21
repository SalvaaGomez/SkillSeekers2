package game.entidades.npc;

import idiomas.Idioma;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
/**
 * Clase que representa a un NPC de tipo "malo" en el juego. Este NPC tiene una serie de diálogos
 * y preguntas con opciones de respuesta que el jugador debe contestar correctamente.
 * En caso de respuesta correcta, avanza al siguiente diálogo; si no, muestra un mensaje de error.
 * Además, puede afectar el progreso del jugador en el nivel (mazmorras).
 */
public class NPCMalo extends NPC {
    private final List<List<String>> respuestas;
    /**
     * Constructor de la clase NPCMalo.
     *
     * @param skin La imagen (skin) que representa al NPC.
     * @param coordX La coordenada X del NPC en el mapa.
     * @param coordY La coordenada Y del NPC en el mapa.
     * @param dialogos Lista de diálogos que el NPC mostrará al jugador.
     * @param respuestas Lista de respuestas posibles para las preguntas del NPC.
     */
    public NPCMalo(Image skin, int coordX, int coordY, List<String> dialogos, List<List<String>> respuestas) {
        super(skin, coordX, coordY, dialogos);

        this.respuestas = respuestas;
    }
    /**
     * Muestra el diálogo del NPC. Si es la primera vez que se interactúa con el NPC,
     * se muestra el diálogo y las opciones de respuesta. Si ya se han preguntado todas las preguntas,
     * se marca al NPC como derrotado, mostrando un mensaje de notificación.
     * Si todos los NPCs han sido derrotados, se desbloquea una puerta.
     */
    @Override
    public void mostrarDialogo() {
        if(!juego.getNpclist()[id]) { // Verifica si el NPC ya ha sido derrotado
            if (indice < dialogos.size()) {
                juego.getDialogoPanel().setTextoDialogo(dialogos.get(indice)); // Muestra el diálogo actual
                juego.getDialogoPanel().setOpciones(respuestas.get(indice)); // Muestra las opciones de respuesta
            } else {
                // Marca al NPC como derrotado y muestra un mensaje
                juego.getNpclist()[id] = true;
                finalizarInteraccion();
                juego.getNotisPanel().mostrarNotiTemporal((Idioma.getRb().getString("derrotado")), 3); // Notificación de derrota
                // Si todos los NPCs han sido derrotados, desbloquea una puerta
                if (mazmorraTerminada(juego.getNpclist())) {
                    juego.getNotisPanel().mostrarNotiTemporal((Idioma.getRb().getString("nivel")), 5); // Notificación de nivel completado
                    juego.habilitarPuerta();// Desbloquea la puerta
                }
            }
        }
    }
    /**
     * Devuelve un {@link KeyAdapter} que captura las teclas presionadas durante la interacción con el NPC.
     * Si la tecla presionada es "ESCAPE", finaliza la interacción. Si es "ENTER", valida la respuesta seleccionada
     * por el jugador y avanza en los diálogos si la respuesta es correcta.
     *
     * @return Un {@link KeyAdapter} para manejar las interacciones de teclado.
     */
    @Override
    public KeyAdapter avanzarDialogo() {
        KeyAdapter teclasDialogo = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (juego.getDialogoPanel().isVisible()){
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        finalizarInteraccion(); // Finaliza la interacción si se presiona ESCAPE

                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        JButton respuesta = juego.getDialogoPanel().getSeleccionActual(); // Obtiene la respuesta seleccionada
                        if(respuesta != null) {
                            String resp = respuesta.getText(); // Obtiene el texto de la respuesta
                            if (validarRespuesta(resp)) {
                                indice += 1;
                                mostrarDialogo(); // Avanza al siguiente diálogo

                            } else {
                                juego.getNotisPanel().mostrarNotiTemporal((Idioma.getRb().getString("incorrecto")),5); // Muestra notificación de respuesta incorrecta
                                finalizarInteraccion(); // Finaliza la interacción
                            }
                        }else{
                            juego.getNotisPanel().mostrarNotiTemporal((Idioma.getRb().getString("incompleto")),3);  // Muestra notificación si no se ha seleccionado respuesta
                        }
                    }
                }
            }
        };
        return teclasDialogo;
    }
    /**
     * Valida si la respuesta seleccionada por el jugador es correcta.
     *
     * @param respuesta La respuesta seleccionada por el jugador.
     * @return {@code true} si la respuesta es correcta, {@code false} si no lo es.
     */
    public boolean validarRespuesta(String respuesta){
        return respuesta.equals(respuestas.get(indice).get(0)); // La respuesta correcta es la primera en la lista
    }

    /**
     * Verifica si todos los NPCs han sido derrotados, lo cual indicaría que la mazmorras ha sido completada.
     *
     * @param listaNPC Un arreglo de booleanos que indica si un NPC ha sido derrotado (true) o no (false).
     * @return {@code true} si todos los NPCs han sido derrotados, {@code false} de lo contrario.
     */
    public boolean mazmorraTerminada(boolean[] listaNPC){
        for(boolean b : listaNPC){
            if(!b){
                return false; // Si algún NPC no ha sido derrotado, la mazmorras no está terminada
            }
        }
        return true; // Todos los NPCs han sido derrotados, la mazmorras está terminada
    }
}