Enlace de la conversación con el LLM (Gemini): [Conversacion](https://share.gemini.google/1uJCWPAapwIU)

Conversación completa copiada y pegada:
Necesito implementar las pruebas de unidad de JUnit, estas son las especificaciones:

Especificación de la clase Order

Arquitectura del sistema

La clase Order contiene los artículos comprados por un cliente de un portal de compras.

Para cada pedido, se crea una invoice.

Figura 1. Diagrama de clases

La arquitectura del sistema de software se muestra en la Figura 1. El portal de compras

vende una amplia gama de productos. Cuando un cliente quiere hacer una compra, se crea

una nueva order. Dicha instanciación la realiza una clase controladora que no se muestra

en el diagrama.

Cada vez que un cliente selecciona un product, se crea un nuevo item1 y la clase

controlador añade el item a la order. Cuando una order contiene items, adopta el

status UNCONFIRMED; de lo contrario, el status será null. La clase controladora

asegura que el item nunca es null.

El mismo product (con el mismo id) puede ser vendido por varios proveedores, y cada

proveedor puede asignar un price distinto.

1Se debe tener en cuenta que la relación entre Order y Product es n:m, por lo que se requiere un objeto

Item intermedio.

Pag. 1

Cualquier item puede ser retirado del pedido. También se puede calcular el importe total

de la order. Una vez completada la order, esta pasa al status PLACED y, a

continuación, se crea una nueva invoice que se almacena en la clase estática Invoices

(esta clase Invoices tiene como propósito evitar la creación de una base de datos dado

el estado actual del proyecto).

Implementación requerida de la clase Order

La clase Order toma los siguientes valores durante la inicialización:

c La lista de items deberá estar vacía.

c El status de la order deberá tomar el valor null.

Se deben implementar los siguientes métodos de Order:

• void addItem(Item item)

Este método añade un item a la order

c Se debe añadir el item a la lista de items.

c Si el status de la order es PLACED, se lanzará una

CannotAddItemsToPlacedOrderException.

c El price del item debe ser mayor o igual a cero. De lo contrario, se

lanzará una IncorrectItemException.

c La quantity del item debe ser mayor que cero. De lo contrario, se

lanzará una IncorrectItemException.

c Si el item ya existe en la lista de items y el price coincide, el

nuevo item no se añadirá a la lista de items, sino que se

incrementará la quantity del item existente.

c Si el item ya existe en la lista de items pero el price es distinto,

el nuevo item no se añadirá a la lista de items, sino que se

incrementará la quantity del item existente. El price del item en

la lista de items deberá ser el price más alto.

c Cuando se añade el primer item a la lista de ítems, el status de la order

pasa a UNCONFIRMED.

Pag. 2

• void removeItem(Item item)

Este método elimina un item de la order

c El item debe eliminase de la lista de items.

c Si el item no existe en la lista de items, se deberá lanzar

NonExistingItemException.

c Si la lista de items queda vacía, se le asignará el valor null al

status de la order.



y este es mi archivo Order.java:



package es.upm.grise.order;



import java.util.ArrayList;



import es.upm.grise.order.exceptions.CannotAddItemsToPlacedOrderException;

import es.upm.grise.order.exceptions.IncorrectItemException;

import es.upm.grise.order.exceptions.NonExistingItemException;



public class Order {



    private ArrayList<Item> items;

    private Status status;

    private Invoice invoice;



    /*

     * Method to test

     */

    public Order() {



        items = new ArrayList<Item>();

        status = null;

    }



    /*

     * Method to test

     */

    public void addItem(Item item) throws CannotAddItemsToPlacedOrderException, IncorrectItemException {



        if (status == Status.PLACED) {



            throw new CannotAddItemsToPlacedOrderException();



        }



        if (item.getPrice() < 0) {



            throw new IncorrectItemException();



        }



        if (item.getQuantity() <= 0) {



            throw new IncorrectItemException();



        }



        for (Item i : items) {



            if (i.getProduct().getId() == item.getProduct().getId()) {



                i.setQuantity(i.getQuantity() + item.getQuantity());



                if (item.getPrice() > i.getPrice()) {

                   

                    i.setPrice(item.getPrice());



                }



                return;



            }



        }



        items.add(item);



        if (items.size() == 1) {



            status = Status.UNCONFIRMED;



        }



    }



    /*

     * Method to test

     */

    public void removeItem(Item item) throws NonExistingItemException {



        Item removedItem = null;



        for (Item i : items) {



            if (i.getProduct().getId() == item.getProduct().getId()) {



                removedItem = i;

                break;



            }



        }



        if (removedItem == null) {



            throw new NonExistingItemException();



        }



        items.remove(removedItem);



        if (items.isEmpty()) {



            status = null;



        }



    }



    /*

     * getters

     */



    public ArrayList<Item> getItems() {

        return items;

    }



    public Status getStatus() {

        return status;

    }



    public Invoice getInvoice() {

        return invoice;

    }



}



y este mi OrderTest.java:



package es.upm.grise.order;



import org.junit.jupiter.api.Test;



class CruiseControlTest {



    @Test

    public void smokeTest() {}

   

} 

Aquí tienes la implementación completa de la clase de pruebas OrderTest.java utilizando JUnit 5 y Mockito.

Key Test Cases Addressed
Target Requirement / Method	Test Case
Constructor	Verifies that items is empty and status is null.
addItem Exceptions	
• Throws CannotAddItemsToPlacedOrderException when status is PLACED.


• Throws IncorrectItemException if price < 0 or quantity <= 0.

addItem Logic	
• Changes status to UNCONFIRMED when adding the first item.


• Increments quantity when adding an item with an existing product ID.


• Updates price to the higher price if the new item's price is greater.


• Keeps the current price if the new item's price is lower or equal.

removeItem Exceptions	• Throws NonExistingItemException when trying to remove an item not in the order.
removeItem Logic	
• Removes the specified item from the list.


• Resets status to null when the last item is removed.

OrderTest.java Code
Java
package es.upm.grise.order;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    // Helper method to create Mock Items with a Mock Product
    private Item createMockItem(long productId, double price, int quantity) {
        Product mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn(productId);

        Item mockItem = mock(Item.class);
        when(mockItem.getProduct()).thenReturn(mockProduct);
        when(mockItem.getPrice()).thenReturn(price);
        when(mockItem.getQuantity()).thenReturn(quantity);

        return mockItem;
    }

    // --- 1. CONSTRUCTOR TESTS ---

    @Test
    @DisplayName("Initialization: Order items should be empty and status null")
    public void testOrderInitialization() {
        assertTrue(order.getItems().isEmpty());
        assertNull(order.getStatus());
    }

    // --- 2. ADD ITEM TESTS ---

    @Test
    @DisplayName("addItem: Throws CannotAddItemsToPlacedOrderException when order status is PLACED")
    public void testAddItemWhenStatusIsPlaced_throwsException() throws Exception {
        // Set private status field to Status.PLACED via Reflection
        Field statusField = Order.class.getDeclaredField("status");
        statusField.setAccessible(true);
        statusField.set(order, Status.PLACED);

        Item item = createMockItem(1L, 10.0, 1);

        assertThrows(CannotAddItemsToPlacedOrderException.class, () -> {
            order.addItem(item);
        });
    }

    @Test
    @DisplayName("addItem: Throws IncorrectItemException when price is negative")
    public void testAddItemNegativePrice_throwsException() {
        Item item = createMockItem(1L, -5.0, 1);

        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(item);
        });
    }

    @Test
    @DisplayName("addItem: Throws IncorrectItemException when quantity is zero or negative")
    public void testAddItemZeroOrNegativeQuantity_throwsException() {
        Item itemZeroQty = createMockItem(1L, 10.0, 0);
        Item itemNegQty = createMockItem(2L, 10.0, -2);

        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(itemZeroQty);
        });

        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(itemNegQty);
        });
    }

    @Test
    @DisplayName("addItem: First item added sets status to UNCONFIRMED and increases list size")
    public void testAddFirstItem_changesStatusToUnconfirmed() throws Exception {
        Item item = createMockItem(1L, 10.0, 2);

        order.addItem(item);

        assertEquals(1, order.getItems().size());
        assertEquals(Status.UNCONFIRMED, order.getStatus());
    }

    @Test
    @DisplayName("addItem: Adding existing product with same price increments quantity")
    public void testAddExistingItemSamePrice_incrementsQuantity() throws Exception {
        Item item1 = createMockItem(1L, 10.0, 2);
        Item item2 = createMockItem(1L, 10.0, 3);

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(1, order.getItems().size());
        verify(item1).setQuantity(5);
        verify(item1, never()).setPrice(anyDouble());
    }

    @Test
    @DisplayName("addItem: Adding existing product with higher price updates quantity and price")
    public void testAddExistingItemHigherPrice_updatesQuantityAndPrice() throws Exception {
        Item item1 = createMockItem(1L, 10.0, 2);
        Item item2 = createMockItem(1L, 20.0, 3);

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(1, order.getItems().size());
        verify(item1).setQuantity(5);
        verify(item1).setPrice(20.0);
    }

    @Test
    @DisplayName("addItem: Adding existing product with lower price updates quantity but keeps higher price")
    public void testAddExistingItemLowerPrice_keepsHigherPrice() throws Exception {
        Item item1 = createMockItem(1L, 20.0, 2);
        Item item2 = createMockItem(1L, 10.0, 3);

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(1, order.getItems().size());
        verify(item1).setQuantity(5);
        verify(item1, never()).setPrice(anyDouble());
    }

    // --- 3. REMOVE ITEM TESTS ---

    @Test
    @DisplayName("removeItem: Throws NonExistingItemException when item is not in order")
    public void testRemoveNonExistingItem_throwsException() {
        Item item = createMockItem(1L, 10.0, 1);

        assertThrows(NonExistingItemException.class, () -> {
            order.removeItem(item);
        });
    }

    @Test
    @DisplayName("removeItem: Removes item from list")
    public void testRemoveItem_removesFromList() throws Exception {
        Item item1 = createMockItem(1L, 10.0, 1);
        Item item2 = createMockItem(2L, 15.0, 1);

        order.addItem(item1);
        order.addItem(item2);

        order.removeItem(item1);

        assertEquals(1, order.getItems().size());
        assertEquals(Status.UNCONFIRMED, order.getStatus());
    }

    @Test
    @DisplayName("removeItem: Status becomes null when last item is removed")
    public void testRemoveLastItem_setsStatusToNull() throws Exception {
        Item item = createMockItem(1L, 10.0, 1);

        order.addItem(item);
        order.removeItem(item);

        assertTrue(order.getItems().isEmpty());
        assertNull(order.getStatus());
    }
}
Note on Reflection: Because status is a private field without a setter in Order.java, Reflection (getDeclaredField) is used in testAddItemWhenStatusIsPlaced_throwsException to force the PLACED state and test exception handling safely.

porque da ese error?

 create mode 100644 README.md

PS C:\Users\jdiaz\OneDrive - Servicat\Escritorio\Jonathan Personal\INVESTIGACION\2026b-ESPE-parte-d1> mvn test

[INFO] Scanning for projects...

[INFO] 

[INFO] -----------------< es.upm.grise.profundizacion:order >------------------

[INFO] Building order 0.0.1-SNAPSHOT

[INFO]   from pom.xml

[INFO] --------------------------------[ jar ]---------------------------------

[INFO] 

[INFO] --- resources:3.3.1:resources (default-resources) @ order ---

[INFO] skip non existing resourceDirectory C:\Users\jdiaz\OneDrive - Servicat\Escritorio\Jonathan Personal\INVESTIGACION\2026b-ESPE-parte-d1\src\main\resources

[INFO] 

[INFO] --- compiler:3.13.0:compile (default-compile) @ order ---

[INFO] Recompiling the module because of changed source code.

[INFO] Compiling 10 source files with javac [debug release 17] to target\classes

[INFO] 

[INFO] --- resources:3.3.1:testResources (default-testResources) @ order ---

[INFO] skip non existing resourceDirectory C:\Users\jdiaz\OneDrive - Servicat\Escritorio\Jonathan Personal\INVESTIGACION\2026b-ESPE-parte-d1\src\test\resources

[INFO] 

[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ order ---

[INFO] Recompiling the module because of changed dependency.

[INFO] Compiling 1 source file with javac [debug release 17] to target\test-classes

[INFO] -------------------------------------------------------------

[ERROR] COMPILATION ERROR : 

[INFO] -------------------------------------------------------------

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[4,26] package org.mockito does not exist

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[27,31] cannot find symbol

  symbol:   method mock(java.lang.Class<es.upm.grise.order.Product>)

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[28,9] cannot find symbol

  symbol:   method when(long)

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[30,25] cannot find symbol

  symbol:   method mock(java.lang.Class<es.upm.grise.order.Item>)

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[31,9] cannot find symbol

  symbol:   method when(es.upm.grise.order.Product)

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[32,9] cannot find symbol

  symbol:   method when(double)

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[33,9] cannot find symbol

  symbol:   method when(int)

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[110,9] cannot find symbol

  symbol:   method verify(es.upm.grise.order.Item)

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[111,23] cannot find symbol

  symbol:   method never()

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[111,41] cannot find symbol

  symbol:   method anyDouble()

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[124,9] cannot find symbol

  symbol:   method verify(es.upm.grise.order.Item)

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[125,9] cannot find symbol

  symbol:   method verify(es.upm.grise.order.Item)

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[138,9] cannot find symbol

  symbol:   method verify(es.upm.grise.order.Item)

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[139,23] cannot find symbol

  symbol:   method never()

  location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[139,41] cannot find symbol

  symbol:   method anyDouble()

  location: class es.upm.grise.order.OrderTest

[INFO] 15 errors 

[INFO] -------------------------------------------------------------

[INFO] ------------------------------------------------------------------------

[INFO] BUILD FAILURE

[INFO] ------------------------------------------------------------------------

[INFO] Total time:  2.992 s

[INFO] Finished at: 2026-07-21T10:13:24-05:00

[INFO] ------------------------------------------------------------------------

[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.13.0:testCompile (default-testCompile) on project order: Compilation failure: Compilation failure: 

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[4,26] package org.mockito does not exist

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[27,31] cannot find symbol

[ERROR]   symbol:   method mock(java.lang.Class<es.upm.grise.order.Product>)

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[28,9] cannot find symbol

[ERROR]   symbol:   method when(long)

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[30,25] cannot find symbol

[ERROR]   symbol:   method mock(java.lang.Class<es.upm.grise.order.Item>)

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[31,9] cannot find symbol

[ERROR]   symbol:   method when(es.upm.grise.order.Product)

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[32,9] cannot find symbol

[ERROR]   symbol:   method when(double)

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[33,9] cannot find symbol

[ERROR]   symbol:   method when(int)

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[110,9] cannot find symbol

[ERROR]   symbol:   method verify(es.upm.grise.order.Item)

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[111,23] cannot find symbol

[ERROR]   symbol:   method never()

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[111,41] cannot find symbol

[ERROR]   symbol:   method anyDouble()

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[124,9] cannot find symbol

[ERROR]   symbol:   method verify(es.upm.grise.order.Item)

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[125,9] cannot find symbol

[ERROR]   symbol:   method verify(es.upm.grise.order.Item)

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[138,9] cannot find symbol

[ERROR]   symbol:   method verify(es.upm.grise.order.Item)

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[139,23] cannot find symbol

[ERROR]   symbol:   method never()

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[139,41] cannot find symbol

[ERROR]   symbol:   method anyDouble()

[ERROR]   location: class es.upm.grise.order.OrderTest

[ERROR] -> [Help 1]

[ERROR] 

[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.

[ERROR] Re-run Maven using the -X switch to enable full debug logging.

[ERROR] 

[ERROR] For more information about the errors and possible solutions, please read the following articles:

[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException

PS C:\Users\jdiaz\OneDrive - Servicat\Escritorio\Jonathan Personal\INVESTIGACION\2026b-ESPE-parte-d1> 

Ese error ocurre porque tu proyecto Maven no tiene la librería de Mockito declarada en el archivo pom.xml.

El compilador intenta procesar las anotaciones e importaciones como import static org.mockito.Mockito.*; (mock, when, verify, etc.), pero no encuentra las librerías en el classpath.

¿Cómo solucionarlo?
Tienes dos opciones para resolverlo:

Opción 1: Agregar Mockito a tu pom.xml (Recomendado)
Abre tu archivo pom.xml y añade las dependencias de Mockito (y de JUnit 5 si no las tuvieras) dentro del bloque <dependencies>:

XML
<dependencies>
    <!-- JUnit 5 (Jupiter) -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.10.2</version>
        <scope>test</scope>
    </dependency>

    <!-- Mockito Core -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.11.0</version>
        <scope>test</scope>
    </dependency>
    <!-- Mockito JUnit Jupiter Extension -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <version>5.11.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
Una vez guardado el pom.xml, vuelve a ejecutar:

Bash
mvn clean test
Opción 2: Si no tienes permiso para modificar el pom.xml
Si es un examen o un entorno donde el pom.xml no se puede tocar y solo te permiten usar Java puro/JUnit sin librerías de mocks externas, tendrás que escribir la prueba instanciando objetos reales (o stubs manuales) en lugar de usar mock() y when().

Si este es tu caso, avísame y te adapto el archivo OrderTest.java sin usar Mockito.

este es el pom.xml:

Te faltó adjuntar el contenido del pom.xml en tu mensaje.

Por favor, pégalo aquí para que pueda revisarlo y decirte exactamente dónde agregar las dependencias o qué modificar.



<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"

  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

  <modelVersion>4.0.0</modelVersion>



  <groupId>es.upm.grise.profundizacion</groupId>

  <artifactId>order</artifactId>

  <version>0.0.1-SNAPSHOT</version>



  <name>order</name>

  <!-- FIXME change it to the project's website -->

  <url>http://www.example.com</url>



  <properties>

    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

    <maven.compiler.release>17</maven.compiler.release>

  </properties>



  <dependencies>

    <!--

    https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api -->

    <dependency>

      <groupId>org.junit.jupiter</groupId>

      <artifactId>junit-jupiter-api</artifactId>

      <version>5.13.4</version>

      <scope>test</scope>

    </dependency>

  </dependencies>



  <build>

    <pluginManagement><!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->

      <plugins>

        <!-- clean lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#clean_Lifecycle -->

        <plugin>

          <artifactId>maven-clean-plugin</artifactId>

          <version>3.4.0</version>

        </plugin>

        <!-- default lifecycle, jar packaging: see https://maven.apache.org/ref/current/maven-core/default-bindings.html#Plugin_bindings_for_jar_packaging -->

        <plugin>

          <artifactId>maven-resources-plugin</artifactId>

          <version>3.3.1</version>

        </plugin>

        <plugin>

          <artifactId>maven-compiler-plugin</artifactId>

          <version>3.13.0</version>

        </plugin>

        <plugin>

          <artifactId>maven-surefire-plugin</artifactId>

          <version>3.3.0</version>

        </plugin>

        <plugin>

          <artifactId>maven-jar-plugin</artifactId>

          <version>3.4.2</version>

        </plugin>

        <plugin>

          <artifactId>maven-install-plugin</artifactId>

          <version>3.1.2</version>

        </plugin>

        <plugin>

          <artifactId>maven-deploy-plugin</artifactId>

          <version>3.1.2</version>

        </plugin>

        <!-- site lifecycle, see https://maven.apache.org/ref/current/maven-core/lifecycles.html#site_Lifecycle -->

        <plugin>

          <artifactId>maven-site-plugin</artifactId>

          <version>3.12.1</version>

        </plugin>

        <plugin>

          <artifactId>maven-project-info-reports-plugin</artifactId>

          <version>3.6.1</version>

        </plugin>

      </plugins>

    </pluginManagement>

  </build>

</project>

¡Tal como sospechábamos! Tu pom.xml solo tiene la dependencia de JUnit 5 (junit-jupiter-api), pero no incluye Mockito.

Por esta razón, la prueba basada en mocks arroja el error package org.mockito does not exist.

Tienes dos formas claras de solucionarlo:

OPCIÓN A: Agregar Mockito a tu pom.xml (Si puedes editarlo)
Si estás trabajando en tu entorno local o tienes libertad para modificar el proyecto, reemplaza el bloque <dependencies> de tu pom.xml por el siguiente:

XML
  <dependencies>
    <!-- JUnit 5 API -->
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>5.13.4</version>
      <scope>test</scope>
    </dependency>

    <!-- Mockito Core (Necesario para mock, when, verify) -->
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-core</artifactId>
      <version>5.11.0</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
OPCIÓN B: Reescribir OrderTest.java sin Mockito (Si NO puedes tocar el pom.xml)
Si este ejercicio pertenece a una práctica/evaluación de la UPM donde el pom.xml debe mantenerse intacto, debes usar objetos concretos o una clase interna / stub en lugar de Mockito.

Aquí tienes la versión de OrderTest.java adaptada utilizando exclusivamente JUnit 5 puro, implementando una subclase/stub liviana de Item y Product:

Java
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

    // --- Clases Auxiliares (Stubs) para pruebas sin Mockito ---

    private static class DummyProduct extends Product {
        private final long id;

        public DummyProduct(long id) {
            this.id = id;
        }

        @Override
        public long getId() {
            return id;
        }
    }

    private static class DummyItem extends Item {
        private final Product product;
        private double price;
        private int quantity;

        public DummyItem(long productId, double price, int quantity) {
            this.product = new DummyProduct(productId);
            this.price = price;
            this.quantity = quantity;
        }

        @Override
        public Product getProduct() {
            return product;
        }

        @Override
        public double getPrice() {
            return price;
        }

        @Override
        public void setPrice(double price) {
            this.price = price;
        }

        @Override
        public int getQuantity() {
            return quantity;
        }

        @Override
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
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

        Item item = new DummyItem(1L, 10.0, 1);

        assertThrows(CannotAddItemsToPlacedOrderException.class, () -> {
            order.addItem(item);
        });
    }

    @Test
    @DisplayName("addItem: Lanza IncorrectItemException con precio negativo")
    public void testAddItemNegativePrice_throwsException() {
        Item item = new DummyItem(1L, -5.0, 1);

        assertThrows(IncorrectItemException.class, () -> {
            order.addItem(item);
        });
    }

    @Test
    @DisplayName("addItem: Lanza IncorrectItemException con cantidad menor o igual a cero")
    public void testAddItemZeroOrNegativeQuantity_throwsException() {
        Item itemZero = new DummyItem(1L, 10.0, 0);
        Item itemNeg = new DummyItem(2L, 10.0, -1);

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
        Item item = new DummyItem(1L, 10.0, 2);

        order.addItem(item);

        assertEquals(1, order.getItems().size());
        assertEquals(Status.UNCONFIRMED, order.getStatus());
    }

    @Test
    @DisplayName("addItem: Mismo producto con mismo precio incrementa la cantidad")
    public void testAddExistingItemSamePrice_incrementsQuantity() throws Exception {
        Item item1 = new DummyItem(1L, 10.0, 2);
        Item item2 = new DummyItem(1L, 10.0, 3);

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(1, order.getItems().size());
        assertEquals(5, order.getItems().get(0).getQuantity());
        assertEquals(10.0, order.getItems().get(0).getPrice());
    }

    @Test
    @DisplayName("addItem: Mismo producto con mayor precio actualiza la cantidad y el precio")
    public void testAddExistingItemHigherPrice_updatesQuantityAndPrice() throws Exception {
        Item item1 = new DummyItem(1L, 10.0, 2);
        Item item2 = new DummyItem(1L, 20.0, 3);

        order.addItem(item1);
        order.addItem(item2);

        assertEquals(1, order.getItems().size());
        assertEquals(5, order.getItems().get(0).getQuantity());
        assertEquals(20.0, order.getItems().get(0).getPrice());
    }

    @Test
    @DisplayName("addItem: Mismo producto con menor precio incrementa cantidad pero conserva precio mayor")
    public void testAddExistingItemLowerPrice_keepsHigherPrice() throws Exception {
        Item item1 = new DummyItem(1L, 20.0, 2);
        Item item2 = new DummyItem(1L, 10.0, 3);

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
        Item item = new DummyItem(1L, 10.0, 1);

        assertThrows(NonExistingItemException.class, () -> {
            order.removeItem(item);
        });
    }

    @Test
    @DisplayName("removeItem: Elimina el ítem de la lista")
    public void testRemoveItem_removesFromList() throws Exception {
        Item item1 = new DummyItem(1L, 10.0, 1);
        Item item2 = new DummyItem(2L, 15.0, 1);

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
        Item item = new DummyItem(1L, 10.0, 1);

        order.addItem(item);
        order.removeItem(item);

        assertTrue(order.getItems().isEmpty());
        assertNull(order.getStatus());
    }
}
Si usas la Opción B, puedes volver a ejecutar mvn test directamente sin modificar tu archivo pom.xml.

voy por la opcion b, pero me da error aqui

public DummyItem(long productId, double price, int quantity) {

            this.product = new DummyProduct(productId);

            this.price = price;

            this.quantity = quantity;

        }

Ese error ocurre porque la clase Item o Product de tu proyecto probablemente:

Tiene un constructor definido que requiere parámetros.

Es una interfaz o una clase abstracta sin constructor por defecto.

O los atributos de la clase padre son privados/diferentes a los de nuestro stub.

Para corregirlo de la forma más limpia y universal (sin importar cómo estén implementadas las clases originales Item o Product), utilizaremos clases anónimas inline dentro de un método auxiliar.

Reemplaza todo el contenido de tu OrderTest.java por esta versión final que no requiere subclases explícitas:

Java
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

    // Método auxiliar para crear Items dinámicos sin tocar constructores padres
    private Item createTestItem(long productId, double initialPrice, int initialQuantity) {
        Product product = new Product() {
            @Override
            public long getId() {
                return productId;
            }
        };

        return new Item() {
            private double currentPrice = initialPrice;
            private int currentQuantity = initialQuantity;

            @Override
            public Product getProduct() {
                return product;
            }

            @Override
            public double getPrice() {
                return currentPrice;
            }

            @Override
            public void setPrice(double price) {
                this.currentPrice = price;
            }

            @Override
            public int getQuantity() {
                return currentQuantity;
            }

            @Override
            public void setQuantity(int quantity) {
                this.currentQuantity = quantity;
            }
        };
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
Prueba corriendo de nuevo mvn test en tu consola.

PS C:\Users\jdiaz\OneDrive - Servicat\Escritorio\Jonathan Personal\INVESTIGACION\2026b-ESPE-parte-d1> mvn test

[INFO] Scanning for projects...

[INFO] 

[INFO] -----------------< es.upm.grise.profundizacion:order >------------------

[INFO] Building order 0.0.1-SNAPSHOT

[INFO]   from pom.xml

[INFO] --------------------------------[ jar ]---------------------------------

[INFO] 

[INFO] --- resources:3.3.1:resources (default-resources) @ order ---

[INFO] skip non existing resourceDirectory C:\Users\jdiaz\OneDrive - Servicat\Escritorio\Jonathan Personal\INVESTIGACION\2026b-ESPE-parte-d1\src\main\resources

[INFO] 

[INFO] --- compiler:3.13.0:compile (default-compile) @ order ---

[INFO] Nothing to compile - all classes are up to date.

[INFO] 

[INFO] --- resources:3.3.1:testResources (default-testResources) @ order ---

[INFO] skip non existing resourceDirectory C:\Users\jdiaz\OneDrive - Servicat\Escritorio\Jonathan Personal\INVESTIGACION\2026b-ESPE-parte-d1\src\test\resources

[INFO] 

[INFO] --- compiler:3.13.0:testCompile (default-testCompile) @ order ---

[INFO] Recompiling the module because of changed source code.

[INFO] Compiling 1 source file with javac [debug release 17] to target\test-classes

[INFO] -------------------------------------------------------------

[ERROR] COMPILATION ERROR : 

[INFO] -------------------------------------------------------------

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[33,16] constructor Item in class es.upm.grise.order.Item cannot be applied to given types;

  required: es.upm.grise.order.Product,int,double

  found:    no arguments

  reason: actual and formal argument lists differ in length

[INFO] 1 error

[INFO] -------------------------------------------------------------

[INFO] ------------------------------------------------------------------------

[INFO] BUILD FAILURE

[INFO] ------------------------------------------------------------------------

[INFO] Total time:  2.690 s

[INFO] Finished at: 2026-07-21T10:19:30-05:00

[INFO] ------------------------------------------------------------------------

[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.13.0:testCompile (default-testCompile) on project order: Compilation failure

[ERROR] /C:/Users/jdiaz/OneDrive - Servicat/Escritorio/Jonathan Personal/INVESTIGACION/2026b-ESPE-parte-d1/src/test/java/es/upm/grise/order/OrderTest.java:[33,16] constructor Item in class es.upm.grise.order.Item cannot be applied to given types;

[ERROR]   required: es.upm.grise.order.Product,int,double

[ERROR]   found:    no arguments

[ERROR]   reason: actual and formal argument lists differ in length

[ERROR] 

[ERROR] -> [Help 1]

[ERROR] 

[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.

[ERROR] Re-run Maven using the -X switch to enable full debug logging.

[ERROR] 

[ERROR] For more information about the errors and possible solutions, please read the following articles:

[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException

PS C:\Users\jdiaz\OneDrive - Servicat\Escritorio\Jonathan Personal\INVESTIGACION\2026b-ESPE-parte-d1> 

¡Ahí está la clave! El compilador te lo dijo exacto:

required: es.upm.grise.order.Product, int, double

La clase Item requiere obligatoriamente un constructor con 3 parámetros: (Product, int quantity, double price). No tiene un constructor vacío.

Sabiendo la firma exacta del constructor de Item, no necesitamos hacer trucos raros con métodos abstractos. Podemos instanciar Item directamente.

Sustituye todo tu OrderTest.java por esta versión limpia:

Java
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

    // Helper para crear un Product simulado
    private Product createProduct(long id) {
        return new Product() {
            @Override
            public long getId() {
                return id;
            }
        };
    }

    // Helper para crear un Item usando su constructor real (Product, quantity, price)
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
Vuelve a ejecutar mvn test y ya debería compilar y pasar las pruebas sin problemas.

[INFO] ------------------------------------------------------------------------

[ERROR] Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:3.3.0:test (default-test) on project order: There are test failures.

[ERROR] 

[ERROR] Please refer to C:\Users\jdiaz\OneDrive - Servicat\Escritorio\Jonathan Personal\INVESTIGACION\2026b-ESPE-parte-d1\target\surefire-reports for the individual test results.

[ERROR] Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.

[ERROR] -> [Help 1]

[ERROR] 

[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.

[ERROR] Re-run Maven using the -X switch to enable full debug logging.

[ERROR] 

[ERROR] For more information about the errors and possible solutions, please read the following articles:

[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException

PS C:\Users\jdiaz\OneDrive - Servicat\Escritorio\Jonathan Personal\INVESTIGACION\2026b-ESPE-parte-d1> 

¡Avanzamos un paso importante! El código ya compiló perfectamente (el error anterior de tipos desapareció). Ahora el fallo es que una o más pruebas unitarias fallaron durante la ejecución (There are test failures).

Para saber exactamente cuál falló y por qué, puedes ejecutar el siguiente comando en tu consola de PowerShell:

PowerShell
Get-Content target\surefire-reports\*.txt
O si prefieres un reporte más limpio directamente desde Maven, ejecuta:

PowerShell
mvn test -Dtest=OrderTest
Diagnóstico probable
Lo más común al instanciar Product directamente como una clase abstracta o concreta es cómo está implementada la clase Product.java en tu proyecto.

Si Product no es una interfaz sino una clase con un constructor obligatorio (por ejemplo, new Product(long id)), nuestro método helper createProduct estará fallando.

Sube o revisa la cabecera de las clases Product.java e Item.java de tu proyecto para asegurarnos de que la instanciación en las pruebas encaje exactamente con su código.

esta es product.java

package es.upm.grise.order;



public class Product {

   

    long id;



    public long getId() {

       

        return id;

       

    }



}

Ahí está la causa del fallo. En Product.java:

El atributo id no se inicializa en ningún lado ni hay un constructor que lo reciba, por lo que su valor por defecto es 0.

En las pruebas estábamos haciendo new Product() { get... }, pero al pasárselo a Item, la clase Item probablemente crea sus propios objetos o accede directamente al campo de Product, devolviendo siempre 0 en lugar del ID esperado.

Para resolverlo sin modificar la arquitectura original, asignaremos el valor del id directamente mediante Reflexión al instanciar Product.

Reemplaza tu archivo OrderTest.java con esta versión corregida:

Java
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
Vuelve a ejecutar mvn test y confirma los resultados.




Gemini es una IA y puede comete