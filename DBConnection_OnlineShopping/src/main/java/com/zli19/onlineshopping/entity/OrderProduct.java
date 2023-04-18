
package com.zli19.onlineshopping.entity;

/**
 *
 * @author zhiku
 */
public class OrderProduct {
    private Integer orderID;
    private String productID;
    private Integer amountSold;

    public OrderProduct() {
    }

    public OrderProduct(Integer orderID, String productID, Integer amountSold) {
        this.orderID = orderID;
        this.productID = productID;
        this.amountSold = amountSold;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public String getProductID() {
        return productID;
    }

    public Integer getAmountSold() {
        return amountSold;
    }

    public void setAmountSold(Integer amountSold) {
        this.amountSold = amountSold;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    
    @Override
    public String toString() {
        return "OrderProduct{" + "orderID=" + orderID + ", productID=" + productID + ", amountSold=" + amountSold + '}';
    }
    
    
}
