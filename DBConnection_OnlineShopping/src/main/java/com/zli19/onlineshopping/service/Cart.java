
package com.zli19.onlineshopping.service;

import com.zli19.onlineshopping.entity.Product;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;



/**
 *
 * @author zhiku
 */
public abstract class Cart{    
    public static final HashMap<Product, Integer> productsInCart = new HashMap<>();
    public static final File FILE = new File("chart.txt");
    
    static {
        try {
            if(!FILE.createNewFile()){
                try(FileInputStream fis = new FileInputStream(FILE);
                        ObjectInputStream is = new ObjectInputStream(fis);){
                    while(true){
                        productsInCart.put((Product)is.readObject(),(Integer)is.readObject());
                    }
                }catch(EOFException ex){                   
                }catch(IOException | ClassNotFoundException ex){
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }

    static double getTotal() {
        double sum = 0;
        for(Map.Entry<Product, Integer> entry : Cart.productsInCart.entrySet()){
            sum += entry.getKey().getPricePerUnit().doubleValue()*entry.getValue();        
        }
        return sum;
    }
    
    public static void save(){
        try(FileOutputStream fos = new FileOutputStream(FILE);
            ObjectOutputStream os = new ObjectOutputStream(fos);){
            for(Map.Entry<Product, Integer> entry : Cart.productsInCart.entrySet()){
                os.writeObject(entry.getKey());
                os.writeObject(entry.getValue());
                os.flush();
            }       
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void clear(){
        productsInCart.clear();
    }
    
    public static void show(){
        System.out.println("\t--Cart--");
        System.out.println("\tProductID\tName\t\tPrice\t\tAmount");
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Product, Integer> entry : productsInCart.entrySet()){          
            sb.append("\t")
            .append(entry.getKey().getProductID())
            .append("\t\t")
            .append(entry.getKey().getProductName())
            .append("\t\t")
            .append(entry.getKey().getPricePerUnit())
            .append("\t\t")
            .append(entry.getValue())
            .append("\n");
        }
        System.out.print(sb.toString());
        System.out.println("\tTotal:\t" + Cart.getTotal());    
    }
}
