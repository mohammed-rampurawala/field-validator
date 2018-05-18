package com.mr.androidvalidation.validator;

import com.mr.androidvalidation.descriptor.DynamicFormValidationResponse;
import com.mr.androidvalidation.dynamic_form.component.BaseComponent;
import com.mr.androidvalidation.dynamic_form.component.TextInputComponent;

import rx.Observable;

public class PhoneNumberValidator extends BaseValidator {
    private static final String TAG = PhoneNumberValidator.class.getName();
    private static final String PHONE_NUMBER_REGEX = "^1\\d{10}|\\d{10}$";

    public PhoneNumberValidator(Object[] args) {
        super(args);
    }

    @Override
    public Observable<DynamicFormValidationResponse<BaseComponent>> validate(BaseComponent item) {
        if (item instanceof TextInputComponent) {
            String value = ((TextInputComponent) item).getText().toString();
            if (value.isEmpty() || value.matches(PHONE_NUMBER_REGEX)) {
                return Observable.just(DynamicFormValidationResponse.createSuccess(item));
            }
        }
        return Observable.just(DynamicFormValidationResponse.createInvalid(item, message));
    }

}