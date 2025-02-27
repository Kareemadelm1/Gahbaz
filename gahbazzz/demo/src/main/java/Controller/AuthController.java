package Controller;

import dto.JwtResponse;
import dto.LoginRequest;
import model.Student;
import model.Teacher;
import model.User;
import model.UserRole;
import repository.StudentRepository;
import repository.TeacherRepository;
import repository.UserRepository;
import security.jwt.JwtUtils;
import services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser( @RequestBody LoginRequest loginRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Get user details
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Find user for additional info
        Optional<org.apache.catalina.User> userOpt = userRepository.findByUsername(userDetails.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User user = (User) userOpt.get();
        Object userData = null;

        // Get role-specific data
        if (user.getRole() == UserRole.STUDENT) {
            Optional<Student> student = studentRepository.findByUser((org.apache.catalina.User) user);
            userData = student.orElse(null);
        } else if (user.getRole() == UserRole.TEACHER) {
            Optional<Teacher> teacher = teacherRepository.findByUser(user);
            userData = teacher.orElse(null);
        }

        // Return response with JWT and user data
        return ResponseEntity.ok(
                new JwtResponse(
                        jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        userDetails.getFullName(),
                        userDetails.getRole(),
                        userData
                )
        );
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken() {
        // This endpoint will be accessed with JWT token
        // If JWT is valid, it returns 200 OK, if not, the JwtAuthTokenFilter will return 401
        return ResponseEntity.ok().build();
    }
}