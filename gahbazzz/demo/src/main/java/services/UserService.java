package services;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        // Add password encoding (e.g., BCrypt) in practice <button class="citation-flag" data-index="5">
        return (user != null && user.getPassword().equals(password)) ? user : null;
    }
}
