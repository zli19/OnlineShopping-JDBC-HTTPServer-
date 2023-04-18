
package com.zli19.onlineshopping.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;


/**
 *
 * @author zhiku
 */
public class Product implements Serializable{
    private String productID;
    private String productName;
    private BigDecimal pricePerUnit;
    private Integer availableForSale;

    public Product() {
    }

    public Product(String productID, String productName, BigDecimal pricePerUnit, Integer availableForSale) {
        this.productID = productID;
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.availableForSale = availableForSale;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public Integer getAvailableForSale() {
        return availableForSale;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public void setAvailableForSale(Integer availableForSale) {
        this.availableForSale = availableForSale;
    }

    @Override
    public String toString() {
        return "Product{" + "productID=" + productID + ", productName=" + productName + ", pricePerUnit=" + pricePerUnit + ", availableForSale=" + availableForSale + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.productID);
        hash = 23 * hash + Objects.hashCode(this.productName);
        hash = 23 * hash + Objects.hashCode(this.pricePerUnit);
        hash = 23 * hash + Objects.hashCode(this.availableForSale);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (!Objects.equals(this.productID, other.productID)) {
            return false;
        }
        if (!Objects.equals(this.productName, other.productName)) {
            return false;
        }
        if (!Objects.equals(this.pricePerUnit, other.pricePerUnit)) {
            return false;
        }
        return Objects.equals(this.availableForSale, other.availableForSale);
    }
    
    
}
