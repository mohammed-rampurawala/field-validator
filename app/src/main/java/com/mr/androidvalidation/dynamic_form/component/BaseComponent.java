package com.mr.androidvalidation.dynamic_form.component;


import com.mr.androidvalidation.descriptor.DynamicFormDescriptor;

import rx.Observable;

public interface BaseComponent {
    void init(DynamicFormDescriptor<?> descriptor);

    void selfValidate();

    Observable<Boolean> getIsValidObservable();

    void setIsLastChild(boolean lastChild);
}
