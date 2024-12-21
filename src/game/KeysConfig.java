package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/**
 * Clase que maneja la configuración de las teclas para los controles del jugador.
 * Esta clase implementa la interfaz KeyListener para detectar y manejar los eventos
 * de las teclas presionadas y liberadas.
 */
public class KeysConfig implements KeyListener {

    private boolean Avanzar;
    private boolean Atras;
    private boolean Izquierda;
    private boolean Derecha;
    /**
     * Obtiene el estado de la tecla para avanzar.
     *
     * @return true si la tecla para avanzar está presionada, false si no.
     */
    public boolean isAvanzar() {
        return Avanzar;
    }
    /**
     * Obtiene el estado de la tecla para ir hacia atrás.
     *
     * @return true si la tecla para ir hacia atrás está presionada, false si no.
     */
    public boolean isAtras() {
        return Atras;
    }
    /**
     * Obtiene el estado de la tecla para ir hacia la izquierda.
     *
     * @return true si la tecla para ir hacia la izquierda está presionada, false si no.
     */
    public boolean isIzquierda() {
        return Izquierda;
    }
    /**
     * Obtiene el estado de la tecla para ir hacia la derecha.
     *
     * @return true si la tecla para ir hacia la derecha está presionada, false si no.
     */
    public boolean isDerecha() {
        return Derecha;
    }
    /**
     * Este método se invoca cuando una tecla es escrita. En este caso, no se utiliza.
     *
     * @param e El evento de la tecla presionada.
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }
    /**
     * Este método se invoca cuando una tecla es presionada. Actualiza el estado de las
     * teclas asociadas al movimiento del jugador.
     *
     * @param e El evento de la tecla presionada.
     */
    @Override
    public void keyPressed(KeyEvent e) {

        int tecla = e.getKeyCode();
        if (tecla == KeyEvent.VK_W || tecla == KeyEvent.VK_UP) {
            Avanzar = true;
        }
        if (tecla == KeyEvent.VK_A || tecla == KeyEvent.VK_LEFT) {
            Izquierda = true;
        }
        if (tecla == KeyEvent.VK_S || tecla == KeyEvent.VK_DOWN) {
            Atras = true;
        }
        if (tecla == KeyEvent.VK_D || tecla == KeyEvent.VK_RIGHT) {
            Derecha = true;
        }
    }
    /**
     * Este método se invoca cuando una tecla es liberada. Actualiza el estado de las
     * teclas asociadas al movimiento del jugador.
     *
     * @param e El evento de la tecla liberada.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int tecla = e.getKeyCode();
        if (tecla == KeyEvent.VK_W || tecla == KeyEvent.VK_UP) {
            Avanzar = false;
        }
        if (tecla == KeyEvent.VK_A || tecla == KeyEvent.VK_LEFT) {
            Izquierda = false;
        }
        if (tecla == KeyEvent.VK_S || tecla == KeyEvent.VK_DOWN) {
            Atras = false;
        }
        if (tecla == KeyEvent.VK_D || tecla == KeyEvent.VK_RIGHT) {
            Derecha = false;
        }
    }

}
