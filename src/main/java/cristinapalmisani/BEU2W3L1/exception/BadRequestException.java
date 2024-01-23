package cristinapalmisani.BEU2W3L1.exception;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class BadRequestException extends RuntimeException{
    private List<ObjectError> errorlist;

    public BadRequestException(String message){
        super(message);
    }

    public BadRequestException(List<ObjectError> errors){
        super("Errors paylod");
        this.errorlist = errors;
    }
}
