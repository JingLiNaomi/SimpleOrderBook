import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderBookLevelTest {

    private OrderBookLevel orderBookLevel;
    private Order order;

    @BeforeEach
    void setUp() {
        // Mock an Order object
        order = new Order(1L, 100.0, 'B', 500L);
        orderBookLevel = new OrderBookLevel(123456789L, order);
    }

    @Test
    void testConstructorAndGetterMethods() {
        assertEquals(123456789L, orderBookLevel.getTimeInNanos(), "TimeInNanos should match constructor value");
        assertEquals(1L, orderBookLevel.getId(), "ID should match the Order object's ID");
        assertEquals(100.0, orderBookLevel.getPrice(), 0.001, "Price should match the Order object's price");
        assertEquals('B', orderBookLevel.getSide(), "Side should match the Order object's side");
        assertEquals(500L, orderBookLevel.getSize(), "Size should match the Order object's size");
        assertNotNull(orderBookLevel.getOrder(), "GetOrder should not return null");
    }

    @Test
    void testSetSize() {
        long newSize = 600L;
        orderBookLevel.setSize(newSize);
        assertEquals(newSize, orderBookLevel.getSize(), "Size should be updated to the new value");
    }

    @Test
    void testGetOrder() {
        Order fetchedOrder = orderBookLevel.getOrder();
        assertSame(order, fetchedOrder, "The fetched Order should be the same as the one used to create OrderBookLevel");
    }
}
