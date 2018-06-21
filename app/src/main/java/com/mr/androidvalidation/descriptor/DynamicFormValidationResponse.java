package com.mr.androidvalidation.descriptor;

import android.support.annotation.Nullable;

import com.mr.androidvalidation.dynamic_form.component.BaseComponent;

public class DynamicFormValidationResponse<T extends BaseComponent> {
    private BaseComponent component;
    private boolean isValid;
    private String message;

    private DynamicFormValidationResponse(BaseComponent component, boolean isValid, @Nullable String message) {
        this.component = component;
        this.isValid = isValid;
        this.message = message;
    }

    public static DynamicFormValidationResponse<BaseComponent> createInvalid(BaseComponent component, String message) {
        return new DynamicFormValidationResponse<>(component, false, message);
    }

    public static DynamicFormValidationResponse<BaseComponent> createSuccess(BaseComponent component) {
        return new DynamicFormValidationResponse<>(component, true, "");
    }

    public BaseComponent getComponent() {
        return component;
    }

    public boolean getIsValid() {
        return isValid;
    }

    public DynamicFormValidationResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getMessage() {
        return (message == null) ? "" : message;
    }

    @Override
    public String toString() {
        return "Matchbox Validation Result {" + component.getClass().getName() + ", isValid:" + isValid + "}";
    }
}