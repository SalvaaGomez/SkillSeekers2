package game;


import basedatos.Partidas;
import basedatos.gestorPartidas;
import game.casas.ubicaciones;
import game.entidades.npc.CreadorNPC;
import game.entidades.npc.NPC;
import game.entidades.npc.NPCVisual;
import game.entidades.pj.Personaje;
import game.entidades.pj.PersonajeVisual;
import game.mapa.Mapa;
import game.mapa.MapaVisual;
import game.notificaciones.c_notificacion;
import game.notificaciones.v_notificacion;
import idiomas.Idioma;
import pantallas.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

/**
 * Clase principal del juego, encargada de gestionar la lógica de juego, actualización y visualización.
 * Controla la interacción entre el mapa, los personajes y las notificaciones.
 */
public class Juego extends JLayeredPane implements Runnable {

    private int Mapa_CoordX;
    private int Mapa_CoordY;

    private c_notificacion controlNotis;
    private v_notificacion notisPanel;
    private ubicaciones modeloNotis;
    private MenuJuego menuPanel;
    private Dialogos dialogoPanel;
    private boolean mostrarMenu = false;

    private Thread hilo;
    private final KeysConfig keysConfig = new KeysConfig();
    private KeyAdapter teclas;
    private final Partidas partidaActual;
    private final gestorPartidas gestorPartidas = new gestorPartidas();

    private final MapaVisual mapaPanel;
    private NPCVisual npcPanel;

    private Personaje prota;
    private List<NPC> npcs;
    private boolean[] npclist;
    private final CreadorNPC Creador = new CreadorNPC();

    private final Mapa mapa = new Mapa("/mapas/sk2.tmx");
    private final Mapa casaPequeña = new Mapa("/mapas/casa2.tmx");
    private final Mapa casaGrande = new Mapa("/mapas/casa1.tmx");
    private Mapa mapaActual = mapa;

    private GUI gui;

    /**
     * Constructor del juego. Inicializa las variables necesarias y configura los componentes de la interfaz.
     *
     * @param p La partida actual que se cargará al inicio del juego.
     */
    public Juego(Partidas p) {
        this.partidaActual = p;
        this.setLayout(null);
        addKeyListener(keysConfig);
        setFocusable(true);

        mapaPanel = new MapaVisual(mapaActual);
        mapaPanel.setBackground(new Color(125, 219, 206));
        mapaPanel.setBounds(0, 0, GUI.getAnchoPantalla(), GUI.getAltoPantalla());
        mapaPanel.setVisible(true);
        this.add(mapaPanel, Integer.valueOf(1)); // Fondo
        if (p.getNivel() == 6) {
            Mapa_CoordX = 18;
            Mapa_CoordY = 84;
            cambiarMapa("mazmorra", -1);
            finalizarjuego();
        } else {
            prota = new Personaje(this);
            PersonajeVisual pjPanel = new PersonajeVisual(prota);
            modeloNotis = new ubicaciones(prota.getNivel());
            notisPanel = new v_notificacion();
            controlNotis = new c_notificacion(modeloNotis, notisPanel);
            controlNotis.setupKeyListener(this);
            if (partidaActual.getCoordX() == 0 && partidaActual.getCoordY() == 0) {
                Mapa_CoordY = modeloNotis.getSpawnSeleccionado().get("coordY");
                Mapa_CoordX = modeloNotis.getSpawnSeleccionado().get("coordX");
                notisPanel.mostrarNotiTemporal((Idioma.getRb().getString("NotiInicio")), 7);
            } else {
                Mapa_CoordX = partidaActual.getCoordX();
                Mapa_CoordY = partidaActual.getCoordY();
            }

            notisPanel.setBounds(0, 0, GUI.getAnchoPantalla(), GUI.getAltoPantalla());
            notisPanel.setOpaque(false);
            notisPanel.setVisible(true);

            pjPanel.setBounds(0, 0, GUI.getAnchoPantalla(), GUI.getAltoPantalla());
            pjPanel.setOpaque(false);
            pjPanel.setVisible(true);

            dialogoPanel = new Dialogos();

            mostrarNPC(0);

            this.add(npcPanel, Integer.valueOf(2)); // NPC's
            this.add(pjPanel, Integer.valueOf(3)); // Personaje
            this.add(notisPanel, Integer.valueOf(4)); // Notificaciones
            this.add(dialogoPanel, Integer.valueOf(5)); // Dialogos

            addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    if (!getDialogoPanel().isVisible()) {
                        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            mostrarMenu = !mostrarMenu;
                            menuPanel.setVisible(mostrarMenu);
                        }
                    }

                }
            });
            crearMenuPanel();

            startGame();
        }
    }

    /**
     * Crea el panel del menú del juego con las acciones para las opciones del menú.
     */
    private void crearMenuPanel() {
        menuPanel = new MenuJuego(
                e -> cerrarMenu(),            // Acción para volver al juego
                e -> irAlMenuPrincipal(),     // Acción para ir al menú principal
                e -> guardarYSalir()          // Acción para guardar y salir
        );
        this.add(menuPanel, Integer.valueOf(6));
    }

    /**
     * Cierra el menú del juego.
     */
    private void cerrarMenu() {
        mostrarMenu = false;
        menuPanel.setVisible(false);
    }

    /**
     * Cambia al menú principal.
     */
    private void irAlMenuPrincipal() {
        if (partidaActual.getNivel() != 6) {
            guardarJuego();
        }
        gui.Menu();
    }

    /**
     * Guarda el progreso del juego y cierra la aplicación.
     */
    private void guardarYSalir() {

        guardarJuego();
        System.exit(0);
    }

    /**
     * Inicia el juego en un nuevo hilo para actualizar el estado y la pantalla.
     */
    public void startGame() {
        hilo = new Thread(this);
        hilo.start();
    }

    /**
     * Método ejecutado en el hilo del juego para actualizar el estado y pintar la pantalla a 60 FPS.
     */
    @Override
    public void run() {
        while (hilo != null) {
            update();
            repaint();
            requestFocusInWindow();
            try {
                Thread.sleep((long) 1000 / 120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Actualiza los elementos del juego (personaje, mapa y control de notificaciones).
     */
    public void update() {

        if (!mostrarMenu) { // Actualizar solo si el menú no está visible
            prota.updatePJ();

            if (controlNotis != null) {
                controlNotis.setCoordinates(Mapa_CoordX, Mapa_CoordY);
            }
            if (mapaPanel != null) {
                mapaPanel.setCordenadas(Mapa_CoordX, Mapa_CoordY);


            }
        }
    }

    /**
     * Cambia el mapa actual y ajusta las coordenadas del jugador dependiendo del lugar.
     *
     * @param sitio El nombre del lugar donde se cambiará el mapa (puede ser "pequeño", "grande", "mazmorra", etc.).
     * @param id    El identificador del NPC o evento en el mapa actual.
     */
    public void cambiarMapa(String sitio, int id) {
        MapaVisual mv;
        switch (sitio) {
            case "pequeño":
                Mapa_CoordX = 18;
                Mapa_CoordY = -230;
                mapaActual = casaPequeña;
                prota.setMapa();
                mv = new MapaVisual(mapaActual);
                mv.setBackground(new Color(6, 6, 6));
                mostrarNPC(id);
                break;
            case "grande":
                Mapa_CoordX = 14;
                Mapa_CoordY = -288;
                mapaActual = casaGrande;
                prota.setMapa();
                mv = new MapaVisual(mapaActual);
                mv.setBackground(new Color(6, 6, 6));
                mostrarNPC(id);
                break;
            case "mazmorra":
                if (partidaActual.getNivel() == 6) {
                    mapaActual = new Mapa("/mapas/mazmorra5.tmx");
                } else {
                    mapaActual = new Mapa("/mapas/mazmorra" + prota.getNivel() + ".tmx");
                    prota.setMapa();
                    mostrarNPC(id);
                    if (npclist == null || !mazmorraEmpezada(npclist)) {
                        npclist = new boolean[npcs.size()];
                        Arrays.fill(npclist, false);
                    }
                }
                mv = new MapaVisual(mapaActual);
                mv.setBackground(new Color(6, 6, 6));
                break;
            case "mapa":
                Mapa_CoordX = controlNotis.getProximaSalidaX();
                Mapa_CoordY = controlNotis.getProximaSalidaY();
                mapaActual = mapa;
                prota.setMapa();
                mv = mapaPanel;
                mv.setBackground(new Color(125, 219, 206));
                if (prota.getNivel() == 1 || prota.getNivel() == 5) {
                    mostrarNPC(id);
                } else {
                    npcPanel = null;
                }
                break;
            default:
                throw new IllegalArgumentException("Lugar no válido");
        }

        for (Component comp : this.getComponents()) {
            if (this.getLayer(comp) == 1 || this.getLayer(comp) == 2) {
                this.remove(comp);
            }
        }
        mv.setBounds(0, 0, GUI.getAnchoPantalla(), GUI.getAltoPantalla());
        mv.setVisible(true);
        this.add(mv, Integer.valueOf(1)); // Fondo
        if (npcPanel != null) {
            this.add(npcPanel, Integer.valueOf(2));
        }

        revalidate();
        repaint();

    }

    /**
     * Muestra los NPCs en el mapa dependiendo del nivel y la ubicación.
     *
     * @param id El identificador de los NPCs que deben ser mostrados.
     */
    public void mostrarNPC(int id) {
        npcs = Creador.spawnearNPCs(prota.getNivel(), id, partidaActual.getLenguaje().toLowerCase());
        modeloNotis.setNpcs(npcs);
        npcPanel = new NPCVisual(npcs, this);
        npcPanel.setBounds(0, 0, GUI.getAnchoPantalla(), GUI.getAltoPantalla());
        npcPanel.setOpaque(false);
        npcPanel.setVisible(true);
    }

    /**
     * Gestiona la interacción con un NPC, mostrando su diálogo.
     *
     * @param npc    El NPC con el que se interactúa.
     * @param numNpc El número identificador del NPC.
     * @param lugar  El lugar donde ocurre la interacción (puede ser "mazmorra", "mapa", etc.).
     */
    public void interaccionNPC(NPC npc, int numNpc, String lugar) {
        if (!lugar.equals("mazmorra") || (!npclist[numNpc] && lugar.equals("mazmorra"))) {
            controlNotis.deshabilitarNPC(true);

            npc.setJuego(this);
            npc.setId(numNpc);
            if (lugar.equals("mapa") || prota.getNivel() == 5) {
                dialogoPanel.setNumNPC(-1);
            } else {
                dialogoPanel.setNumNPC(numNpc);
            }
            dialogoPanel.setVisible(true);

            npc.mostrarDialogo();
            teclas = npc.avanzarDialogo();
            addKeyListener(teclas);
        } else {
            notisPanel.mostrarNotiTemporal((Idioma.getRb().getString("yaDerrotado")), 3);
        }

    }

    /**
     * Verifica si ya se ha comenzado la mazmorras al comprobar los NPCs derrotados.
     *
     * @param listaNPC Lista de NPCs derrotados.
     * @return true si algún NPC ha sido derrotado, false en caso contrario.
     */
    public boolean mazmorraEmpezada(boolean[] listaNPC) {
        for (boolean b : listaNPC) {
            if (b) {
                return true;
            }
        }
        return false;
    }

    /**
     * Habilita la puerta final en el juego si el nivel lo permite.
     */
    public void habilitarPuerta() {
        if (prota.getNivel() < 5) {
            controlNotis.setHabilitarPuertaFinal(true);
        } else {
            finalizarjuego();
        }
    }

    /**
     * Finaliza el juego, mostrando un diploma y regresando al menú principal.
     */
    private void finalizarjuego() {
        hilo = null;
        if (notisPanel != null) {
            notisPanel.setVisible(false);
            prota.subirNivel();
        }
        Diploma d = new Diploma(partidaActual.getUser(), partidaActual.getLenguaje(), e -> irAlMenuPrincipal());
        d.setVisible(true);
        this.add(d, Integer.valueOf(8)); // Fondo
    }

    /**
     * Subir el nivel del personaje.
     */
    public void subirNivel() {
        prota.subirNivel();
        modeloNotis.seleccion(prota.getNivel());
        controlNotis.resetModelo(modeloNotis);
        npclist = null;
    }

    /**
     * Guarda el progreso del juego actual.
     */
    public void guardarJuego() {
        if (mapaActual != mapa) {
            partidaActual.setCoordX(controlNotis.getProximaSalidaX());
            partidaActual.setCoordY(controlNotis.getProximaSalidaY());
        } else {
            partidaActual.setCoordX(Mapa_CoordX);
            partidaActual.setCoordY(Mapa_CoordY);
        }
        partidaActual.setNivel(prota.getNivel());
        gestorPartidas.editarPartida(partidaActual);
    }

    //GETTERS
    public boolean[] getNpclist() {
        return npclist;
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public Dialogos getDialogoPanel() {
        return dialogoPanel;
    }

    public KeyAdapter getTeclas() {
        return teclas;
    }

    public c_notificacion getControlNotis() {
        return controlNotis;
    }

    public Partidas getPartidaActual() {
        return partidaActual;
    }

    public Mapa getMapaActual() {
        return mapaActual;
    }

    public KeysConfig getKeysConfig() {
        return keysConfig;
    }

    public int getMapa_CoordY() {
        return Mapa_CoordY;
    }

    public int getMapa_CoordX() {
        return Mapa_CoordX;
    }

    public void setMapa_CoordX(int mapa_CoordX) {
        Mapa_CoordX = mapa_CoordX;
    }

    public void setMapa_CoordY(int mapa_CoordY) {
        Mapa_CoordY = mapa_CoordY;
    }

    public v_notificacion getNotisPanel() {
        return notisPanel;
    }

}



