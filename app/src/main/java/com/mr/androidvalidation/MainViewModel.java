package com.mr.androidvalidation;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.InputType;

import com.mr.androidvalidation.descriptor.DynamicFormDescriptor;
import com.mr.androidvalidation.descriptor.RxDynamicFormValidator;
import com.mr.androidvalidation.descriptor.ValidatorDescriptor;
import com.mr.androidvalidation.factory.ComponentFactory;

import java.util.ArrayList;

public class MainViewModel {

    public final ObservableField<String> name = new ObservableField<>();
    private final ObservableField<String> phoneNumber = new ObservableField<>();
    private final ObservableField<String> city = new ObservableField<>();
    private final ObservableField<String> nickname = new ObservableField<>();
    private final ObservableField<String> nameOnBill = new ObservableField<>();

    public ObservableBoolean helloButtonEnabled = new ObservableBoolean(false);

    public MainViewModel() {

    }

    public ArrayList<DynamicFormDescriptor<?>> getViewDescriptors(Context context) {

        ValidatorDescriptor isNotEmpty = new ValidatorDescriptor(RxDynamicFormValidator.IS_NOT_EMPTY, context.getString(R.string.field_cannot_be_empty));
        return new ArrayList<DynamicFormDescriptor<?>>() {{
            add(DynamicFormDescriptor.getDescriptor(ComponentFactory.TEXT_INPUT, context.getString(R.string.label_first_name), name)
                    .addValidatorType(new ValidatorDescriptor(RxDynamicFormValidator.MIN_CHAR, context.getString(R.string.min_error), 2))
                    .addValidatorType(isNotEmpty)
                    .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                    .setHint(context.getString(R.string.hint_first_name)));

            add(DynamicFormDescriptor.getDescriptor(ComponentFactory.TEXT_INPUT, context.getString(R.string.label_phone_number), phoneNumber)
                    .setInputType(InputType.TYPE_CLASS_PHONE)
                    .setHint(context.getString(R.string.hint_phone_number))
                    .addValidatorType(isNotEmpty)
                    .addValidatorType(new ValidatorDescriptor(RxDynamicFormValidator.PHONE_NUMBER, context.getString(R.string.invalid_phone_number))));

            add(DynamicFormDescriptor.getDescriptor(ComponentFactory.TEXT_INPUT, context.getString(R.string.label_city), city)
                    .addValidatorType(isNotEmpty)
                    .setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                    .setHint(context.getString(R.string.hint_city)));

            add(DynamicFormDescriptor.getDescriptor(ComponentFactory.TEXT_INPUT, context.getString(R.string.label_nickname), nickname)
                    .addValidatorType(isNotEmpty)
                    .setInputType(InputType.TYPE_CLASS_TEXT)
                    .setHint(context.getString(R.string.hint_nickname)));

            add(DynamicFormDescriptor.getDescriptor(ComponentFactory.TEXT_INPUT, context.getString(R.string.hint_on_payslip), nameOnBill)
                    .setHint(context.getString(R.string.hint_on_payslip))
                    .setInputType(InputType.TYPE_CLASS_TEXT));
        }};
    }
}
