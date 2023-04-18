
package com.zli19.onlineshopping.service;

import com.zli19.onlineshopping.dao.impl.OrderDAO;
import com.zli19.onlineshopping.dao.impl.OrderProductDAO;
import com.zli19.onlineshopping.dao.impl.ProductDAO;
import com.zli19.onlineshopping.dao.impl.UserDAO;
import com.zli19.onlineshopping.entity.Order;
import com.zli19.onlineshopping.entity.OrderProduct;
import com.zli19.onlineshopping.entity.Product;
import com.zli19.onlineshopping.entity.User;
import com.zli19.onlineshopping.util.Constants;
import com.zli19.onlineshopping.util.Transaction;
import com.zli19.onlineshopping.util.Util;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author zhiku
 */
public class Service {
    
    private static User user = null;

    public static void showProduct() {
        ProductDAO productDAO = new ProductDAO();
        Transaction.begin();
        boolean flag = true;
        while(flag){
            System.out.println("\t--Browse or Search--");
            String chosen = Util.input("1.Show All 2.Search 3.Exit >")
                    .strip()
                    .toLowerCase();            
                
            switch(chosen){
                case "1", "show all" -> { 
                    System.out.println("\tProductID\t\tName\t\tPrice\t\tAvailable");
                    ArrayList<Product> list = productDAO.queryAll();
                    for (Product product : list) {
                        StringBuilder sb = new StringBuilder();                              
                        sb.append("\t\t")
                        .append(product.getProductID())
                        .append("\t\t")
                        .append(product.getProductName())
                        .append("\t\t")
                        .append(product.getPricePerUnit())
                        .append("\t\t")
                        .append(product.getAvailableForSale());
                        System.out.println(sb.toString());
                    }
                    if(user != null){
                        addItems();
                    }
                }
                case "2", "search" -> {                   
                    String name = Util
                            .input("Please enter the name of the product you want to search >")
                            .toLowerCase();
                    ArrayList<Product> result = productDAO.queryBy("productName", name);
                    if(result.isEmpty()){
                        System.out.println("Sorry. We are not selling " + name + ".");
                    }else{    
                        for (Product product : result) {
                            System.out.println(product);
                        } 
                        if(user != null){
                            addItems();
                        }                       
                    }
                }
                case "3", "exit" -> flag = false;
            }
        }
        Transaction.end();
    }

    public static boolean login() {
        UserDAO userDAO = new UserDAO();
        Transaction.begin();
        while(true){
            System.out.println("\t--Sign In--");
            String name = Util.input("Enter your user name >").strip();
            String password = Util.input("Enter your password >");
            ArrayList<User> result = userDAO.queryBy("userName", name);
            if(result.isEmpty() || !password.equals(result.get(0).getPassword())){
                System.out.println("Sorry, there was a problem. Your user name or password was incorrect. ");
                String answer = Util
                        .input("Try again?(Y/N)>" )
                        .strip()
                        .toLowerCase();
                if("n".equals(answer) || "no".equals(answer)){
                    Transaction.end();
                    return false;
                }
                continue;
            }
            user = result.get(0);
            System.out.println("Welcome back, " + user.getUserName());
            break;   
        }
        Transaction.end();
        return true;
    }

    public static boolean register() {
        Transaction.begin();
        UserDAO userDAO = new UserDAO();

        while(true){
            System.out.println("\t--Register--");
            String name = Util.input("Enter a user name >").strip();
            if(!userDAO.queryBy("userName", name).isEmpty()){
                System.out.println("This name has been used.");
                String answer = Util
                        .input("Enter \"exit\" to leave, otherwise try again.>" )
                        .strip()
                        .toLowerCase();
                if("exit".equals(answer)){
                    Transaction.end();
                    return false;
                }
                continue;
            }
            String password;
            while(true){
                password = Util.input("Enter your password >");  
                if(password.equals(Util.input("Reenter your password >"))){
                    break;
                }
                System.out.println("The password entered does not match!");
                String answer = Util
                        .input("Enter \"exit\" to leave, otherwise try again.>" )
                        .strip()
                        .toLowerCase();
                if("exit".equals(answer)){
                    Transaction.end();
                    return false;
                }
            }
      
            userDAO.insert(new User(name, null, null, password));
            break;
        }
        Transaction.commit();
        Transaction.end();  
        return true;
    }

    private static void addItems() {          
        while(true){
            System.out.println("\t--Add to Chart--");
            System.out.println("Enter productID and amount to add to your chart Or \"exit\" to leave.");
            String productID = Util.input("productID: ").strip();
            if("exit".equalsIgnoreCase(productID)){
                break;
            }
            ProductDAO productDAO = new ProductDAO();
            ArrayList<Product> pl = productDAO.queryBy("productID", productID);
            if(pl.isEmpty()){
                System.out.println("Not found.");
                continue;
            }
            Product product = pl.get(0);
            
            String amountString = Util.input("amount: ").strip();
            Integer amount = null;
            while(!"exit".equalsIgnoreCase(amountString) && amount == null){              
                try{
                    int temp = Integer.parseInt(amountString);   
                    if(temp > product.getAvailableForSale()){
                        System.out.println("Beyond the amount available.");
                        amountString = Util.input("amount >").strip();
                    }else if(temp <= 0){
                        System.out.println("Invalid input.");
                        amountString = Util.input("amount >").strip();
                    }else{
                        amount = temp;
                    }              
                }catch(NumberFormatException ex){
                    System.out.println("Invalid input.");
                    amountString = Util.input("amount >").strip();
                }
            }
            if("exit".equalsIgnoreCase(amountString)){
                break;
            }
            
            addToChart(product, amount);                     
        }
    }

    private static void checkOut () {
        if(!isChartAvailable()){
            System.out.println("Please modify your chart.");
        }else{
            System.out.println("\t--Checkout--");
            System.out.print("Please enter your Shipping address.\n>");
            String shippingAddress = new Scanner(System.in).nextLine();
            String payMethod = null;
            while(payMethod == null){
                String input = Util.input("""
                                          Please choose your prefered Payment method.
                                          1.credit card
                                          2.debit card
                                          3.gift card
                                          >""").strip();
                switch(input){
                    case "1" -> payMethod = "credit card";             
                    case "2" -> payMethod = "debit card";
                    case "3" -> payMethod = "gift card";
                   default -> System.out.println("Invalid input.");
                }
            }

            System.out.println("Please review your order.");
            Cart.show();
            System.out.println("\tShipping address: " + shippingAddress);
            System.out.println("\tPayment method: " + payMethod);

            Boolean isPaid = null;
            while(isPaid == null){
                String input = Util.input("Confirm to check out?(Y/N)>").strip().toLowerCase();
                switch(input){
                    case "n","no" -> isPaid = false;                
                    case "y","yes" -> isPaid = true;                       
                }
            }

            placeOrder(shippingAddress, isPaid);
            Cart.clear();
            Cart.save();
            if(isPaid){
                System.out.println("Your order is paid and will be processed.");
                checkOrder();
            }else{
                System.out.println("Your order is established and waiting for payment.");
                checkOrder();
            }
        }        
    }

    public static void logout() {
        user = null;
        System.out.println("You have successfully logged out.");
    }


    public static void checkOrder() {
        OrderDAO orderDAO = new OrderDAO();
        ArrayList<Order> orders = orderDAO.queryBy("userName", user.getUserName());
        if(orders.isEmpty()){
            System.out.println("You have no record of order.");
        }else{
            System.out.println("\t--Check Your Orders--"); 
            OrderProductDAO orderProductDAO = new OrderProductDAO();
            ProductDAO productDAO = new ProductDAO();
            
            boolean canPay = false;
            boolean canCancel = false;
            for(Order order : orders){
                StringBuilder sb = new StringBuilder("\n\torderID\t\tdate\t\ttime\t\tstatus\t\t\tship to\n\t");
                sb.append(order.getOrderID())
                        .append("\t\t")
                        .append(order.getOrderDate())
                        .append("\t")
                        .append(order.getTime())
                        .append("\t")
                        .append(order.getStatus())
                        .append("\t")
                        .append(order.getShippingAddress());
                System.out.println(sb.toString());
                System.out.println("\t---------------------------------------------------");
                
                List<SimpleEntry> list = orderProductDAO.queryBy("orderID", order.getOrderID())
                        .stream()
                        .map((value)->new SimpleEntry(productDAO.queryBy("productID", value.getProductID()).get(0), value.getAmountSold()))
                        .toList();
                System.out.println("\tProductID\tName\t\tPrice\t\tAmount");
                StringBuilder listSb = new StringBuilder();
                for(SimpleEntry entry : list){          
                    listSb.append("\t")
                    .append(((Product)entry.getKey()).getProductID())
                    .append("\t\t")
                    .append(((Product)entry.getKey()).getProductName())
                    .append("\t\t")
                    .append(((Product)entry.getKey()).getPricePerUnit())
                    .append("\t\t")
                    .append(entry.getValue())
                    .append("\n");
                }
                System.out.print(listSb.toString());
                if(!canPay){
                    canPay = Constants.WAIT_FOR_PAYMENT.equals(order.getStatus());
                }
                if(!canCancel){
                    canCancel = Constants.PAYMENT_RECEIVED.equals(order.getStatus())
                            || Constants.WAIT_FOR_PAYMENT.equals(order.getStatus());
                }            
            }
        }        
    }

    public static void showChart() {             
        boolean flag = true;
        while(flag){
            if(Cart.productsInChart.isEmpty()){
                System.out.println("Your chart is now empty.");
                flag = false;
            }else{
                Cart.show();    
                String input = Util.input("""
                                          What dou you want to do with your chart?
                                          1.Checkout
                                          2.Modify
                                          3.Clear
                                          4.Exit
                                          >""").strip().toLowerCase();
                switch (input) {
                    case "1","checkout" -> checkOut();
                    case "2","modify" -> modifyChart();
                    case "3","clear" -> clearChart();
                    case "4","exit" -> flag = false;
                }
            }
        }
    }

    private static void addToChart(Product product, int amount) {       
        Cart.productsInChart.put(product, Cart.productsInChart.getOrDefault(product, 0)+amount);
        Cart.save();
        System.out.println("Successfully add "
                + amount + (amount == 1 ? " unit of " : " units of ") 
                + product.getProductName() 
                + " to your chart.");
    }

    private static void modifyChart() {
        System.out.println("Choose the product in your chart you want to edit.");
        int amountbefore = -1;
        Product product = null;
        while(amountbefore == -1){
            String productID = Util.input("productID: ").strip();
            for (Map.Entry<Product, Integer> entry : Cart.productsInChart.entrySet()) {
                if(entry.getKey().getProductID().equals(productID)){
                    product = entry.getKey();
                    amountbefore = entry.getValue();
                }
            }                    
            if(amountbefore == -1){
                System.out.println("Not found in your chart.");
            }         
        }
        
        System.out.println("You hava " + amountbefore + " in your chart.");
        int amountafter = -1;
        while(amountafter < 0){
            String amountString = Util.input("Now change the number to >").strip();
            try{
                amountafter = Integer.parseInt(amountString);                  
            }catch(NumberFormatException ex){
                System.out.println("Invalid input.");
                continue;
            }
            if(amountafter < 0){
                System.out.println("Invalid input.");
            }  
        }
        
        if(amountafter == 0){
            Cart.productsInChart.remove(product);
            System.out.println("Successfully deleted "
                    + product.getProductName()
                    + " from your chart.");
        }else{
            Cart.productsInChart.replace(product, amountafter);  
            System.out.println("Successfully changed "
                    + product.getProductName()
                    + " from " + amountbefore
                    + " to " + amountafter
                    + " in your chart.");
        }
        Cart.save();
    }

    private static void clearChart() {
        Cart.clear();
        Cart.save();
    }

    private static void placeOrder(String shippingAddress, boolean isPaid) {
        String status = isPaid ? Constants.PAYMENT_RECEIVED : Constants.WAIT_FOR_PAYMENT;     
        Date today = new Date(System.currentTimeMillis());
        Time time = Time.valueOf(LocalTime.now());
        

        OrderDAO orderDAO = new OrderDAO();    
        orderDAO.insert(new Order(user.getUserName(),
                today,
                time,
                shippingAddress,
                status));
        Order order = orderDAO
                .queryByUserDateTime(user.getUserName(), today, time)
                .get(0);   
        
        Transaction.begin();
        OrderProductDAO orderProductDAO = new OrderProductDAO();
        ProductDAO productDAO = new ProductDAO();
        for(Map.Entry<Product, Integer> entry : Cart.productsInChart.entrySet()){
            orderProductDAO
                .insert(new OrderProduct(order.getOrderID(), entry.getKey().getProductID(), entry.getValue()));
            if(isPaid){
                Product product = productDAO.queryBy("productID",entry.getKey().getProductID())
                        .get(0);
                product.setAvailableForSale(product.getAvailableForSale() - entry.getValue());
                productDAO.update(product);
            }
        }
        Transaction.commit(); 
        Transaction.end();
    }

    private static boolean isChartAvailable() {
        ProductDAO productDAO = new ProductDAO();
        boolean flag = true;
        for(Map.Entry<Product, Integer> entry : Cart.productsInChart.entrySet()){
            Integer availableForSale = productDAO.queryBy("productID",entry.getKey().getProductID())
                    .get(0).getAvailableForSale();
            if(entry.getValue() > availableForSale){
                System.out.println("You want to buy "
                        + entry.getValue() + " " +entry.getKey().getProductName() 
                        + ", but we only have " + availableForSale + " avaiable.");
                if(flag){
                    flag = false;
                }
            }
        }
        return flag;
    }
}
