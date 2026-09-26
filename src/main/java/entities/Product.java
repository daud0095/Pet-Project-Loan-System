package entities;

import java.time.LocalDate;

public class Product {

    // alle data er defineret i produkt klass i forhold til json
    // Konstruktør og getters setters method
    private int id;
    private String picturePath;
    private String name;
    private String description;
    private int stock ;
    private Status status;
    private LocalDate deliveryDate;
    private LocalDate loanDate;

    public Product(int id, String picturePath, String name, String description, int stock, Status status, LocalDate deliveryDate, LocalDate loanDate) {
        this.id = id;
        this.picturePath = picturePath;
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.status = status;
        this.deliveryDate = deliveryDate;
        this.loanDate = loanDate;

    }

    public LocalDate getLoanDate(){
        return loanDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", picturePath='" + picturePath + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", status=" + status +
                ", deliveryDate=" + deliveryDate +
                '}';
    }
}
