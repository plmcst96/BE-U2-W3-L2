package cristinapalmisani.BEU2W3L1.controller;

import cristinapalmisani.BEU2W3L1.entities.User;
import cristinapalmisani.BEU2W3L1.exception.BadRequestException;
import cristinapalmisani.BEU2W3L1.exception.NotFoundException;
import cristinapalmisani.BEU2W3L1.payloads.user.UserRequestDTO;
import cristinapalmisani.BEU2W3L1.payloads.user.UserResponseDTO;
import cristinapalmisani.BEU2W3L1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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

    @GetMapping
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sort) {
        return userService.getUsers(page, size, sort);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO postUsers(@RequestBody @Validated UserRequestDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        } else {
            User user = userService.save(body);
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
    public User findByIdAndUpdate(@PathVariable UUID id, @RequestBody @Validated UserRequestDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return userService.findByIdAndUpdate(id, body);
    }

    @DeleteMapping("/{id}")
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
