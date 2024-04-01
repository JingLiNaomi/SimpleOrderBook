import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        // Initialize your Order object before each test
        order = new Order(1L, 100.0, 'B', 500L);
    }

    @Test
    void testGetId() {
        // Test that the getId method returns the correct ID
        assertEquals(1L, order.getId(), "The ID should match the constructor value");
    }

    @Test
    void testGetPrice() {
        // Test that the getPrice method returns the correct price
        assertEquals(100.0, order.getPrice(), 0.001, "The price should match the constructor value");
    }

    @Test
    void testGetSide() {
        // Test that the getSide method returns the correct side
        assertEquals('B', order.getSide(), "The side should match the constructor value");
    }

    @Test
    void testGetSize() {
        // Test that the getSize method returns the correct size
        assertEquals(500L, order.getSize(), "The size should match the constructor value");
    }
}