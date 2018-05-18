package com.mr.androidvalidation.descriptor;

public class ValidatorDescriptor {
    private @RxDynamicFormValidator.DynamicValidators int validatorType;
    private String message;
    private Object[] args;

    public ValidatorDescriptor(@RxDynamicFormValidator.DynamicValidators int validatorType, String message, Object... args) {
        this.setMessage(message);
        this.setValidatorType(validatorType);
        this.args = args;
    }

    @RxDynamicFormValidator.DynamicValidators
    public int getValidatorType() {
        return validatorType;
    }

    public ValidatorDescriptor setValidatorType(int validatorType) {
        this.validatorType = validatorType;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ValidatorDescriptor setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object[] getArgs() {
        return args;
    }
}