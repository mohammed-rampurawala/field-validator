package com.mr.androidvalidation.descriptor;

import android.databinding.ObservableField;

import com.mr.androidvalidation.factory.ComponentFactory;

import java.util.ArrayList;
import java.util.List;

public class DynamicFormDescriptor<T> {
    @ComponentFactory.FormControl
    public int controlType;
    public String labelText;
    private T content;
    public ObservableField<String> observableField;
    private ObservableField<String> confirmationObservableField;
    public List<ValidatorDescriptor> validatorTypes;

    private int inputType;
    private String hint;

    public T getContent() {
        return content;
    }

    public static <T extends Object> DynamicFormDescriptor<T> getDescriptor(final int type, final String label, final ObservableField<String> observable) {
        if (observable.get() == null) {
            observable.set("");
        }
        return new DynamicFormDescriptor<T>() {{
            controlType = type;
            labelText = label;
            observableField = observable;
        }};
    }

    public DynamicFormDescriptor<T> setConfirmationField(ObservableField<String> confirmationField) {
        confirmationField.set("");
        this.confirmationObservableField = confirmationField;
        return this;
    }

    public ObservableField<String> getConfirmationObservableField() {
        return confirmationObservableField;
    }

    public DynamicFormDescriptor<T> setContent(T content) {
        this.content = content;
        return this;
    }

    public DynamicFormDescriptor<T> setSelectedIndex(int index) {
        int selectedIndex = index;
        return this;
    }

    public DynamicFormDescriptor<T> addValidatorType(ValidatorDescriptor validatorDescriptor) {
        if (validatorTypes == null) {
            validatorTypes = new ArrayList<>();
        }
        validatorTypes.add(validatorDescriptor);
        return this;
    }

    public DynamicFormDescriptor<T> addValidatorTypes(ValidatorDescriptor... validators) {
        for (ValidatorDescriptor val:validators) {
            addValidatorType(val);
        }
        return this;
    }

    public DynamicFormDescriptor<T> setInputType(int inputType) {
        this.inputType = inputType;
        return this;
    }

    public int getInputType() {
        return inputType;
    }

    public String getHint() {
        return hint;
    }

    public DynamicFormDescriptor<T> setHint(String hint) {
        this.hint = hint;
        return this;
    }
}
