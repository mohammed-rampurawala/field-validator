package com.mr.androidvalidation.dynamic_form.component;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.inputmethod.EditorInfo;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.mr.androidvalidation.descriptor.DynamicFormDescriptor;
import com.mr.androidvalidation.descriptor.DynamicFormValidationResponse;
import com.mr.androidvalidation.descriptor.RxDynamicFormValidator;
import com.mr.androidvalidation.descriptor.ValidatorDescriptor;
import com.mr.androidvalidation.factory.DynamicFormValidator;
import com.mr.androidvalidation.factory.DynamicValidatorFactory;

import java.util.List;
import java.util.NoSuchElementException;

import rx.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class TextInputComponent extends AppCompatEditText implements BaseComponent {

    private RxDynamicFormValidator validator;

    private ObservableField<String> observableField;

    private Emitter<Boolean> isValidBooleanEmitter;

    private boolean isLastChild;

    private String currentErrorMessage="";

    private boolean hasBeenInteractedWith;

    public TextInputComponent(Context context) {
        super(context);
        setup(context);
    }

    private void setup(Context context) {
        validator = RxDynamicFormValidator.createFor(this).onSubscribe();
    }

    public TextInputComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }

    public TextInputComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }

    @Override
    public void init(DynamicFormDescriptor<?> descriptor) {
        if (descriptor.validatorTypes != null) {
            addValidatorsFromDescriptors(descriptor.validatorTypes);
        }
        setSingleLine(true);
        setImeOptions(EditorInfo.IME_ACTION_NEXT);
        setInputType(descriptor.getInputType());
        setHint(descriptor.getHint());
        observableField = (ObservableField<String>) descriptor.observableField;
        setText(observableField.get());

        //when the EditText loses focus run the validators
        RxView.focusChanges(this)
                .skip(1)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("error", e.toString());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            selfValidate();
                            setHasBeenInteractedWith(true);
                        } else {
                            setNextFocusDownId(getNextFocusDownId());
                            setNextFocusForwardId(getNextFocusDownId());
                        }
                    }
                });

        //When a change happens run the validators
        RxTextView.afterTextChangeEvents(this)
                .skip(1)
                .subscribe(new Subscriber<TextViewAfterTextChangeEvent>() {
                    @Override
                    public void onCompleted() {
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("error", e.toString());
                    }

                    @Override
                    public void onNext(TextViewAfterTextChangeEvent textViewAfterTextChangeEvent) {
                        observableField.set(textViewAfterTextChangeEvent.editable().toString());
                        setHasBeenInteractedWith(true);
                        selfValidate();
                    }
                });

        final ObservableField<?> compareField = descriptor.getConfirmationObservableField();

        if (compareField != null) {
            compareField.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable observable, int i) {
                    selfValidate();
                }
            });
        }
    }

    private void addValidatorsFromDescriptors(List<ValidatorDescriptor> validatorTypes) {
        if (validatorTypes != null) {
            for (ValidatorDescriptor type : validatorTypes)
                addValidator(DynamicValidatorFactory.getValidatorByDescriptor(type));
        }
    }

    public void addValidator(DynamicFormValidator<BaseComponent> v) {
        validator.addValidator(v);
    }

    @Override
    public void selfValidate() {
        if (hasValidators()) {
            RxDynamicFormValidator.getFirstErrorResponse(getValidationObservable())
                    .subscribe(new Subscriber<DynamicFormValidationResponse<? extends BaseComponent>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof NoSuchElementException) {
                                setError(null);
                                setValid(true);
                            }
                        }

                        @Override
                        public void onNext(DynamicFormValidationResponse<? extends BaseComponent> dynamicFormValidationResponse) {
                            String message = dynamicFormValidationResponse.getMessage();
                            if (!message.equals(getError()) && hasBeenInteractedWith) {
                                setError(message);
                            }
                            setValid(false);
                        }
                    });
        }
    }

    @Override
    public Observable<Boolean> getIsValidObservable() {
        return Observable.fromEmitter(new Action1<Emitter<Boolean>>() {
            @Override
            public void call(Emitter<Boolean> booleanEmitter) {
                isValidBooleanEmitter = booleanEmitter;
                if (hasValidators()) {
                    selfValidate();
                } else {
                    booleanEmitter.onNext(true);
                }

            }
        }, Emitter.BackpressureMode.NONE);
    }

    @Override
    public void setIsLastChild(boolean lastChild) {
        isLastChild = lastChild;
    }

    private void setValid(boolean valid) {
        if (isValidBooleanEmitter != null) {
            isValidBooleanEmitter.onNext(valid);
        }
    }


    public void setHasBeenInteractedWith(boolean value) {
        this.hasBeenInteractedWith = value;
    }


    public void setIsValidBooleanEmitter(Emitter<Boolean> isValidBooleanEmitter) {
        this.isValidBooleanEmitter = isValidBooleanEmitter;
    }

    public boolean hasValidators() {
        return validator.getValidators().size() > 0;
    }

    public Observable<List<DynamicFormValidationResponse<? extends BaseComponent>>> getValidationObservable() {
        return (validator == null) ? null : validator.toObservable();
    }
}
