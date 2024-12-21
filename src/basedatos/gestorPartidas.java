package basedatos;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

/**
 * Clase encargada de gestionar las partidas almacenadas en un archivo JSON.
 * Proporciona métodos para leer, crear y editar partidas.
 */
public class gestorPartidas {
    private ObjectMapper mapper = new ObjectMapper();

    private File archivo;


    private List<Partidas> partidas;

    /**
     * Constructor de la clase. Inicializa el archivo de partidas leyendo el archivo JSON desde los recursos.
     */
    public gestorPartidas() {
        URL res = Objects.requireNonNull(this.getClass().getResource("/basedatos/partidas/partidas.json"));

        try {
            archivo = Paths.get(res.toURI()).toFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lee el archivo de partidas y carga las partidas en la lista.
     * Si el archivo está vacío o no existe, se crea una lista vacía.
     */
    private void leerArchivo() {
        try {
            if (archivo.exists() && archivo.length() != 0) {
                partidas = mapper.readValue(archivo, new TypeReference<List<Partidas>>() {
                });
            } else {
                partidas = new ArrayList<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Devuelve la última partida en la lista de partidas.
     *
     * @return La última partida en la lista.
     */
    public Partidas getPartidas() {
        return partidas.get(partidas.size() - 1);
    }

    /**
     * Crea una nueva partida y la guarda en el archivo de partidas.
     *
     * @param nombre   El nombre del jugador.
     * @param lenguaje El lenguaje seleccionado para la partida.
     */
    public void nuevaPartida(String nombre, String lenguaje) {
        leerArchivo();
        Partidas nuevaPartida = new Partidas();
        UUID id = UUID.randomUUID();
        nuevaPartida.setId(id);
        nuevaPartida.setUser(nombre);
        nuevaPartida.setLenguaje(lenguaje);
        nuevaPartida.setUrl(lenguaje.toLowerCase() + ".json");
        nuevaPartida.setNivel(1);
        nuevaPartida.setFecha(String.valueOf(new Date()));

        // Añadir la nueva persona a la lista existente
        partidas.add(nuevaPartida);
        if (partidas.size() > 8) {
            partidas.get(0);
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, partidas);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Edita una partida existente en el archivo de partidas.
     * Si la partida con el mismo ID ya existe, la reemplaza con la nueva.
     *
     * @param p La partida que se quiere editar.
     */
    public void editarPartida(Partidas p) {
        leerArchivo();
        for (Partidas partida : partidas) {
            if (p.getId().equals(partida.getId())) {
                partidas.remove(partida);
                partidas.add(p);
                try {
                    mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, partidas);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }
}
