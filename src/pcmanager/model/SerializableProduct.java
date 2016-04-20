package pcmanager.model;

import es.upv.inf.Product;
import es.upv.inf.Product.Category;
import java.io.Serializable;


public class SerializableProduct implements Serializable {
    private String description;
    private double price;
    private int stock;
    private Category category;

    public SerializableProduct() {
    }
    
    public SerializableProduct(Product p) {
        this.description = p.getDescription();
        this.price = p.getPrice();
        this.stock = p.getStock();
        this.category = p.getCategory();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    
}
