import java.io.*;
import java.util.*;

class Product {
    private UUID id;
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}

class ProductManagementSystem {
    public Map<UUID, Product> products = new HashMap<>();
    private String purchaseHistoryFile = "purchase_history.txt";

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public void removeProduct(UUID productId, int quantityToRemove) {
        Product product = products.get(productId);
        if (product != null) {
            int updatedQuantity = product.getQuantity() - quantityToRemove;
            if (updatedQuantity <= 0) {
                products.remove(productId);
            } else {
                product.setQuantity(updatedQuantity);
            }
        } else {
            System.out.println("Product not found!");
        }
    }

    public void updateProduct(UUID productId, Product updatedProduct) {
        if (products.containsKey(productId)) {
            products.put(productId, updatedProduct);
        } else {
            System.out.println("Product not found!");
        }
    }

    public void displayProducts() {
        for (Product product : products.values()) {
            System.out.println(product);
        }
    }

    public void logPurchase(UUID productId, int quantity) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(purchaseHistoryFile, true))) {
            String timestamp = new Date().toString();
            writer.println("Timestamp: " + timestamp);
            writer.println("Product UUID: " + productId);
            writer.println("Quantity Purchased: " + quantity);
            writer.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasProducts() {
        return !products.isEmpty();
    }
}

class StoreManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductManagementSystem productManagementSystem = new ProductManagementSystem();

        while (true) {
            System.out.print("Are you entering as a customer or retailer? (customer/retailer/exit): ");
            String userType = scanner.nextLine();

            if (userType.equalsIgnoreCase("customer")) {
                if (productManagementSystem.hasProducts()) {
                    System.out.println("Available Products:");
                    productManagementSystem.displayProducts();

                    System.out.print("\nEnter UUID of the product you want to purchase: ");
                    UUID productIdPurchased = UUID.fromString(scanner.nextLine());

                    System.out.print("Enter quantity to purchase: ");
                    int quantityPurchased = scanner.nextInt();

                    Product purchasedProduct = productManagementSystem.products.get(productIdPurchased);
                    if (purchasedProduct != null && purchasedProduct.getQuantity() >= quantityPurchased) {
                        purchasedProduct.setQuantity(purchasedProduct.getQuantity() - quantityPurchased);
                        productManagementSystem.logPurchase(productIdPurchased, quantityPurchased);
                        System.out.println("Purchase successful!");
                    } else {
                        System.out.println("Product not available in sufficient quantity.");
                    }
                } else {
                    System.out.println("There are no products available to buy.");
                }
            } else if (userType.equalsIgnoreCase("retailer")) {
                while (true) {
                    System.out.println("\nRetailer Management:");
                    System.out.println("1. Add Product");
                    System.out.println("2. Remove Product");
                    System.out.println("3. View Stock");
                    System.out.println("4. Exit");

                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1:
                            System.out.print("Enter product name: ");
                            String name = scanner.nextLine();

                            System.out.print("Enter product price: ");
                            double price = scanner.nextDouble();

                            System.out.print("Enter product quantity: ");
                            int quantity = scanner.nextInt();
                            scanner.nextLine(); // Consume newline

                            Product product = new Product(name, price, quantity);
                            productManagementSystem.addProduct(product);
                            System.out.println("Product added successfully!");
                            break;
                        case 2:
                            System.out.print("Enter UUID of the product to remove: ");
                            UUID productIdToRemove = UUID.fromString(scanner.nextLine());
                            
                            System.out.print("Enter quantity to remove: ");
                            int quantityToRemove = scanner.nextInt();
                            scanner.nextLine(); // Consume newline

                            productManagementSystem.removeProduct(productIdToRemove, quantityToRemove);
                            System.out.println("Product removed successfully!");
                            break;
                        case 3:
                            System.out.println("Current Stock:");
                            productManagementSystem.displayProducts();
                            break;
                        case 4:
                            System.out.println("Exiting Retailer Mode...");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }

                    if (choice == 4) {
                        break;
                    }
                }
            } else if (userType.equalsIgnoreCase("exit")) {
                System.out.println("Exiting Store Management System...");
                break;
            } else {
                System.out.println("Invalid user type. Please enter as customer, retailer, or exit.");
            }
        }

        scanner.close();
    }
}