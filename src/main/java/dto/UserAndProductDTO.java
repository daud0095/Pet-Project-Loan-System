package dto;

import entities.Product;
import entities.User;

import java.time.LocalDate;
import java.util.List;

public class UserAndProductDTO {
    private String productName;
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

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public void setAfleveringsdato(LocalDate afleveringsdato) {
        this.afleveringsdato = afleveringsdato;
    }

    public void setReturn_date(LocalDate return_date) {
        this.return_date = return_date;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

