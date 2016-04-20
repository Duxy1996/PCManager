package pcmanager.model;

import es.upv.inf.Product;
import es.upv.inf.Product.Category;
import java.io.Serializable;
import java.util.ArrayList;

public class PC implements Serializable {
    private String name;
    private ArrayList<Component> components;

    public PC(String nombre) {
        this.name = nombre;
        this.components = new ArrayList<Component>();
    }

    public PC(String nombre, ArrayList<Component> componentes) {
        this.name = nombre;
        this.components = componentes;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }
    
    public boolean isValid() {
        boolean cage = false, cpu = false, gpu = false, hdd = false, ssd = false, mother = false, ram = false;       
        
        for(Component c : components) {
            if(Product.Category.CASE == c.getProduct().getCategory()) cage = true;
            if(Product.Category.CPU == c.getProduct().getCategory()) cpu = true;
            if(Product.Category.GPU == c.getProduct().getCategory()) gpu = true;
            if(Product.Category.HDD == c.getProduct().getCategory()) hdd = true;
            if(Product.Category.HDD_SSD == c.getProduct().getCategory()) ssd = true;
            if(Product.Category.MOTHERBOARD == c.getProduct().getCategory()) mother = true;
            if(Product.Category.RAM == c.getProduct().getCategory()) ram = true;            
            if(cage && cpu && gpu && (hdd || ssd) && mother && ram) return true;
        }            
        return false;
    }
    
    
}
