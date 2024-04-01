import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class OrderBookTest {

    private OrderBook orderBook;
    private Order bidOrder;
    private Order offerOrder;

    @BeforeEach
    void setUp() {
        orderBook = new OrderBook();
        bidOrder = new Order(1L, 100, 'B', 500L);
        offerOrder = new Order(2L, 105.0, 'O', 300L);
    }
    
    @Test
    void testAddOrder() {
        OrderBookLevel addedLevel = orderBook.addOrder(bidOrder);
        assertNotNull(addedLevel, "Added level should not be null");
        assertEquals(100.0, orderBook.getLevelPrice('B', 1),  "The price of the level should match the order price");
    }

    @Test
    void testRemoveOrder() {
        orderBook.addOrder(bidOrder);
        OrderBookLevel removedLevel = orderBook.removeOrder(bidOrder.getId());
        assertNotNull(removedLevel, "Removed level should not be null");
    }

    @Test
    void testReplaceOrderSize() {
        orderBook.addOrder(bidOrder);
        OrderBookLevel replacedLevel = orderBook.replaceOrderSize(bidOrder.getId(), 600L);
        assertNotNull(replacedLevel, "Replaced level should not be null");
        assertEquals(600L, replacedLevel.getSize(), "The size of the level should be updated");
    }
    @Test
    void testAddOrder_NullOrder() {
        OrderBookLevel addedLevel = orderBook.addOrder(null);
        assertNull(addedLevel, "Adding a null order should return null");
    }

    @Test
    void testRemoveOrder_NonExistentOrder() {
        OrderBookLevel removedLevel = orderBook.removeOrder(9999);
        assertNull(removedLevel, "Removing a non-existent order should return null");
    }

    @Test
    void testGetLevelPrice() {
        Order bidOrder1 = new Order(3L, 200, 'B', 500L);
        Order offerOrder1 = new Order(4L, 205.0, 'O', 300L);
        Order bidOrder2 = new Order(5L, 300, 'B', 500L);
        Order offerOrder2 = new Order(6L, 305.0, 'O', 300L);
        Order bidOrder3 = new Order(7L, 400, 'B', 500L);
        Order offerOrder3 = new Order(8L, 405.0, 'O', 300L);
        orderBook.addOrder(bidOrder);
        orderBook.addOrder(offerOrder);
        orderBook.addOrder(bidOrder1);
        orderBook.addOrder(offerOrder1);
        orderBook.addOrder(bidOrder2);
        orderBook.addOrder(offerOrder2);
        orderBook.addOrder(bidOrder3);
        orderBook.addOrder(offerOrder3);
        assertEquals(400.0, orderBook.getLevelPrice('B', 1));
        assertEquals(105.0, orderBook.getLevelPrice('O', 1));
        assertEquals(300.0, orderBook.getLevelPrice('B', 2));
        assertEquals(205.0, orderBook.getLevelPrice('O', 2));
    }

    @Test
    void testGetTotalLevelSize() {
        Order offerOrder1 = new Order(4L, 105.0, 'O', 300L);
        Order bidOrder1 = new Order(5L, 400, 'B', 500L);
        Order bidOrder2 = new Order(6L, 400, 'B', 500L);

        orderBook.addOrder(bidOrder);
        orderBook.addOrder(offerOrder);
        orderBook.addOrder(bidOrder1);
        orderBook.addOrder(bidOrder2);
        orderBook.addOrder(offerOrder1);

        assertEquals(1000L, orderBook.getTotalLevelSize('B', 1));
        assertEquals(600L, orderBook.getTotalLevelSize('O', 1));
    }

    @Test
    void testGetOrders() {
        Order bidOrder1 = new Order(3L, 400, 'B', 500L);
        Order offerOrder1 = new Order(4L, 205.0, 'O', 300L);
        Order bidOrder2 = new Order(5L, 400, 'B', 500L);
        Order offerOrder2 = new Order(6L, 305.0, 'O', 300L);
        Order bidOrder3 = new Order(7L, 400, 'B', 500L);
        Order offerOrder3 = new Order(8L, 405.0, 'O', 300L);
        orderBook.addOrder(bidOrder);
        orderBook.addOrder(offerOrder);
        orderBook.addOrder(bidOrder1);
        orderBook.addOrder(offerOrder1);
        orderBook.addOrder(bidOrder2);
        orderBook.addOrder(offerOrder2);
        orderBook.addOrder(bidOrder3);
        orderBook.addOrder(offerOrder3);
        List<Order> bids = orderBook.getOrders('B');
        assertFalse(bids.isEmpty(), "Bids list should not be empty");
        assertEquals(4, bids.size(), "Bids list should contain four orders");
        assertSame(bidOrder1, bids.get(0), "The orders should be ordered by price and time added");
        assertSame(bidOrder2, bids.get(1), "The orders should be ordered by price and time added");
        assertSame(bidOrder3, bids.get(2), "The orders should be ordered by price and time added");
        assertSame(bidOrder, bids.get(3), "The orders should be ordered by price and time added");
    }
}
