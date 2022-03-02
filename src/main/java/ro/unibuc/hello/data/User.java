package ro.unibuc.hello.data;

import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@NoArgsConstructor
@ToString
@Document
public class User {
    @Id
    public String id;

    @Indexed(unique = true)
    public String name;

    public String passwordSalt;
    public String passwordHash;

    public User(String name, String password) throws NoSuchAlgorithmException {
        this.name = name;
        this.passwordSalt = generateSalt();
        this.passwordHash = hashPassword(password, this.passwordSalt);
    }

    public boolean isPasswordValid(String password) throws NoSuchAlgorithmException {
        var hash = hashPassword(password, this.passwordSalt);
        return this.passwordHash.equals(hash);
    }

    private String generateSalt() {
        var leftLimit = Character.getNumericValue('a');
        var rightLimit = Character.getNumericValue('z');
        var targetStringLength = 10;
        var random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        var saltedPassword = password + salt;
        var digest = MessageDigest.getInstance("SHA-256");
        var byteHash = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte aByte : byteHash) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString();
    }
}
