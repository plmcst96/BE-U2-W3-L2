package cristinapalmisani.BEU2W3L1.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ErrorPayload {
    private String message;
    private Date timestamp;
}
