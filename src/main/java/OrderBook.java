import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class OrderBook {
    private final TreeSet<OrderBookLevel> bids;
    private final TreeSet<OrderBookLevel> asks;
    private final HashMap<Long, OrderBookLevel> OrderBookLevelCache;
    private final Comparator<OrderBookLevel> sortByTime = Comparator.comparing(OrderBookLevel::getTimeInNanos);
    private final Comparator<OrderBookLevel> sortByPrice = Comparator.comparing(OrderBookLevel::getPrice);
    private final char BID = 'B';
    private final char OFFER = 'O';
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();
    private final ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();

    public OrderBook() {
        bids = new TreeSet<>(sortByPrice.reversed().thenComparing(sortByTime));
        asks = new TreeSet<>(sortByPrice.thenComparing(sortByTime));
        OrderBookLevelCache = new HashMap<>();
    }

    public OrderBookLevel addOrder(Order order){
        writeLock.lock();
        try {
            if(order == null) {
                System.out.println(String.format("Order is null, failed to add"));
                return null;
            }

            OrderBookLevel level = new OrderBookLevel( System.nanoTime(),order);
            if (order.getSide() == BID) {
                bids.add(level);
                OrderBookLevelCache.put(order.getId(), level);
            } else if (order.getSide() == OFFER) {
                asks.add(level);
                OrderBookLevelCache.put(order.getId(), level);
            }
            else{
                throw new Exception(String.format("Unsupported side %s", order.getSide()));
            }
            return level;
        }catch (Exception e){
            System.out.println(String.format("Failed to add order %s due to exception %s", order.getId(), e.getMessage()));
            return null;
        }finally {
            writeLock.unlock();
        }

    }

    public OrderBookLevel removeOrder(long orderId){
        writeLock.lock();
        try {
            if(!OrderBookLevelCache.containsKey(orderId)) {
                System.out.println(String.format("Order %s is not found, failed to remove", orderId));
                return null;
            }

            OrderBookLevel oldLevel = OrderBookLevelCache.get(orderId);
            if (oldLevel.getSide() == BID) {
                bids.remove(oldLevel);
            } else if (oldLevel.getSide() == OFFER) {
                asks.remove(oldLevel);
            } else {
                throw new Exception(String.format("Unsupported side %s", oldLevel.getSide()));
            }
            OrderBookLevelCache.remove(oldLevel);
            return oldLevel;
        } catch (Exception e){
            System.out.println(String.format("Failed to remove order %s due to exception %s", orderId, e.getMessage()));
            return null;
        } finally {
            writeLock.unlock();
        }
    }

    public OrderBookLevel replaceOrderSize(long orderId, long size){
        writeLock.lock();
        try {
            if (!OrderBookLevelCache.containsKey(orderId)) {
                System.out.println(String.format("Order %s is not found, failed to replace size", orderId));
                return null;
            }

            OrderBookLevel level = OrderBookLevelCache.get(orderId);
            level.setSize(size);
            return level;
        } catch (Exception e){
            System.out.println(String.format("Failed to replace order %s due to exception %s", orderId, e.getMessage()));
            return null;
        } finally {
            writeLock.unlock();
        }
    }

    public double getLevelPrice(char side, int level){
        readLock.lock();
        try {
            TreeSet<OrderBookLevel> levels = side == BID ? bids : asks;
            Iterator<OrderBookLevel> it = levels.iterator();
            int i = 0;
            OrderBookLevel current = null;
            while (it.hasNext() && i < level) {
                current = it.next();
                i++;
            }
            return current != null ? current.getPrice() : Double.NaN;
        } finally {
            readLock.unlock();
        }
    }

    public long getTotalLevelSize(char side, int level){
        readLock.lock();
        try {
            double price = getLevelPrice(side, level);
            if (Double.isNaN(price))
                return 0;
            TreeSet<OrderBookLevel> levels = side == BID ? bids : asks;
            return levels.stream().filter(l -> l.getPrice() == price).mapToLong(l -> l.getSize()).sum();
        } finally {
            readLock.unlock();
        }
    }

    public List<Order> getOrders(char side){
        readLock.lock();
        try {
            TreeSet<OrderBookLevel> levels = side == BID ? bids : asks;
            return levels.stream().map(l -> l.getOrder()).collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

}
