package schnauzer.digital.life2048;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Created by Rogelio on 11/19/2015.
 */
public class OnlineGameController {

    public class MajorXComparator implements Comparator<Tile> {
        @Override
        public int compare(Tile tile1, Tile tile2) {
            return (tile1.x>tile2.x ? -1 : (tile1.x==tile2.x ? 0 : 1));
        }
    }

    public class MinorXComparator implements Comparator<Tile> {
        @Override
        public int compare(Tile tile1, Tile tile2) {
            return (tile1.x<tile2.x ? -1 : (tile1.x==tile2.x ? 0 : 1));
        }
    }

    public class MajorYComparator implements Comparator<Tile> {
        @Override
        public int compare(Tile tile1, Tile tile2) {
            return (tile1.y>tile2.y ? -1 : (tile1.y==tile2.y ? 0 : 1));
        }
    }

    public class MinorYComparator implements Comparator<Tile> {
        @Override
        public int compare(Tile tile1, Tile tile2) {
            return (tile1.y<tile2.y ? -1 : (tile1.y==tile2.y ? 0 : 1));
        }
    }

    private boolean host = true;
    private boolean online = false;
    protected boolean myTurn;
    private int startCount = 0;
    private int x1, y1, value1, x2, y2, value2;

    public boolean gameOver = false;

    public ArrayList<Tile> tiles;
    Random rnd;
    Grid grid;
    public int size;
    MinorXComparator minorXComparator;
    MinorYComparator minorYComparator;
    MajorXComparator majorXComparator;
    MajorYComparator majorYComparator;

    TextView tileViews[][];
    MainActivity main;

    int movementCount;

    public OnlineGameController(int size, TextView tileViews[][], MainActivity main, boolean host, boolean online) {
        //constructor
        rnd = new Random();
        this.size = size;
        grid = new Grid(size);
        this.tileViews = tileViews;
        this.main = main;
        this.host = this.myTurn = host;
        main.score=0;
        main.updateScore();

        tiles = new ArrayList<Tile>() {};

        minorXComparator = new MinorXComparator();
        minorYComparator = new MinorYComparator();
        majorXComparator = new MajorXComparator();
        majorYComparator = new MajorYComparator();

        if (host) {
            addRandomTile();
            addRandomTile();

            if (online) {

            }
        }
    }

    protected void setStartStatus (int x1, int y1, int value1, int x2, int y2, int value2) {
        addTile(x1, y1, value1);
        addTile(x2, y2, value2);
    }

    private void addRandomTile () {
        int x, y;
        do {
            x = rnd.nextInt(size);
            y = rnd.nextInt(size);
        } while (grid.cells[x][y]==true);

        addTile(x, y);
    }

    protected void addTile (int x, int y) {
        int value = rnd.nextInt(2);
        if (value ==0)
            value=2;
        else if (value==1)
            value=4;

        tiles.add(new Tile(x, y, value));
        grid.setCellBusy(x, y);

        if (online && host) {

        }
    }

    protected void addTile (int x, int y, int value) {
        tiles.add(new Tile(x, y, value));
        grid.setCellBusy(x, y);
    }

    public void moveTiles(int movement) {
        // 0 = up; 1 = right; 2 = down; 3 = left;

        if (!myTurn)
            return;

        switch (movement) {
            case 0:
                Collections.sort(tiles, minorYComparator);
                break;
            case 1:
                Collections.sort(tiles, majorXComparator);
                break;
            case 2:
                Collections.sort(tiles, majorYComparator);
                break;
            case 3:
                Collections.sort(tiles, minorXComparator);
                break;
        }

        movementCount=0;

        for (int i=0; i<tiles.size(); i++) {
            i = moveCurrentTile(tiles.get(i), movement, i);
        }

        gameOver = true;

        for (int j=0; j<size; j++) {
            for (int i=0; i<size; i++) {
                if (grid.cells[i][j]==false)
                    gameOver = false;
            }
        }

        if (movementCount>0) {
            addRandomTile();
            gameOver = false;
        }
    }

    public void moveTiles(int movement, int x, int y, int value) {
        // 0 = up; 1 = right; 2 = down; 3 = left;

        switch (movement) {
            case 0:
                Collections.sort(tiles, minorYComparator);
                break;
            case 1:
                Collections.sort(tiles, majorXComparator);
                break;
            case 2:
                Collections.sort(tiles, majorYComparator);
                break;
            case 3:
                Collections.sort(tiles, minorXComparator);
                break;
        }

        movementCount=0;

        for (int i=0; i<tiles.size(); i++) {
            i = moveCurrentTile(tiles.get(i), movement, i);
        }

        gameOver = true;

        for (int j=0; j<size; j++) {
            for (int i=0; i<size; i++) {
                if (grid.cells[i][j]==false)
                    gameOver = false;
            }
        }

        if (movementCount>0) {
            addTile(x, y, value);
            gameOver = false;
        }
    }

    private int moveCurrentTile(Tile tile, int movement, int i) {
        // 0 = up; 1 = right; 2 = down; 3 = left;
        tile.savePosition();
        Tile nextTile=null;
        int xModifier=0, yModifier=0;
        boolean dead=false;

        switch (movement) {
            case 0:
                yModifier=-1;
                break;
            case 1:
                xModifier=1;
                break;
            case 2:
                yModifier=1;
                break;
            case 3:
                xModifier=-1;
                break;
        }

        boolean cantMove = false;

        while (keepGoing(tile, movement) && !cantMove && !dead) {
            nextTile = getTileAtPosition(tile.x+xModifier, tile.y+yModifier);
            if (nextTile==null) {
                tile.updatePosition(tile.x+xModifier, tile.y+yModifier);
                movementCount++;
            } else if (tile.value == nextTile.value) {
                main.score += nextTile.value *= 2;
                main.updateScore();
                grid.setCellFree(tile.x, tile.y);
                tiles.remove(tile);
                movementCount++;
                i--;
                dead = true;
            } else {
                cantMove = true;
            }
        }

        grid.setCellFree(tile.previousX, tile.previousY);
        if (!dead)
            grid.setCellBusy(tile.x, tile.y);

        return i;
    }

    private boolean keepGoing(Tile tile, int movement) {
        switch (movement) {
            case 0:
                return tile.y>0;
            case 1:
                return tile.x<grid.size-1;
            case 2:
                return tile.y<grid.size-1;
            case 3:
                return tile.x>0;
        }
        return false;
    }

    public Tile getTileAtPosition (int x, int y) {
        if (grid.cells[x][y]==false)
            return null;

        Tile tile = null;

        for (int i=0; i<tiles.size(); i++) {
            if (tiles.get(i).x==x && tiles.get(i).y==y)
                tile=tiles.get(i);
        }

        return tile;
    }
}
