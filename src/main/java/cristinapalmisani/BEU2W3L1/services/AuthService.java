package cristinapalmisani.BEU2W3L1.services;

import cristinapalmisani.BEU2W3L1.entities.User;
import cristinapalmisani.BEU2W3L1.exception.UnauthorizeException;
import cristinapalmisani.BEU2W3L1.payloads.user.UserLoginDTO;
import cristinapalmisani.BEU2W3L1.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService usersService;

    @Autowired
    private JWTTools jwtTools;

    public String authenticateUser(UserLoginDTO body) {
        User user = usersService.findByEmail(body.email());
        if (body.password().equals(user.getPassword())) {
            return jwtTools.createToken(user);
        } else {
            throw new UnauthorizeException("Credenziali non valide!");
        }
    }
}
