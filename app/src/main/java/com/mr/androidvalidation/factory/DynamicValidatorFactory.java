package com.mr.androidvalidation.factory;

import com.mr.androidvalidation.descriptor.RxDynamicFormValidator;
import com.mr.androidvalidation.descriptor.ValidatorDescriptor;
import com.mr.androidvalidation.dynamic_form.component.BaseComponent;
import com.mr.androidvalidation.validator.IsNotEmptyValidator;
import com.mr.androidvalidation.validator.MinCharLengthValidator;
import com.mr.androidvalidation.validator.PhoneNumberValidator;

public class DynamicValidatorFactory {
    public static DynamicFormValidator<BaseComponent> getValidatorByDescriptor(ValidatorDescriptor descriptor) {
        return getValidatorByType(descriptor.getValidatorType(), descriptor.getArgs()).setMessage(descriptor.getMessage());
    }

    private static DynamicFormValidator<BaseComponent> getValidatorByType(@RxDynamicFormValidator.DynamicValidators int validatorType, Object[] args) {
        switch (validatorType) {
            case RxDynamicFormValidator.IS_NOT_EMPTY:
                return new IsNotEmptyValidator(args);
            case RxDynamicFormValidator.PHONE_NUMBER:
                return new PhoneNumberValidator(args);
            case RxDynamicFormValidator.MIN_CHAR:
                return new MinCharLengthValidator(args);
            default:
                return null;
        }
    }
}
