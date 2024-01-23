package cristinapalmisani.BEU2W3L1.controller;

import cristinapalmisani.BEU2W3L1.entities.User;
import cristinapalmisani.BEU2W3L1.exception.BadRequestException;
import cristinapalmisani.BEU2W3L1.exception.NotFoundException;
import cristinapalmisani.BEU2W3L1.payloads.user.UserRequestDTO;
import cristinapalmisani.BEU2W3L1.payloads.user.UserResponseDTO;
import cristinapalmisani.BEU2W3L1.services.AuthService;
import cristinapalmisani.BEU2W3L1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sort) {
        return userService.getUsers(page, size, sort);
    }

    // /me endpoints
    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentUser) {
        // @AuthenticationPrincipal permette di accedere ai dati dell'utente attualmente autenticato
        // (perchè avevamo estratto l'id dal token e cercato l'utente nel db)
        return currentUser;
    }


    @PutMapping("/me")
    public User getMeAndUpdate(@AuthenticationPrincipal User currentUser, @RequestBody UserRequestDTO body) {
        return userService.findByIdAndUpdate(currentUser.getId(), body);
    }

    @DeleteMapping("/me")
    public void getMeAnDelete(@AuthenticationPrincipal User currentUser) {
        userService.findByIdAndDelete(currentUser.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO postUsers(@RequestBody @Validated UserRequestDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            User user = authService.save(body);
            return new UserResponseDTO(user.getId());
        }
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        try {
            return userService.findById(id);
        } catch (Exception e) {
            throw new NotFoundException(id);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated UserRequestDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return userService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void findByIdAndDelete(@PathVariable UUID id) {
        try {
            userService.findByIdAndDelete(id);
        } catch (Exception e) {
            throw new NotFoundException(id);
        }
    }

    @PostMapping("/{id}/avatar")
    public String uploadExample(@PathVariable UUID id, @RequestParam("avatar") MultipartFile body) throws IOException {
        return userService.uploadPicture(id, body);
    }
}
