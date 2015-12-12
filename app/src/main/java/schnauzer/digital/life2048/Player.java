package schnauzer.digital.life2048;

/**
 * Created by Rogelio on 11/19/2015.
 */
public class Player {
    String id;
    String name;
    String status;

    public Player(String name, int id) {
        this.name = name;
        this.id = Integer.toString(id);
        this.status = "D";
    }

    public Player(String name, int id, String status) {
        this.name = name;
        this.id = Integer.toString(id);
        this.status = status;
    }

    public Player(String name, String id, String status) {
        this.name = name;
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return Integer.parseInt(id);
    }

    public void setId(int id) {
        this.id = Integer.toString(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
