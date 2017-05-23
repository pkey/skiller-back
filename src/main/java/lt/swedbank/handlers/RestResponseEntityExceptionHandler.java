package lt.swedbank.handlers;

import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import lt.swedbank.beans.response.AuthenticationError;
import lt.swedbank.beans.response.AuthenticationErrorsWrapper;
import lt.swedbank.beans.response.ErrorResponse;
import lt.swedbank.exceptions.MainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

    @Autowired
    private MessageSource messageSource;


    @ExceptionHandler({ Auth0Exception.class })
    public ResponseEntity<Object> handleAuth0Exception(final APIException ex, final WebRequest request) {
        ErrorResponse er = new ErrorResponse(ex.getMessage());
        return handleExceptionInternal(ex, er, new HttpHeaders(), HttpStatus.valueOf(ex.getStatusCode()), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ObjectError> fieldSErrors = ex.getBindingResult().getAllErrors();
        AuthenticationErrorsWrapper fieldErrorList = new AuthenticationErrorsWrapper();

        for (Iterator iterator = fieldSErrors.iterator(); iterator.hasNext(); ) {
            FieldError fieldError = (FieldError) iterator.next();
            fieldErrorList.addError(new AuthenticationError(fieldError.getField(), fieldError.getDefaultMessage()));
        }

        return new ResponseEntity<Object>(fieldErrorList, HttpStatus.BAD_REQUEST);
        }

    @ExceptionHandler({MainException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleMainException(MainException ex) {
        return new ErrorResponse(messageSource.getMessage(ex.getMessageCode(), null, Locale.getDefault()));
    }
}
