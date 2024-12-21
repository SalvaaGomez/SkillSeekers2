package game.entidades.npc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Clase responsable de crear instancias de NPCs (Personajes No Jugables) de diferentes tipos.
 * Esta clase utiliza el patrón de diseño Factory para crear objetos NPC de forma flexible
 * según el tipo solicitado.
 */
public class NPCFactory {
    /**
     * Crea un NPC de un tipo especificado, con una imagen (skin), coordenadas (X, Y)
     * y una lista de diálogos para la interacción con el jugador.
     *
     * @param tipo El tipo de NPC a crear (puede ser "bueno", "malo" o "boss").
     * @param coordX La coordenada X donde se ubicará el NPC en el mapa.
     * @param coordY La coordenada Y donde se ubicará el NPC en el mapa.
     * @param dialogos Una lista de listas de diálogos que el NPC mostrará al jugador. (En caso de ser tipo 'malo' en la primera posición pasará una lista de preguntas y seguidamente listas de posibles respuestas a cada pregunta)
     * @return Un objeto NPC del tipo especificado.
     * @throws IllegalArgumentException Si el tipo de NPC proporcionado no es válido.
     */
    public static NPC crearNPC(String tipo, int coordX, int coordY, List<List<String>> dialogos) {
        Image skin;
        switch (tipo) {
            case "bueno":
                try {
                    skin = ImageIO.read(Objects.requireNonNull(CreadorNPC.class.getResource("/images/skins/npc/bueno/npc_bueno_" + (int) Math.floor(Math.random() * 8) + ".png")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return new NPCBueno(skin, coordX, coordY, dialogos.get(0)); // Obtiene el primer conjunto de diálogos que está solo en la primera posicion
            case "malo":
            case "boss":
                try {
                    // Carga la imagen adecuada dependiendo de si es un "boss" o un "malo"
                    if(tipo.equals("boss")){
                        skin = ImageIO.read(Objects.requireNonNull(CreadorNPC.class.getResource("/images/skins/npc/malo/npc_boss.png")));
                    }else{
                        skin = ImageIO.read(Objects.requireNonNull(CreadorNPC.class.getResource("/images/skins/npc/malo/npc_malo_" + (int) Math.floor(Math.random() * 4) + ".png")));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                /* Pasa como dialogos la lista de la primera posicion, elimina el primer conjunto de diálogos y pasa el resto
                   de listas que corresponden a las posibles respuestas de cada dialogo.*/
                return new NPCMalo(skin, coordX, coordY, dialogos.get(0), dialogos);
            default:
                throw new IllegalArgumentException("Tipo de NPC desconocido: " + tipo);
        }
    }
}