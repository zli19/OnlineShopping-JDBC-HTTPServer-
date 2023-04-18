
package com.zli19.onlineshopping.entity;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author zhiku
 */
public class Order {
    private Integer orderID;
    private String userName;
    private Date orderDate;
    private Time orderTime;
    private String shippingAddress;
    private String status;

    public Order() {
    } 

    public Order(String userName, Date orderDate, Time time, String shippingAddress, String status) {
        this(null, userName, orderDate, time, shippingAddress, status);
    }
    
    public Order(Integer orderID, String userName, Date orderDate, Time time, String shippingAddress, String status) {
        this.orderID = orderID;
        this.userName = userName;
        this.orderDate = orderDate;
        this.orderTime = time;
        this.shippingAddress = shippingAddress;
        this.status = status;
    }

    public Integer getOrderID() {
        return orderID;
    }

    public String getUserName() {
        return userName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public Time getTime() {
        return orderTime;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setTime(Time time) {
        this.orderTime = time;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" + "orderID=" + orderID + ", userName=" + userName + ", orderDate=" + orderDate + ", time=" + orderTime + ", shippingAddress=" + shippingAddress + ", status=" + status + '}';
    }
}
