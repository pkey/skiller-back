package lt.swedbank.exceptions.valueStream;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class ValueStreamNotFoundException extends MainException {

    public ValueStreamNotFoundException () {
        this.messageCode = "value_stream_not_found";
        this.statusCode = HttpStatus.NOT_FOUND;
    }
}
