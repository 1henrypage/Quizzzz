package server.server_classes;

public class IdGenerator {
    private long id;

    /**
     * Constructor for IDGenerator
     */
    public IdGenerator() {
        this.id = 0L;
    }

    /**
     * Creates ids. returns long and increments
     * @return The ID
     */
    public synchronized long createID() {
        return id++;
    }
}
