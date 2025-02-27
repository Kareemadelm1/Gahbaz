package dto;


import lombok.Data;
import model.UserRole;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private UserRole role;
    private Object userData;

    public JwtResponse(String token, Long id, String username, String email, String fullName, UserRole role, Object userData) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.userData = userData;
    }
}
