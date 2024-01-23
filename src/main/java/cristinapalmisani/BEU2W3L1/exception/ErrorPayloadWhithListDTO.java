package cristinapalmisani.BEU2W3L1.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ErrorPayloadWhithListDTO{
    String message;
    Date timestamp;
    List<String> errorlist;

    public ErrorPayloadWhithListDTO(String message, Date date, List<String> errorsMessages) {
    }
}
