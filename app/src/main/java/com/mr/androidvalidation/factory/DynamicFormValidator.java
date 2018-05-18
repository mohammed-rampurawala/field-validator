package com.mr.androidvalidation.factory;

import com.mr.androidvalidation.descriptor.DynamicFormValidationResponse;
import com.mr.androidvalidation.dynamic_form.component.BaseComponent;

import rx.Observable;

public interface DynamicFormValidator<T extends BaseComponent> {
    Observable<DynamicFormValidationResponse<BaseComponent>> validate(T item);

    DynamicFormValidator<T> setMessage(String message);

    void setArgs(Object[] args);
}
