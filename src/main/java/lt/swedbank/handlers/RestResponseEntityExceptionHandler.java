package lt.swedbank.handlers;

import com.auth0.exception.APIException;
import com.auth0.exception.Auth0Exception;
import lt.swedbank.beans.response.AuthenticationError;
import lt.swedbank.beans.response.AuthenticationErrorsWrapper;
import lt.swedbank.beans.response.ErrorResponse;
import lt.swedbank.exceptions.skill.SkillAlreadyExistsException;
import lt.swedbank.exceptions.skill.SkillNotFoundException;
import lt.swedbank.exceptions.user.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Iterator;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler({ Auth0Exception.class })
    public ResponseEntity<Object> handleBadRequest(final APIException ex, final WebRequest request) {
        final String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.valueOf(ex.getStatusCode()), request);
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

    @ExceptionHandler({ UserNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleUserNotFoundExeption(final UserNotFoundException ex, final WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ SkillAlreadyExistsException.class })
    public ResponseEntity<ErrorResponse> handleSkillAlreadyExistsException(SkillAlreadyExistsException ex, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({SkillNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleSkillNotFoundException(SkillNotFoundException ex, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex), HttpStatus.NOT_FOUND);
    }
}
