package game.casas;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.entidades.npc.NPC;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Clase que maneja las ubicaciones de los pueblos, casas, mazmorras y spawn en el juego.
 * Esta clase carga los datos de un archivo JSON y permite seleccionar diferentes elementos
 * de acuerdo al nivel del jugador.
 */
public class ubicaciones {

    private List<Map<String, Object>> pueblos;
    private List<Map<String, Integer>> casasSeleccionadas;
    private List<String> tamañoCasasSeleccionadas;
    private List<NPC> npcs;
    private Map<String, Integer> mazmorraEntradaSeleccionada;
    private Map<String, Integer> mazmorraPuerta;
    private Map<String, Integer> mazmorraSalidaSeleccionada;
    private Map<String, Integer> spawnSeleccionado;

    /**
     * Constructor de la clase que carga los datos de las ubicaciones desde un archivo JSON.
     *
     * @param nivel El nivel del jugador que determina qué ubicaciones se cargarán.
     */
    public ubicaciones(int nivel) {
        URL res = Objects.requireNonNull(this.getClass().getResource("/basedatos/casas/casas.json"));

        ObjectMapper mapper = new ObjectMapper();

        pueblos = new ArrayList<>();
        try {
            this.pueblos = mapper.readValue(Paths.get(res.toURI()).toFile(), new TypeReference<List<Map<String, Object>>>() {
            });
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        seleccion(nivel);

    }

    /**
     * Selecciona las ubicaciones del pueblo, mazmorras y spawn según el nivel del jugador.
     *
     * @param nivel El nivel del jugador.
     */
    public void seleccion(int nivel) {
        seleccionarPueblo(nivel);
        seleccionarMazmorra(nivel);
        seleccionarSpawn(nivel);
    }

    /**
     * Selecciona las casas del pueblo para el nivel dado.
     *
     * @param nivel El nivel del jugador.
     * @throws IllegalArgumentException Si el nivel no es válido.
     */
    private void seleccionarPueblo(int nivel) {
        if (nivel > 0 && nivel <= pueblos.size()) {
            Map<String, Object> pueblo = pueblos.get(nivel - 1);
            this.casasSeleccionadas = new ArrayList<>();
            this.tamañoCasasSeleccionadas = new ArrayList<>();
            List<?> casas = (List<?>) pueblo.get("casas");
            if (casas != null) {
                for (Object casaObj : casas) {

                    Map<?, ?> casa = (Map<?, ?>) casaObj;
                    if (!(((Map<?, ?>) casaObj).get("casa").equals(0))) {
                        Map<String, Integer> coords = (Map<String, Integer>) casa.get("coords");
                        this.casasSeleccionadas.add(coords);
                        this.tamañoCasasSeleccionadas.add((String) casa.get("tamaño"));
                    }

                }
            }
        } else {
            throw new IllegalArgumentException("Nivel no válido");
        }
    }

    /**
     * Selecciona las mazmorras para el nivel dado.
     *
     * @param nivel El nivel del jugador.
     * @throws IllegalArgumentException Si el nivel no es válido.
     */
    private void seleccionarMazmorra(int nivel) {
        if (nivel > 0 && nivel <= pueblos.size()) {
            Map<String, Object> pueblo = pueblos.get(nivel - 1);
            Map<String, Map<String, Integer>> mazmorras = (Map<String, Map<String, Integer>>) pueblo.get("mazmorras");
            if (mazmorras != null) {
                this.mazmorraEntradaSeleccionada = mazmorras.get("mazmorraEntrada");
                this.mazmorraPuerta = mazmorras.get("mazmorraPuerta");

                this.mazmorraSalidaSeleccionada = mazmorras.get("mazmorraSalida");

            }
        } else {
            throw new IllegalArgumentException("Nivel no válido");
        }
    }

    /**
     * Selecciona el spawn para el nivel dado.
     *
     * @param nivel El nivel del jugador.
     * @throws IllegalArgumentException Si el nivel no es válido.
     */
    private void seleccionarSpawn(int nivel) {
        if (nivel > 0 && nivel <= pueblos.size()) {
            Map<String, Object> pueblo = pueblos.get(nivel - 1);
            Map<String, Integer> spawn = (Map<String, Integer>) pueblo.get("spawn");
            this.spawnSeleccionado = spawn;
        } else {
            throw new IllegalArgumentException("Nivel no válido");
        }
    }

    /**
     * Obtiene la lista de casas seleccionadas para el nivel actual.
     *
     * @return La lista de casas seleccionadas.
     */
    public List<Map<String, Integer>> getCasasSeleccionadas() {
        return casasSeleccionadas;
    }

    /**
     * Obtiene el tamaño de la casa seleccionada en el índice especificado.
     *
     * @param i El índice de la casa.
     * @return El tamaño de la casa seleccionada.
     */
    public String getTamañoCasasSeleccionadas(int i) {
        return tamañoCasasSeleccionadas.get(i);
    }

    /**
     * Obtiene las coordenadas del spawn seleccionado para el nivel actual.
     *
     * @return El mapa de coordenadas del spawn.
     */
    public Map<String, Integer> getSpawnSeleccionado() {
        return spawnSeleccionado;
    }

    /**
     * Obtiene las coordenadas de la puerta de la mazmorra seleccionada.
     *
     * @return El mapa de coordenadas de la puerta de la mazmorra.
     */
    public Map<String, Integer> getMazmorraPuerta() {
        return mazmorraPuerta;
    }

    /**
     * Obtiene la lista de NPCs asignados a este nivel.
     *
     * @return La lista de NPCs.
     */
    public List<NPC> getNpcs() {
        return npcs;
    }

    /**
     * Establece la lista de NPCs para este nivel.
     *
     * @param npcs La lista de NPCs.
     */
    public void setNpcs(List<NPC> npcs) {
        this.npcs = npcs;
    }

    /**
     * Obtiene las coordenadas de la entrada de la mazmorra seleccionada.
     *
     * @return El mapa de coordenadas de la entrada de la mazmorra.
     */
    public Map<String, Integer> getMazmorraEntradaSeleccionada() {
        return mazmorraEntradaSeleccionada;
    }

    /**
     * Obtiene las coordenadas de la salida de la mazmorra seleccionada.
     *
     * @return El mapa de coordenadas de la salida de la mazmorra.
     */
    public Map<String, Integer> getMazmorraSalidaSeleccionada() {
        return mazmorraSalidaSeleccionada;
    }
}
