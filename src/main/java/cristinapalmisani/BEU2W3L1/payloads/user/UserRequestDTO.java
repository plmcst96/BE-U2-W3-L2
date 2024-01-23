package cristinapalmisani.BEU2W3L1.payloads.user;

import jakarta.validation.constraints.*;

public record UserRequestDTO(
        @NotEmpty(message = "This items cannot be empty")
        @Size(min = 3, max = 30, message = "Name must be between 3 or 30 chars")
        String name,
        @NotEmpty(message = "This items cannot be empty")
        @Size(min = 3, max = 30, message = "Surname must be between 3 or 30 chars")
        String surname,
        @NotEmpty(message = "This items cannot be empty")
        @Size(min = 3, max = 30, message = "Username must be between 3 or 30 chars")
        String username,
        @NotEmpty(message = "This items cannot be empty")
        @Email
        String email,
        @NotEmpty(message = "This items cannot be empty")
        @Size(min = 3, max = 40, message = "Password must be between 3 or 40 chars")
        String password) {
}
