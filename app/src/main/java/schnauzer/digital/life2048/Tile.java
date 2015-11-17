package schnauzer.digital.life2048;

import android.widget.TextView;

import java.util.Comparator;

/**
 * Created by Rogelio on 11/4/2015.
 */
public class Tile implements Comparator<Integer>{
    public int x, y, previousX, previousY, value;

    public Tile (int posX, int posY, int value) {
        this.x = posX;
        this.y = posY;
        this.value = value;
    }

    public void savePosition () {
        this.previousX = this.x;
        this.previousY = this.y;
    }

    public void updatePosition (int posX, int posY) {
        this.x = posX;
        this.y = posY;
    }

    @Override
    public int compare(Integer lhs, Integer rhs) {
        return 0;
    }
}