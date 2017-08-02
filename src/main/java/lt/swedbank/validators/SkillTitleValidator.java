package lt.swedbank.validators;

import lt.swedbank.annotations.SkillTitleConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SkillTitleValidator implements ConstraintValidator<SkillTitleConstraint, String> {
    @Override
    public void initialize(SkillTitleConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && !value.isEmpty() && Character.isUpperCase(value.charAt(0));
    }
}
