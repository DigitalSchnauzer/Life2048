package schnauzer.digital.life2048;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;

/**
 * Created by Rogelio on 11/19/2015.
 */
public class ServiceManager {

    static String stringUrl = "http://www.xpesd.com/DEV/Services/Game2048";
    static OnlineGameController gameController;

    public OnlineGameController getGameController() {
        return gameController;
    }

    public static void setGameController(OnlineGameController gameControl) {
        gameController = gameControl;
    }

    protected static void savePlayer (int id, String name) {

    }

    protected static ArrayList<Player> getListOfPlayers () {
        ArrayList<Player> players = new ArrayList<Player>();
        return players;
    }

    protected static Player.Status getStatus (int id) {
        Player.Status status = null;
        return status;
    }

    protected static void setPlayer (int id) { // invite player

    }

    protected static void acceptRequest () {

    }

    protected static void releasePlayer () { // deny invitation or release when game is over

    }

    protected static void setStartStatus (int x1, int y1, int value1, int x2, int y2, int value2) { // set start status for second player
        gameController.setStartStatus(x1, y1, value1, x2, y2, value2);
    }

    protected static int[] getStartStatus () {
        int[] startStatus = new int[6];
        return startStatus;
    }

    protected void setTurn (int movement, int x, int y, int value, int id) { // termina el turno
        gameController.myTurn = false;
    }

    protected void getTurn () {
        int movement, x, y, value, id;
        movement = 0;
        x = 0;
        y = 0;
        value = 0;
        id = 0;
        gameController.moveTiles(movement, x, y, value);
        gameController.myTurn = true;
    }

    protected void checkConnection () {

    }
}
