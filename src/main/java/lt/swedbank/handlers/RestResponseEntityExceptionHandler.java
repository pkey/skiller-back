package lt.swedbank.handlers;

import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler({ Auth0Exception.class, APIException.class})
    public ResponseEntity<Object> handleBadRequest(final APIException ex, final WebRequest request) {
        final String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.valueOf(ex.getStatusCode()), request);
    }

}
