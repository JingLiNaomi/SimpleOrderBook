public class Order {
    private long id;
    private double price;
    private char side;
    private long size;

    public Order(long id, double price, char side, long size){
        this.id = id;
        this.price = price;
        this.size = size;
        this.side = side;
    }

    public long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public char getSide() {
        return side;
    }

    public long getSize() {
        return size;
    }
}
