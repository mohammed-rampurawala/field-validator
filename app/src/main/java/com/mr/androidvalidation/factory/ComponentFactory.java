package com.mr.androidvalidation.factory;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.Log;

import com.mr.androidvalidation.descriptor.DynamicFormDescriptor;
import com.mr.androidvalidation.dynamic_form.component.BaseComponent;
import com.mr.androidvalidation.dynamic_form.component.TextInputComponent;

import java.lang.annotation.Retention;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class ComponentFactory {

    @Retention(SOURCE)
    @IntDef({TEXT_INPUT})
    public @interface FormControl {
    }

    public static final int TEXT_INPUT = 0x01;

    public static List<BaseComponent> getComponentsFromDescriptors(Context context, List<DynamicFormDescriptor<?>> descriptors) {
        List<BaseComponent> components = new ArrayList<>();
        for (DynamicFormDescriptor<?> descriptor : descriptors) {
            components.add(getComponent(context, descriptor));
        }
        return components;
    }

    public static BaseComponent getComponent(Context context, DynamicFormDescriptor<?> descriptor) {
        BaseComponent component = null;
        @FormControl int type = descriptor.controlType;

        switch (type) {
            case TEXT_INPUT:
                component = new TextInputComponent(context);
                break;
            default:
                Log.i("Error", "No form control found for " + type);
        }

        if (component != null)
            component.init(descriptor);
        return component;
    }

}