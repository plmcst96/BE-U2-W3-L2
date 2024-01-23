package cristinapalmisani.BEU2W3L1.controller;

import cristinapalmisani.BEU2W3L1.entities.User;
import cristinapalmisani.BEU2W3L1.exception.BadRequestException;
import cristinapalmisani.BEU2W3L1.payloads.user.UserLoginDTO;
import cristinapalmisani.BEU2W3L1.payloads.user.UserLoginResponseDTO;
import cristinapalmisani.BEU2W3L1.payloads.user.UserRequestDTO;
import cristinapalmisani.BEU2W3L1.payloads.user.UserResponseDTO;
import cristinapalmisani.BEU2W3L1.services.AuthService;
import cristinapalmisani.BEU2W3L1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;
    @Autowired
    UserService usersService;

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginDTO body) {
        String accessToken = authService.authenticateUser(body);
        return new UserLoginResponseDTO(accessToken);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@RequestBody @Validated UserRequestDTO newUserPayload, BindingResult validation) {
        // Per completare la validazione devo in qualche maniera fare un controllo del tipo: se ci sono errori -> manda risposta con 400 Bad Request
        System.out.println(validation);
        if (validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException("Ci sono errori nel payload!"); // L'eccezione arriverà agli error handlers tra i quali c'è quello che manderà la risposta con status code 400
        } else {
            User newUser = usersService.save(newUserPayload);

            return new UserResponseDTO(newUser.getId());
        }
    }
}
