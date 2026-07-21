package es.upm.grise.order;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.upm.grise.order.exceptions.CannotAddItemsToPlacedOrderException;
import es.upm.grise.order.exceptions.IncorrectItemException;
import es.upm.grise.order.exceptions.NonExistingItemException;

public class OrderTest {

    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order();
    }

    // Crea un Product real y le asigna el id vía reflexión ya que Product no tiene constructor con id
    private Product createProduct(long id) {
        Product product = new Product();
        try {
            Field idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(product, id);
        } catch (Exception e) {
            fail("Error al asignar ID al Product por reflexión: " + e.getMessage());
        }
        return product;
    }

    // Crea un Item usando su constructor oficial (Product, quantity, price)
    private Item createTestItem(long productId, double price, int quantity) {
        Product product = createProduct(productId);
        return new Item(product, quantity, price);
    }

    // --- 1. CONSTRUCTOR TESTS ---

    @Test
    @DisplayName("Inicialización: Lista de ítems vacía y status null")
    public void testOrderInitialization() {
        assertTrue(order.getItems().isEmpty());
        assertNull(order.getStatus());
    }

    // --- 2. ADD ITEM TESTS ---

    @Test
    @DisplayName("addItem: Lanza CannotAddItemsToPlacedOrderException si status es PLACED")
    public void testAddItemWhenStatusIsPlaced_throwsException() throws Exception {
        Field statusField = Order.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(order, Status.PLACED);

        Item item = createTestItem(1L, 10.0, 1);

        assertThrows(CannotAddItemsToPlacedOrderException.class, () -> {
            order.addItem(item);
        });
    }

    @Test
    @DisplayName("addItem: Lanza IncorrectItemException con precio negativo")
    public void testAddItemNegativePrice_throwsException() {
        Item item = createTestItem(1L, -5.0, 1);

        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(item);
        });
    }

    @Test
    @DisplayName("addItem: Lanza IncorrectItemException con cantidad menor o igual a cero")
    public void testAddItemZeroOrNegativeQuantity_throwsException() {
        Item itemZero = createTestItem(1L, 10.0, 0);
        Item itemNeg = createTestItem(2L, 10.0, -1);

        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(itemZero);
        });

        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(itemNeg);
        });
    }

    @Test
    @DisplayName("addItem: El primer ítem asigna el status a UNCONFIRMED")
    public void testAddFirstItem_changesStatusToUnconfirmed() throws Exception {
        Item item = createTestItem(1L, 10.0, 2);

        order.addItem(item);

        assertEquals(1, order.getItems().size());
        assertEquals(Status.UNCONFIRMED, order.getStatus());
    }

    @Test
    @DisplayName("addItem: Mismo producto con mismo precio incrementa la cantidad")
    public void testAddExistingItemSamePrice_incrementsQuantity() throws Exception {
        Item item1 = createTestItem(1L, 10.0, 2);
        Item item2 = createTestItem(1L, 10.0, 3);

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(1, order.getItems().size());
        assertEquals(5, order.getItems().get(0).getQuantity());
        assertEquals(10.0, order.getItems().get(0).getPrice());
    }

    @Test
    @DisplayName("addItem: Mismo producto con mayor precio actualiza la cantidad y el precio")
    public void testAddExistingItemHigherPrice_updatesQuantityAndPrice() throws Exception {
        Item item1 = createTestItem(1L, 10.0, 2);
        Item item2 = createTestItem(1L, 20.0, 3);

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(1, order.getItems().size());
        assertEquals(5, order.getItems().get(0).getQuantity());
        assertEquals(20.0, order.getItems().get(0).getPrice());
    }

    @Test
    @DisplayName("addItem: Mismo producto con menor precio incrementa cantidad pero conserva precio mayor")
    public void testAddExistingItemLowerPrice_keepsHigherPrice() throws Exception {
        Item item1 = createTestItem(1L, 20.0, 2);
        Item item2 = createTestItem(1L, 10.0, 3);

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(1, order.getItems().size());
        assertEquals(5, order.getItems().get(0).getQuantity());
        assertEquals(20.0, order.getItems().get(0).getPrice());
    }

    // --- 3. REMOVE ITEM TESTS ---

    @Test
    @DisplayName("removeItem: Lanza NonExistingItemException si el ítem no está en la orden")
    public void testRemoveNonExistingItem_throwsException() {
        Item item = createTestItem(1L, 10.0, 1);

        assertThrows(NonExistingItemException.class, () -> {
            order.removeItem(item);
        });
    }

    @Test
    @DisplayName("removeItem: Elimina el ítem de la lista")
    public void testRemoveItem_removesFromList() throws Exception {
        Item item1 = createTestItem(1L, 10.0, 1);
        Item item2 = createTestItem(2L, 15.0, 1);

        order.addItem(item1);
        order.addItem(item2);

        order.removeItem(item1);

        assertEquals(1, order.getItems().size());
        assertEquals(2L, order.getItems().get(0).getProduct().getId());
        assertEquals(Status.UNCONFIRMED, order.getStatus());
    }

    @Test
    @DisplayName("removeItem: Al remover el último ítem, el status pasa a null")
    public void testRemoveLastItem_setsStatusToNull() throws Exception {
        Item item = createTestItem(1L, 10.0, 1);

        order.addItem(item);
        order.removeItem(item);

        assertTrue(order.getItems().isEmpty());
        assertNull(order.getStatus());
    }
}