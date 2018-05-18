package com.mr.androidvalidation.validator;

import com.mr.androidvalidation.descriptor.DynamicFormValidationResponse;
import com.mr.androidvalidation.dynamic_form.component.BaseComponent;
import com.mr.androidvalidation.dynamic_form.component.TextInputComponent;

import java.util.Locale;

import rx.Observable;

public class MinCharLengthValidator extends BaseValidator {
    public MinCharLengthValidator(Object[] args) {
        super(args);
    }

    @Override
    public Observable<DynamicFormValidationResponse<BaseComponent>> validate(BaseComponent item) {
        Integer min = 1;
        if (hasArgs() && getArgs()[0] instanceof Integer) {
            min = (Integer) getArgs()[0];
        }
        if (item instanceof TextInputComponent) {
            String content = ((TextInputComponent) item).getText().toString();
            if (content.length() < min) {
                message = String.format(Locale.getDefault(), message, min);
                return Observable.just(DynamicFormValidationResponse.createInvalid(item, message));
            }
        }
        return super.validate(item);
    }
}