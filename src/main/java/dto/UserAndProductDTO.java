package dto;

import entities.Product;
import entities.User;

import java.util.List;

public record UserAndProductDTO(User user, List<Product> products) {
}

