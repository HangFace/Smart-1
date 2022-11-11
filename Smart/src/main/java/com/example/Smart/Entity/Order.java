package com.example.Smart.Entity;

import javax.persistence.*;

@Entity
@Table(name = "myOrder")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long myOrder_id;
    private String orderId;
    private String amount;
    private String receipt;
    private String status;
    @ManyToOne
    private User user;
    private String payment_id;

    public long getMyOrder_id() {
        return myOrder_id;
    }

    public void setMyOrder_id(long myOrder_id) {
        this.myOrder_id = myOrder_id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String order_id) {
        this.orderId = order_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }
}
