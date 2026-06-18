import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * EXAM QUESTION (typical phrasing):
 * Design an inventory/online store management system with the following
 * requirements:
 * a) Each product has a unique product code, name, price, and quantity in stock.
 * b) There are different categories of products: PhysicalProduct (has weight,
 *    affects shipping cost) and DigitalProduct (no shipping cost). Use
 *    inheritance and polymorphism for cost calculation.
 * c) Customers can place orders containing multiple products and quantities.
 * d) An order cannot be placed if there isn't enough stock for any item in it.
 * e) Implement methods to add stock and to place an order (which reduces stock).
 * f) Implement a method to calculate the total cost of an order, including
 *    shipping where applicable.
 * g) Implement a method to display all products that are low in stock
 *    (below a given threshold).
 * h) Ensure encapsulation and validate all inputs.
 *
 * DESIGN NOTES:
 * - Product (abstract) -> PhysicalProduct, DigitalProduct: polymorphism via
 *   getShippingCost().
 * - Order holds a Map<Product, Integer> (product -> quantity ordered).
 * - Inventory is the controller: validates stock availability for the WHOLE
 *   order BEFORE deducting anything (so a failed order doesn't partially
 *   apply, keeping data consistent).
 */

class OutOfStockException extends Exception {
    public OutOfStockException(String message) {
        super(message);
    }
}

abstract class Product {
    protected final String code;
    protected String name;
    protected double price;
    private int quantityInStock;

    public Product(String code, String name, double price, int initialStock) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Product code cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (initialStock < 0) {
            throw new IllegalArgumentException("Initial stock cannot be negative.");
        }
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantityInStock = initialStock;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void addStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Stock to add must be positive.");
        }
        quantityInStock += amount;
    }

    void reduceStock(int amount) throws OutOfStockException {
        if (amount > quantityInStock) {
            throw new OutOfStockException(
                    "Not enough stock for " + name + ": requested " + amount + ", available " + quantityInStock);
        }
        quantityInStock -= amount;
    }

    // polymorphic: physical products cost extra to ship, digital ones don't
    public abstract double getShippingCost(int quantity);

    @Override
    public String toString() {
        return name + " [" + code + "] - " + price + " (" + quantityInStock + " in stock)";
    }
}

class PhysicalProduct extends Product {
    private double weightKg;
    private static final double SHIPPING_RATE_PER_KG = 500.0;

    public PhysicalProduct(String code, String name, double price, int initialStock, double weightKg) {
        super(code, name, price, initialStock);
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Weight must be positive.");
        }
        this.weightKg = weightKg;
    }

    @Override
    public double getShippingCost(int quantity) {
        return weightKg * quantity * SHIPPING_RATE_PER_KG;
    }
}

class DigitalProduct extends Product {
    public DigitalProduct(String code, String name, double price, int initialStock) {
        super(code, name, price, initialStock);
    }

    @Override
    public double getShippingCost(int quantity) {
        return 0.0; // no physical shipping
    }
}

// ---------- Order ----------
class Order {
    private final Map<Product, Integer> items;

    public Order() {
        this.items = new HashMap<>();
    }

    public void addItem(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive.");
        }
        items.merge(product, quantity, Integer::sum);
    }

    public Map<Product, Integer> getItems() {
        return new HashMap<>(items);
    }

    public double calculateTotalCost() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            total += p.getPrice() * qty;
            total += p.getShippingCost(qty);
        }
        return total;
    }
}

// ---------- Inventory (controller) ----------
class Inventory {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    // Validates ALL items have enough stock BEFORE deducting any of them
    public void placeOrder(Order order) throws OutOfStockException {
        Map<Product, Integer> requested = order.getItems();

        for (Map.Entry<Product, Integer> entry : requested.entrySet()) {
            Product p = entry.getKey();
            int qty = entry.getValue();
            if (qty > p.getQuantityInStock()) {
                throw new OutOfStockException(
                        "Cannot fulfill order: " + p.getName() + " requested " + qty +
                                " but only " + p.getQuantityInStock() + " in stock.");
            }
        }
        // All checks passed -> safe to deduct
        for (Map.Entry<Product, Integer> entry : requested.entrySet()) {
            entry.getKey().reduceStock(entry.getValue());
        }
        System.out.printf("Order placed successfully. Total: %.2f%n", order.calculateTotalCost());
    }

    public void displayLowStock(int threshold) {
        System.out.println("Products below stock threshold of " + threshold + ":");
        boolean any = false;
        for (Product p : products) {
            if (p.getQuantityInStock() < threshold) {
                System.out.println("  - " + p);
                any = true;
            }
        }
        if (!any) {
            System.out.println("  (none)");
        }
    }
}

// ---------- Demo ----------
public class InventorySystem {
    public static void main(String[] args) {
        try {
            Inventory inventory = new Inventory();

            Product laptop = new PhysicalProduct("P1", "Laptop", 800000, 5, 2.5);
            Product ebook = new DigitalProduct("D1", "Java OOP eBook", 15000, 100);
            inventory.addProduct(laptop);
            inventory.addProduct(ebook);

            Order order = new Order();
            order.addItem(laptop, 2);
            order.addItem(ebook, 3);

            inventory.placeOrder(order);

            inventory.displayLowStock(5); // laptop now at 3, below threshold

            laptop.addStock(10);
            inventory.displayLowStock(5); // laptop now restocked

            // This should fail - not enough laptops in stock
            Order bigOrder = new Order();
            bigOrder.addItem(laptop, 999);
            inventory.placeOrder(bigOrder);

        } catch (OutOfStockException e) {
            System.out.println("Order failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}
