package com.mr.androidvalidation.descriptor;

import android.support.annotation.IntDef;

import com.mr.androidvalidation.dynamic_form.component.BaseComponent;
import com.mr.androidvalidation.factory.DynamicFormValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

public class RxDynamicFormValidator {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({IS_NOT_EMPTY, MATCHES_CONFIRMATION, PHONE_NUMBER, ZIP_CODE, ACCOUNT_NUMBER, ROUTING_NUMBER, MIN_CHAR})
    public @interface DynamicValidators {
    }

    public static final int IS_NOT_EMPTY = 0x01;
    public static final int MATCHES_CONFIRMATION = 0x02;
    public static final int PHONE_NUMBER = 0x03;
    public static final int ZIP_CODE = 0x04;
    public static final int ACCOUNT_NUMBER = 0x05;
    public static final int ROUTING_NUMBER = 0x06;
    public static final int MIN_CHAR = 0x07;

    private List<DynamicFormValidator<BaseComponent>> validators = new ArrayList<>();
    //to do allow external validators
    private BaseComponent component;
    private Observable<BaseComponent> changeEmitter;

    private RxDynamicFormValidator(BaseComponent component) {
        this.component = component;
    }

    public static RxDynamicFormValidator createFor(BaseComponent component) {
        return new RxDynamicFormValidator(component);
    }

    public RxDynamicFormValidator onSubscribe() {
        this.changeEmitter = Observable.create(new Observable.OnSubscribe<BaseComponent>() {
            @Override
            public void call(Subscriber<? super BaseComponent> subscriber) {
                subscriber.onNext(component);
                subscriber.onCompleted();
            }
        });
        return this;
    }

    public static Observable<DynamicFormValidationResponse<? extends BaseComponent>> getFirstErrorResponse(Observable<List<DynamicFormValidationResponse<? extends BaseComponent>>> responses) {
        return responses
                .flatMap(new Func1<List<DynamicFormValidationResponse<? extends BaseComponent>>, Observable<DynamicFormValidationResponse<? extends BaseComponent>>>() {
                    @Override
                    public Observable<DynamicFormValidationResponse<? extends BaseComponent>> call(List<DynamicFormValidationResponse<? extends BaseComponent>> dynamicFormValidationResponses) {
                        return Observable.from(dynamicFormValidationResponses);
                    }
                })
                .filter(new Func1<DynamicFormValidationResponse<? extends BaseComponent>, Boolean>() {
                    @Override
                    public Boolean call(DynamicFormValidationResponse<? extends BaseComponent> dynamicFormValidationResponse) {
                        return !dynamicFormValidationResponse.getIsValid();
                    }
                }).first();
    }


    public RxDynamicFormValidator addValidator(DynamicFormValidator<BaseComponent> validator) {
        this.validators.add(validator);
        return this;
    }

    public List<DynamicFormValidator<BaseComponent>> getValidators() {
        return validators;
    }

    public Observable<List<DynamicFormValidationResponse<? extends BaseComponent>>> toObservable() {
        if (changeEmitter == null) {
            //throw error
        }
        return changeEmitter.concatMap(new Func1<BaseComponent, Observable<? extends DynamicFormValidationResponse<? extends BaseComponent>>>() {
            @Override
            public Observable<? extends DynamicFormValidationResponse<? extends BaseComponent>> call(final BaseComponent component) {
                return Observable.from(validators)
                        .concatMap(new Func1<DynamicFormValidator<BaseComponent>, Observable<? extends DynamicFormValidationResponse<? extends BaseComponent>>>() {
                            @Override
                            public Observable<? extends DynamicFormValidationResponse<? extends BaseComponent>> call(DynamicFormValidator<BaseComponent> validator) {
                                return validator.validate(component);
                            }
                        });
            }
        }).buffer(validators.size());
    }
}