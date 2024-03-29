package cristinapalmisani.BEU2W3L1.services;

import cristinapalmisani.BEU2W3L1.config.EmailSender;
import cristinapalmisani.BEU2W3L1.entities.Role;
import cristinapalmisani.BEU2W3L1.entities.User;
import cristinapalmisani.BEU2W3L1.exception.BadRequestException;
import cristinapalmisani.BEU2W3L1.exception.UnauthorizeException;
import cristinapalmisani.BEU2W3L1.payloads.user.UserLoginDTO;
import cristinapalmisani.BEU2W3L1.payloads.user.UserRequestDTO;
import cristinapalmisani.BEU2W3L1.repositories.UserRepositoryDAO;
import cristinapalmisani.BEU2W3L1.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService usersService;

    @Autowired
    private UserRepositoryDAO userRepositoryDAO;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUser(UserLoginDTO body) {
        User user = usersService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizeException("Credenziali non valide!");
        }
    }

    public User save(UserRequestDTO body){
        userRepositoryDAO.findByUsername(body.username()).ifPresent(a -> {
            throw new BadRequestException("Username " + a.getUsername() + " already exists");
        });
        userRepositoryDAO.findByEmail(body.email()).ifPresent(a -> {
            throw new BadRequestException("User with email " + a.getEmail() + " already exists");
        });
        User user = new User();
        user.setUsername(body.username());
        user.setName(body.name());
        user.setSurname(body.surname());
        user.setEmail(body.email());
        user.setRole(Role.USER);
        user.setPassword(bcrypt.encode(body.password()));
        User saveUser = userRepositoryDAO.save(user);
        emailSender.sendRegistrationEmail(saveUser);
        return saveUser;
    }
}
