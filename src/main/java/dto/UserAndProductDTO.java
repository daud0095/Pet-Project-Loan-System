package dto;

import entities.Product;
import entities.User;

import java.time.LocalDate;
import java.util.List;

public class UserAndProductDTO {
    private int productId;
    private String productName;
    private String picturePath;
    private String description;
    private int stock;
    private LocalDate loanDate;
    private LocalDate afleveringsdato;
    private LocalDate return_date;
    private String status;


    public UserAndProductDTO(String productName, LocalDate loanDate, LocalDate afleveringsdato, LocalDate return_date, String status) {
        this.productName = productName;
        this.loanDate = loanDate;
        this.afleveringsdato = afleveringsdato;
        this.return_date = return_date;
        this.status = status;
    }

    public UserAndProductDTO(int productId, String productName, String picturePath, String description, int stock, String status, LocalDate loanDate, LocalDate afleveringsdato) {
        this.productId = productId;
        this.productName = productName;
        this.picturePath = picturePath;
        this.description = description;
        this.stock = stock;
        this.status = status;
        this.loanDate = loanDate;
        this.afleveringsdato = afleveringsdato;
    }

    public String getProductName() {
        return productName;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getAfleveringsdato() {
        return afleveringsdato;
    }

    public LocalDate getReturn_date() {
        return return_date;
    }

    public String getStatus() {
        return status;
    }

    public int getProductId() {
        return productId;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public String getDescription() {
        return description;
    }

    public int getStock() {
        return stock;
    }
}

