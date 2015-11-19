package schnauzer.digital.life2048;

/**
 * Created by Rogelio on 11/19/2015.
 */
public class Player {
    int id;
    String name;
    Status status;

    public enum Status {
        PLAYING, AVAILABLE, INVITADO, AWAY;
    }

    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        this.status = Status.AWAY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
