
public class OrderBookLevel {
    private final long timeInNanos;
    private final long id;
    private final double price;
    private final char side;
    private long size;
    private final Order order;

    public OrderBookLevel(long recordTimeInNanos, Order order) {
        this.timeInNanos = recordTimeInNanos;
        this.id = order.getId();
        this.price = order.getPrice();
        this.side = order.getSide();
        this.size = order.getSize();
        this.order = order;
    }

    public long getTimeInNanos() {
        return timeInNanos;
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

    public void setSize(long size) {
        this.size = size;
    }

    public Order getOrder(){
        return order; // better return a copy
    }

}
