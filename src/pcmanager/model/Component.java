package pcmanager.model;
import es.upv.inf.Product;
import java.io.IOException;
import java.io.Serializable;


public class Component implements Serializable {
    private int quantity;
    private SerializableProduct product;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        Product p = new Product(product.getDescription(), product.getPrice(), product.getStock(), product.getCategory());
        return p;
    }

    public void setProduct(Product product) {
        this.product = new SerializableProduct(product);
    }

    public Component(int quantity, Product product) {
        this.quantity = quantity;
        this.product = new SerializableProduct(product);
    }
    
    
}
