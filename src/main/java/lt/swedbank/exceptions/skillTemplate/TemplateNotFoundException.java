package lt.swedbank.exceptions.skillTemplate;

import lt.swedbank.exceptions.MainException;
import org.springframework.http.HttpStatus;

public class TemplateNotFoundException extends MainException {

    public TemplateNotFoundException() {
        this.messageCode = "no_skill_template_found";
        this.statusCode = HttpStatus.NOT_FOUND;
    }
}
