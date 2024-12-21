package basedatos;

import java.util.UUID;
/**
 * Clase que representa una partida en el sistema de base de datos.
 * Contiene información sobre el jugador, el nivel en el que se encuentra,
 * las coordenadas del jugador y otros datos relacionados con la partida.
 */
public class Partidas {
        private UUID id;
        private String user;
        private String lenguaje;
        private String url;
        private int nivel;
        private int coordX;
        private int coordY;
        private String fecha;
        /**
         * Obtiene la coordenada X del jugador en la partida.
         *
         * @return La coordenada X.
         */
        public int getCoordX() {
                return coordX;
        }
        /**
         * Establece la coordenada X del jugador en la partida.
         *
         * @param coordX La coordenada X a establecer.
         */
        public void setCoordX(int coordX) {
                this.coordX = coordX;
        }
        /**
         * Obtiene la coordenada Y del jugador en la partida.
         *
         * @return La coordenada Y.
         */
        public int getCoordY() {
                return coordY;
        }
        /**
         * Establece la coordenada Y del jugador en la partida.
         *
         * @param coordY La coordenada Y a establecer.
         */
        public void setCoordY(int coordY) {
                this.coordY = coordY;
        }
        /**
         * Establece el ID único de la partida.
         *
         * @param id El ID único a establecer.
         */
        public void setId(UUID id) {
                this.id = id;
        }
        /**
         * Obtiene el ID único de la partida.
         *
         * @return El ID único de la partida.
         */
        public UUID getId() {
                return id;
        }
        /**
         * Obtiene la fecha de la partida.
         *
         * @return La fecha de la partida.
         */
        public String getFecha() {
                return fecha;
        }
        /**
         * Obtiene el nivel en el que se encuentra el jugador.
         *
         * @return El nivel de la partida.
         */
        public int getNivel() {
                return nivel;
        }
        /**
         * Obtiene el nombre de usuario del jugador.
         *
         * @return El nombre de usuario.
         */
        public String getUser() {
                return user;
        }
        /**
         * Obtiene el lenguaje seleccionado para la partida.
         *
         * @return El lenguaje de la partida.
         */
        public String getLenguaje() {
                return lenguaje;
        }
        /**
         * Obtiene la URL asociada a la partida.
         *
         * @return La URL de la partida.
         */
        public String getUrl() {
                return url;
        }
        /**
         * Establece la URL asociada a la partida.
         *
         * @param url La URL a establecer.
         */
        public void setUrl(String url) {
                this.url = url;
        }
        /**
         * Establece el nombre de usuario del jugador.
         *
         * @param user El nombre de usuario a establecer.
         */
        public void setUser(String user) {
                this.user = user;
        }
        /**
         * Establece el lenguaje seleccionado para la partida.
         *
         * @param lenguaje El lenguaje a establecer.
         */
        public void setLenguaje(String lenguaje) {
                this.lenguaje = lenguaje;
        }
        /**
         * Establece la fecha de la partida.
         *
         * @param fecha La fecha a establecer.
         */
        public void setFecha(String fecha) {
                this.fecha = fecha;
        }
        /**
         * Establece el nivel en el que se encuentra el jugador.
         *
         * @param nivel El nivel a establecer.
         */
        public void setNivel(int nivel) {
                this.nivel = nivel;
        }
        /**
         * Retorna una representación en forma de cadena de la partida.
         *
         * @return La representación en cadena de la partida.
         */
        @Override
        public String toString() {
                return "Partidas{" +
                        "id=" + id +
                        ", user='" + user + '\'' +
                        ", lenguaje='" + lenguaje + '\'' +
                        ", url='" + url + '\'' +
                        ", nivel=" + nivel +
                        ", coordX=" + coordX +
                        ", coordY=" + coordY +
                        ", fecha='" + fecha + '\'' +
                        '}';
        }

}
