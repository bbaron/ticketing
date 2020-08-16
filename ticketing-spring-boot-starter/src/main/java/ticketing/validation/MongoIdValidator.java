package ticketing.validation;

import org.bson.types.ObjectId;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MongoIdValidator implements ConstraintValidator<MongoId, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        if (s == null) return true;
        return ObjectId.isValid(s);
    }
}
