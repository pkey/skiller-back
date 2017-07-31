package lt.swedbank.exceptions.department;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class DepartmentNotFoundException extends MainException {

    public DepartmentNotFoundException() {
        this.messageCode = "department_not_found";
        this.statusCode = HttpStatus.NOT_FOUND;
    }
}