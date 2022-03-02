package ro.unibuc.hello.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ro.unibuc.hello.data.User;

import java.security.NoSuchAlgorithmException;

public class UserTests {

    @Test
    public void testCheckPassword() throws NoSuchAlgorithmException {
        var user = new User("test", "test");
        Assertions.assertTrue(user.isPasswordValid("test"));
        Assertions.assertFalse(user.isPasswordValid("something else"));
    }
}
