
package com.zli19.onlineshopping.entity;

/**
 *
 * @author zhiku
 */
public class User {
    private String userName;
    private String emailAddress;
    private Integer phoneNumber;
    private String password;
    
    public User() {
    }

    public User(String userName, String emailAddress, Integer phoneNumber, String password) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPhoneNumber(Integer phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public Integer getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" + "userName=" + userName + ", emailAddress=" + emailAddress + ", phoneNumber=" + phoneNumber + ", password=" + password + '}';
    }   
    
}
