package lt.swedbank.handlers;

import lt.swedbank.beans.response.ErrorResponse;
import lt.swedbank.exceptions.MainException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

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

    @Test
    public void handleMethodArgumentNotValid() throws Exception {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        HttpHeaders headers = mock(HttpHeaders.class);
        HttpStatus status = HttpStatus.BAD_REQUEST;
        BindingResult bindingResult = mock(BindingResult.class);
        WebRequest request = mock(WebRequest.class);

        Mockito.when(bindingResult.getAllErrors())
                .thenReturn(Arrays.asList(
                        new FieldError("user", "name", "should not be empty"))
                );

        Mockito.when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> responseEntity = restResponseEntityExceptionHandler.handleMethodArgumentNotValid(ex, headers, status, request);

        Assert.assertNotEquals(responseEntity, null);
    }


}