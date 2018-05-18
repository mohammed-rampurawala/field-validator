package com.mr.androidvalidation.validator;

import com.mr.androidvalidation.descriptor.DynamicFormValidationResponse;
import com.mr.androidvalidation.dynamic_form.component.BaseComponent;
import com.mr.androidvalidation.factory.DynamicFormValidator;

import rx.Observable;

public class BaseValidator implements DynamicFormValidator<BaseComponent> {
    String message = "There is a problem with this field";

    public Object[] getArgs() {
        return args;
    }

    public BaseValidator(Object[] args) {
        this.args = args;
    }

    @Override
    public Observable<DynamicFormValidationResponse<BaseComponent>> validate(BaseComponent item) {
        return Observable.just(DynamicFormValidationResponse.createSuccess(item));
    }

    @Override
    public DynamicFormValidator<BaseComponent> setMessage(String message) {
        this.message = message;
        return this;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasArgs() {
        return args != null && args.length > 0;
    }

    private Object[] args;
}