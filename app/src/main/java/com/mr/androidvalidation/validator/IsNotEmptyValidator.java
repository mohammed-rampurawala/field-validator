package com.mr.androidvalidation.validator;

import com.mr.androidvalidation.descriptor.DynamicFormValidationResponse;
import com.mr.androidvalidation.dynamic_form.component.BaseComponent;
import com.mr.androidvalidation.dynamic_form.component.TextInputComponent;

import rx.Observable;

public class IsNotEmptyValidator extends BaseValidator {

    public IsNotEmptyValidator(Object[] args) {
        super(args);
    }

    @Override
    public Observable<DynamicFormValidationResponse<BaseComponent>> validate(BaseComponent item) {
        //we can have different validation logic for different components
        if (item instanceof TextInputComponent && ((TextInputComponent) item).getText().length() == 0) {
            return Observable.just(DynamicFormValidationResponse.createInvalid(item, message));
        }
        return super.validate(item);
    }

}