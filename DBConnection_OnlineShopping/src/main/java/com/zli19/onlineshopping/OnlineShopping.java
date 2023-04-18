
package com.zli19.onlineshopping;

import com.zli19.onlineshopping.service.Service;
import com.zli19.onlineshopping.util.Util;

/**
 *
 * @author zhiku
 */
public class OnlineShopping {

    public static void main(String[] args) {
        
        while(true){
            String chosen = Util.input("""
                            Hi,welcome! What can I do for you today?
                            1.Browse or Search
                            2.Sign In
                            3.Create an account
                             >""").strip();
            switch (chosen){
                case "3":
                    if(!Service.register()){
                        break;
                    }
                case "2":
                    if(!Service.login()){
                       break; 
                    }

                    boolean flag = true;
                    while(flag){
                        System.out.println("What do you want to do next?");
                        String answer = Util.input("""
                                                   1.Browse or Search
                                                   2.Check your orders
                                                   3.Sign Out
                                                   4.Chart
                                                   >""")
                                .strip();                       
                        switch (answer) {
                            case "1" -> Service.showProduct();
                            case "2" -> Service.checkOrder();
                            case "3" -> {
                                Service.logout();
                                flag = false;
                            }
                            case "4" -> Service.showChart();
                        }
                    }
                    break;
                case "1":
                    Service.showProduct();
                    break;
                default:
                    break;
            }             
        }
    }
}
