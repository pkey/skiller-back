package lt.swedbank.handlers;

import lt.swedbank.beans.response.ErrorResponse;
import lt.swedbank.exceptions.MainException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

@RunWith(SpringJUnit4ClassRunner.class)
public class RestResponseEntityExceptionHandlerTest {

    @InjectMocks
    private RestResponseEntityExceptionHandler restResponseEntityExceptionHandler;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void handleMainException() throws Exception {
        MainException ex = new MainException();
        ex.setStatusCode(HttpStatus.BAD_REQUEST);
        ex.setMessageCode("message_code");

        Mockito.when(messageSource.getMessage(any(), any(), any())).thenReturn("message_code");

        ErrorResponse er = new ErrorResponse(ex.getMessageCode());
        ResponseEntity<ErrorResponse> testResponse = new ResponseEntity<ErrorResponse>(er, ex.getStatusCode());

        ResponseEntity<ErrorResponse> resultResponse = restResponseEntityExceptionHandler.handleMainException(ex);

        assertEquals(testResponse.getBody().getMessage(), resultResponse.getBody().getMessage());
    }

}