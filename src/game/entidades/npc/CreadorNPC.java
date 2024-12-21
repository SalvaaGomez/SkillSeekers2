package game.entidades.npc;

import com.fasterxml.jackson.databind.ObjectMapper;
import idiomas.Idioma;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Clase responsable de la creación y gestión de NPCs (personajes no jugadores) en el juego.
 * Esta clase lee y procesa archivos JSON para generar NPCs con diferentes características,
 * diálogos y preguntas.
 * <p>
 * Utiliza archivos JSON para obtener la información de los NPCs, los diálogos asociados
 * y las preguntas disponibles, creando los NPCs en función de la ubicación en el juego.
 */
public class CreadorNPC {
    private List<Map<String, Object>> pueblosJSON;
    private List<Map<String, Object>> dialogosJSON;
    private List<Map<String, Object>> preguntasJSON;
    /**
     * Método principal que genera una lista de NPCs a partir de los datos de pueblos, casas y diálogos.
     * <p>
     * Carga los archivos JSON necesarios para obtener la información de los NPCs en el pueblo y casa
     * especificados. Retorna una lista de NPCs con sus respectivas ubicaciones y diálogos.
     *
     * @param idPueblo el ID del pueblo donde se deben generar los NPCs.
     * @param idCasa el ID de la casa donde se deben generar los NPCs. Si es -1, no se selecciona una casa específica.
     * @param URL la URL que se utiliza para acceder a los archivos JSON correspondientes a diálogos y preguntas.
     * @return una lista de NPCs generados en el pueblo y casa especificados.
     */
    public List<NPC> spawnearNPCs(int idPueblo, int idCasa, String URL) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            pueblosJSON = objectMapper.readValue(new File(Objects.requireNonNull(CreadorNPC.class.getResource("/basedatos/casas/casas.json")).toURI()), List.class);
            dialogosJSON = objectMapper.readValue(new File(Objects.requireNonNull(CreadorNPC.class.getResource("/basedatos/juegos/"+URL+"/"+URL+"_"+Idioma.getRb().getString("Extension")+".json")).toURI()), List.class);
            preguntasJSON = objectMapper.readValue(new File(Objects.requireNonNull(CreadorNPC.class.getResource("/basedatos/juegos/"+URL+"/"+URL+"_preguntas_"+Idioma.getRb().getString("Extension")+".json")).toURI()), List.class);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return obtenerDatosNPC(idPueblo, idCasa);
    }
    /**
     * Obtiene los datos de los NPCs para un pueblo y casa específicos.
     * <p>
     * Recorre los pueblos cargados desde el archivo JSON y filtra los NPCs correspondientes
     * al pueblo y la casa especificada. Si el ID de la casa es -1, se obtiene NPCs de otras áreas
     * como mazmorras.
     *
     * @param puebloId el ID del pueblo donde se deben generar los NPCs.
     * @param casaId el ID de la casa donde se deben generar los NPCs. Si es -1, se obtienen NPCs de mazmorras.
     * @return una lista de NPCs generados para el pueblo y casa especificados.
     */
    private List<NPC> obtenerDatosNPC(int puebloId, int casaId) {
        List<NPC> lista = new ArrayList<>();
        for (Map<String, Object> pueblo : pueblosJSON) {
            if ((int) pueblo.get("pueblo") == puebloId) {
                // Obtener las casas
                if(casaId != -1) {
                    List<Map<String, Object>> casas = (List<Map<String, Object>>) pueblo.get("casas");
                    if (casas != null) {
                        for (Map<String, Object> casa : casas) {
                            if ((int) casa.get("casa") == casaId) {
                                List<Map<String, Object>> npcs = (List<Map<String, Object>>) casa.get("npc");
                                for (Map<String, Object> npc : npcs) {
                                    int npcId = (int) npc.get("npcID");
                                    // Obtener las coordenadas del NPC
                                    Map<String, Integer> coordenadas = (Map<String, Integer>) npc.get("coordenadas");
                                    int coordX = coordenadas.get("coordX");
                                    int coordY = coordenadas.get("coordY");
                                    List<List<String>> dialogos = new ArrayList<>();
                                    dialogos.add(obtenerDialogosPorNpcId(npcId));
                                    lista.add(NPCFactory.crearNPC("bueno", coordX, coordY, dialogos));
                                }
                            }
                        }
                    }
                } else {
                    Map<String, Object> mazmorras = (Map<String, Object>) pueblo.get("mazmorras");
                    List<Map<String, Object>> npcs = (List<Map<String, Object>>) mazmorras.get("npc");
                    for (Map<String, Object> npc : npcs) {
                        int npcId = (int) npc.get("npcID");
                        // Obtener las coordenadas del NPC
                        Map<String, Integer> coordenadas = (Map<String, Integer>) npc.get("coordenadas");
                        int coordX = coordenadas.get("coordX");
                        int coordY = coordenadas.get("coordY");
                        List<List<String>> dialogos = obtenerPreguntasPorNpcId(npcId);
                        if(puebloId == 5){
                            lista.add(NPCFactory.crearNPC("boss", coordX, coordY, dialogos));
                        }else {
                            lista.add(NPCFactory.crearNPC("malo", coordX, coordY, dialogos));
                        }
                    }
                }
            }
        }
        return lista;
    }
    /**
     * Obtiene los diálogos asociados a un NPC específico.
     * <p>
     * Filtra los diálogos por el ID del NPC y devuelve la lista de diálogos correspondientes.
     *
     * @param npcId el ID del NPC cuyo diálogo se debe obtener.
     * @return una lista de diálogos del NPC especificado.
     */
    private List<String> obtenerDialogosPorNpcId(int npcId) {
        for (Map<String, Object> dialogo : dialogosJSON) {
            if ((int) dialogo.get("npcID") == npcId) {
                return (List<String>) dialogo.get("dialogos");
            }
        }
        return Collections.emptyList();
    }
    /**
     * Obtiene las preguntas y respuestas asociadas a un NPC específico.
     * <p>
     * Filtra las preguntas por el ID del NPC y devuelve una lista que contiene el enunciado
     * de la pregunta y las respuestas posibles.
     *
     * @param npcId el ID del NPC cuyas preguntas se deben obtener.
     * @return una lista de listas de preguntas y respuestas del NPC especificado.
     */
    private List<List<String>> obtenerPreguntasPorNpcId(int npcId) {
        for (Map<String, Object> pregunta : preguntasJSON) {
            if ((int) pregunta.get("npcID") == npcId) {
                List<List<String>> dialogos = new ArrayList<>();
                List<String> enunciado = (List<String>) pregunta.get("pregunta");
                List<List<String>> respuesta = (List<List<String>>) pregunta.get("respuestas");
                dialogos.add(enunciado);
                dialogos.addAll(respuesta);
                return dialogos;
            }
        }
        return Collections.emptyList();
    }
}