package game.notificaciones;

import game.Juego;
import game.casas.ubicaciones;
import game.entidades.npc.NPC;
import idiomas.Idioma;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

/**
 * Clase que maneja las notificaciones del juego y las interacciones del jugador con el entorno.
 */
public class c_notificacion {
    private ubicaciones model;
    private final v_notificacion view;
    private int currentX = 0;
    private int currentY = 0;
    private int proximaSalidaX;
    private int proximaSalidaY;
    private boolean habilitarPuertaFinal;
    private int id;
    private String status;
    private String lugar;
    private NPC npcSeleccionado;
    private int numNPC;
    private boolean deshabilitarNPC;

    /**
     * Constructor de la clase c_notificacion.
     *
     * @param model El modelo de ubicaciones que representa el entorno del juego.
     * @param view  La vista de las notificaciones.
     */
    public c_notificacion(ubicaciones model, v_notificacion view) {
        this.model = model;
        this.view = view;
        lugar = "mapa";
    }

    /**
     * Deshabilita la interacción con el NPC.
     *
     * @param b Si es true, se deshabilita la interacción.
     */
    public void deshabilitarNPC(boolean b) {
        deshabilitarNPC = b;
    }

    /**
     * Configura las coordenadas del jugador y maneja las notificaciones según la posición.
     *
     * @param x Coordenada X del jugador.
     * @param y Coordenada Y del jugador.
     */
    public void setCoordinates(int x, int y) {
        this.currentX = x;
        this.currentY = y;
        if (enCasa()) {
            view.mostrarNotificacionEntrarCasa(id);
        } else if (enSalidaCasa()) {
            view.mostrarNotificacionSalirCasa(true);
        } else if (enMazmorra()) {
            view.mostrarNotificacionEntrarMazmorra(true);
        } else if (enSalidaMazmorra()) {
            view.mostrarNotificacionSalirMazmorra(true);
        } else if (enSalidaFinalMazmorra()) {
            view.mostrarNotificacionSalirMazmorra(true);
        } else if (conNPC() && !deshabilitarNPC) {
            if (lugar.equals("mazmorra")) {
                if (model.getMazmorraSalidaSeleccionada() != null) {
                    view.mostrarNotificacionConNPC(numNPC, "bug");
                } else {
                    view.mostrarNotificacionConNPC(numNPC, "bugger");
                }
            } else if (lugar.equals("mapa")) {
                view.mostrarNotificacionConNPC(numNPC, "sabio");
            } else {
                view.mostrarNotificacionConNPC(numNPC, "aldeano");
            }
        } else {
            status = "mapa";
            view.noMostrarNotificacion();
        }

    }

    /**
     * Verifica si el jugador está dentro de una casa.
     *
     * @return true si el jugador está dentro de una casa, false en caso contrario.
     */
    private boolean enCasa() {
        List<Map<String, Integer>> casas = model.getCasasSeleccionadas();
        for (int i = 0; i < casas.size(); i++) {

            int casaX = casas.get(i).get("coordX");
            int casaY = casas.get(i).get("coordY");

            if (Math.abs(currentX - casaX) < 20.0 && Math.abs(currentY - casaY) < 20.0) {
                id = i + 1;
                status = model.getTamañoCasasSeleccionadas(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si el jugador está en la salida de una casa.
     *
     * @return true si el jugador está en la salida de una casa, false en caso contrario.
     */
    private boolean enSalidaCasa() {
        if (("grande".equals(lugar) || ("pequeño".equals(lugar)))) {
            int casaX;
            int casaY;

            if ("grande".equals(lugar)) {
                casaX = 14;
                casaY = -288;
            } else {
                casaX = 18;
                casaY = -230;
            }

            return Math.abs(currentX - casaX) < 20.0 && Math.abs(currentY - casaY) < 20.0;
        }
        return false;
    }

    /**
     * Verifica si el jugador está cerca de un NPC.
     *
     * @return true si el jugador está cerca de un NPC, false en caso contrario.
     */
    private boolean conNPC() {
        List<NPC> npcs = model.getNpcs();
        if (npcs != null && !"npc".equals(lugar)) {
            for (int i = 0; i < npcs.size(); i++) {

                int coordX = npcs.get(i).getCoordX();
                int coordY = npcs.get(i).getCoordY();

                if (Math.abs(currentX - coordX) < 40.0 && Math.abs(currentY - coordY) < 40.0) {
                    npcSeleccionado = npcs.get(i);
                    numNPC = i;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Verifica si el jugador está cerca de la entrada de una mazmorra.
     *
     * @return true si el jugador está cerca de la entrada de una mazmorra, false en caso contrario.
     */
    private boolean enMazmorra() {
        Map<String, Integer> mazmorra = model.getMazmorraEntradaSeleccionada();

        int mazmorraX = mazmorra.get("coordX");
        int mazmorraY = mazmorra.get("coordY");
        status = "mazmorra";
        return Math.abs(currentX - mazmorraX) < 20.0 && Math.abs(currentY - mazmorraY) < 20.0;
    }

    /**
     * Verifica si el jugador está en la salida de una mazmorra.
     *
     * @return true si el jugador está en la salida de una mazmorra, false en caso contrario.
     */
    private boolean enSalidaMazmorra() {
        if (("mazmorra".equals(lugar))) {
            Map<String, Integer> mazmorra = model.getMazmorraPuerta();

            int mazmorraX = mazmorra.get("coordX");
            int mazmorraY = mazmorra.get("coordY");
            status = "mazmorra";
            return Math.abs(currentX - mazmorraX) < 20.0 && Math.abs(currentY - mazmorraY) < 20.0;
        }
        return false;
    }

    /**
     * Verifica si el jugador está en la salida final de una mazmorra.
     *
     * @return true si el jugador está en la salida final de una mazmorra, false en caso contrario.
     */
    private boolean enSalidaFinalMazmorra() {
        if (("mazmorra".equals(lugar))) {
            if (habilitarPuertaFinal) {
                Map<String, Integer> mazmorra = model.getMazmorraSalidaSeleccionada();
                if (mazmorra != null) {
                    int mazmorraX = mazmorra.get("coordX");
                    int mazmorraY = mazmorra.get("coordY");
                    return Math.abs(currentX - mazmorraX) < 20.0 && Math.abs(currentY - mazmorraY) < 20.0;
                }
            }
        }
        return false;
    }

    /**
     * Configura un listener de teclas para las interacciones del jugador con el mundo.
     *
     * @param j El objeto Juego que maneja las interacciones del jugador.
     */
    public void setupKeyListener(Juego j) {
        j.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_E && !deshabilitarNPC) {
                    if (enCasa()) {
                        proximaSalidaX = currentX;
                        proximaSalidaY = currentY;
                        lugar = status;
                        j.cambiarMapa(status, id);
                    } else if (enSalidaCasa()) {
                        status = "mapa";
                        lugar = status;
                        j.cambiarMapa(status, 0);
                    } else if (conNPC()) {
                        j.interaccionNPC(npcSeleccionado, numNPC, lugar);
                    } else if (enMazmorra()) {
                        proximaSalidaX = currentX;
                        proximaSalidaY = currentY;
                        j.setMapa_CoordX(model.getMazmorraPuerta().get("coordX"));
                        j.setMapa_CoordY(model.getMazmorraPuerta().get("coordY"));
                        lugar = status;
                        j.cambiarMapa(status, -1);
                    } else if (enSalidaMazmorra()) {
                        status = "mapa";
                        lugar = status;
                        j.cambiarMapa(status, 0);
                    } else if (enSalidaFinalMazmorra()) {
                        status = "mapa";
                        lugar = status;
                        j.subirNivel();
                        proximaSalidaX = model.getSpawnSeleccionado().get("coordX");
                        proximaSalidaY = model.getSpawnSeleccionado().get("coordY");
                        j.cambiarMapa(status, 0);
                        j.guardarJuego();
                        j.getNotisPanel().mostrarNotiTemporal((Idioma.getRb().getString("guardando")), 2);
                    }


                }
            }
        });
    }

    /**
     * Obtiene la coordenada X de la próxima salida.
     *
     * @return La coordenada X de la próxima salida.
     */
    public int getProximaSalidaX() {
        return proximaSalidaX;
    }

    /**
     * Obtiene la coordenada Y de la próxima salida.
     *
     * @return La coordenada Y de la próxima salida.
     */
    public int getProximaSalidaY() {
        return proximaSalidaY;
    }

    /**
     * Establece si se debe habilitar la puerta final.
     *
     * @param habilitarPuertaFinal Si es true, habilita la puerta final; si es false, la deshabilita.
     */
    public void setHabilitarPuertaFinal(boolean habilitarPuertaFinal) {
        this.habilitarPuertaFinal = habilitarPuertaFinal;
    }

    /**
     * Reinicia el modelo de ubicaciones con el nuevo objeto proporcionado.
     *
     * @param m El nuevo modelo de ubicaciones que reemplaza el modelo actual.
     */
    public void resetModelo(ubicaciones m) {
        model = m;
    }
}
