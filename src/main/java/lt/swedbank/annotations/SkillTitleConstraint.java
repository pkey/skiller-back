package lt.swedbank.annotations;

import lt.swedbank.validators.SkillTitleValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SkillTitleValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SkillTitleConstraint {
    String message() default "Invalid title";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
