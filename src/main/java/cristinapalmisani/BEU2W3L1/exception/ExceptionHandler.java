package cristinapalmisani.BEU2W3L1.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorPayload handleNotFound(NotFoundException e) {
        return new ErrorPayload(e.getMessage(), new Date());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    public ErrorPayload handleUnauthorized(UnauthorizeException e) {
        return new ErrorPayload(e.getMessage(), new Date());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorPayloadWhithListDTO handleBadRequest(BadRequestException e) {

        List<String> errorsMessages = new ArrayList<>();
        if (e.getErrorlist() != null)
            errorsMessages = e.getErrorlist().stream().map(err -> err.getDefaultMessage()).toList();
        return new ErrorPayloadWhithListDTO(e.getMessage(), new Date(), errorsMessages);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorPayload handleIllegalArgument(IllegalArgumentException e) {
        return new ErrorPayload("Argument not Valid!", new Date());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorPayload handleGeneric(Exception e) {
        log.error("Server Error: NERV!", e);
        return new ErrorPayload("Server Error: NERV!", new Date());
    }
}
