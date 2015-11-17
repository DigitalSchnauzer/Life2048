package schnauzer.digital.life2048;

import java.util.List;

/**
 * Created by Rogelio on 11/4/2015.
 */
public class Grid {
    public int size;
    public boolean cells[][];

    public Grid(int size) {
        this.size = size;
        cells = new boolean[size][size];
    }

    public void setCellFree (int x, int y) {
        cells[x][y] = false;
    }

    public void setCellBusy (int x, int y) {
        cells[x][y] = true;
    }
}
