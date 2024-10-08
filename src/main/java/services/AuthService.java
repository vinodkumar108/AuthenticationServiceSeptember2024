package services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import repositories.UserRepository;

import models.User;

import java.util.Optional;

public class AuthService {

    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean signUp(String email, String password) throws UserAlreadyExistsException {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password cannot be null");
        }

        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with email: " + email);
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashPassword(password)); // Ensure this method hashes the password
        userRepository.save(user);
        return true;
    }

    private boolean isValidEmail(String email) {
        // Implement email validation logic
        return true;
    }

    private String hashPassword(String password) {
        // Implement password hashing logic
        return password;
    }

    public boolean login(String email, String password) throws UsernameNotFoundException
    {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        boolean matches = password.equals(userOptional.get().getPassword());
        if (matches) {
            return true;
        }
        else {
            throw new WrongPasswordException("Wrong password.");
        }
        return false;
    }


}
