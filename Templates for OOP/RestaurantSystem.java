import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/*
 * EXAM QUESTION (typical phrasing):
 * Design a restaurant order management system with the following requirements:
 * a) Each menu item has a unique code, name, price, and category
 *    (Food or Drink). Use inheritance/polymorphism for category-specific
 *    behavior (e.g. drinks may have an "alcoholic" flag affecting age rules).
 * b) A customer can place an order with multiple menu items and quantities.
 * c) Orders should be sent to the kitchen and processed one at a time per
 *    chef, but multiple chefs can process different orders concurrently.
 * d) Implement a method to calculate the total bill, including a service
 *    charge percentage.
 * e) Implement a method to mark an order as completed.
 * f) Ensure encapsulation and validate all inputs (e.g. no negative quantity).
 *
 * DESIGN NOTES:
 * - MenuItem (abstract) -> FoodItem, DrinkItem: polymorphism via
 *   getDescription().
 * - Orders flow through a BlockingQueue to simulate a kitchen queue; multiple
 *   "Chef" worker threads pull from the queue and process orders concurrently,
 *   demonstrating a realistic producer-consumer multithreading scenario.
 */

abstract class MenuItem {
    protected final String code;
    protected String name;
    protected double price;

    public MenuItem(String code, String name, double price) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Item code cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be empty.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getDescription(); // polymorphic
}

class FoodItem extends MenuItem {
    private int prepTimeMinutes;

    public FoodItem(String code, String name, double price, int prepTimeMinutes) {
        super(code, name, price);
        if (prepTimeMinutes <= 0) {
            throw new IllegalArgumentException("Prep time must be positive.");
        }
        this.prepTimeMinutes = prepTimeMinutes;
    }

    public int getPrepTimeMinutes() {
        return prepTimeMinutes;
    }

    @Override
    public String getDescription() {
        return "Food: " + name + " (prep " + prepTimeMinutes + " min)";
    }
}

class DrinkItem extends MenuItem {
    private boolean alcoholic;

    public DrinkItem(String code, String name, double price, boolean alcoholic) {
        super(code, name, price);
        this.alcoholic = alcoholic;
    }

    public boolean isAlcoholic() {
        return alcoholic;
    }

    @Override
    public String getDescription() {
        return "Drink: " + name + (alcoholic ? " (alcoholic)" : "");
    }
}

class OrderItem {
    private final MenuItem item;
    private final int quantity;

    public OrderItem(MenuItem item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        this.item = item;
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return item.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return quantity + "x " + item.getName();
    }
}

class CustomerOrder {
    private static int counter = 0;
    private final int orderId;
    private final String tableNumber;
    private final List<OrderItem> orderItems;
    private static final double SERVICE_CHARGE_RATE = 0.10;
    private volatile boolean completed = false; // volatile: visible across threads immediately

    public CustomerOrder(String tableNumber) {
        if (tableNumber == null || tableNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Table number cannot be empty.");
        }
        this.orderId = ++counter;
        this.tableNumber = tableNumber;
        this.orderItems = new ArrayList<>();
    }

    public void addItem(MenuItem item, int quantity) {
        orderItems.add(new OrderItem(item, quantity));
    }

    public double calculateTotalBill() {
        double subtotal = orderItems.stream().mapToDouble(OrderItem::getSubtotal).sum();
        return subtotal + (subtotal * SERVICE_CHARGE_RATE);
    }

    public void markCompleted() {
        completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Order #" + orderId + " (Table " + tableNumber + "): " + orderItems;
    }
}

// ---------- Chef worker thread: consumes orders from the kitchen queue ----------
class Chef implements Runnable {
    private final String name;
    private final BlockingQueue<CustomerOrder> kitchenQueue;
    private static final CustomerOrder POISON_PILL = new CustomerOrder("POISON");

    public Chef(String name, BlockingQueue<CustomerOrder> kitchenQueue) {
        this.name = name;
        this.kitchenQueue = kitchenQueue;
    }

    static CustomerOrder getPoisonPill() {
        return POISON_PILL;
    }

    @Override
    public void run() {
        try {
            while (true) {
                CustomerOrder order = kitchenQueue.take();
                if (order == POISON_PILL) {
                    System.out.println(name + " is going home, no more orders.");
                    break;
                }
                System.out.println(name + " started preparing " + order);
                Thread.sleep(200); // simulate cooking time
                order.markCompleted();
                System.out.println(name + " completed " + order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + " was interrupted: " + e.getMessage());
        }
    }
}

// ---------- Demo ----------
public class RestaurantSystem {
    public static void main(String[] args) {
        try {
            FoodItem pizza = new FoodItem("F1", "Margherita Pizza", 8000, 15);
            DrinkItem soda = new DrinkItem("D1", "Coca-Cola", 1500, false);

            CustomerOrder order1 = new CustomerOrder("Table 1");
            order1.addItem(pizza, 2);
            order1.addItem(soda, 2);

            CustomerOrder order2 = new CustomerOrder("Table 2");
            order2.addItem(pizza, 1);

            System.out.printf("Order 1 total: %.2f%n", order1.calculateTotalBill());
            System.out.printf("Order 2 total: %.2f%n", order2.calculateTotalBill());

            // Kitchen: two chefs processing orders concurrently from a shared queue
            BlockingQueue<CustomerOrder> kitchenQueue = new LinkedBlockingQueue<>();
            ExecutorService executor = Executors.newFixedThreadPool(2);

            executor.execute(new Chef("Chef Marie", kitchenQueue));
            executor.execute(new Chef("Chef Paul", kitchenQueue));

            kitchenQueue.put(order1);
            kitchenQueue.put(order2);
            // Send one poison pill per chef so both threads know to stop
            kitchenQueue.put(Chef.getPoisonPill());
            kitchenQueue.put(Chef.getPoisonPill());

            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);

            System.out.println("Order 1 completed? " + order1.isCompleted());
            System.out.println("Order 2 completed? " + order2.isCompleted());

        } catch (IllegalArgumentException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
