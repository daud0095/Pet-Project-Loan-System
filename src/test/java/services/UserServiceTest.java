package services;

import entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {


    @Test
    void validatePasswordToShort() {
        assertFalse(UserService.validatePassword("1233"));
    }

    @Test
    void validatePasswordToLong(){
        assertEquals(false, UserService.validatePassword("1234567891234567"));

    }



}