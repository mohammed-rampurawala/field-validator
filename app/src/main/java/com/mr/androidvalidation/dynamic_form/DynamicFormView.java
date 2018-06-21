package com.mr.androidvalidation.dynamic_form;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.mr.androidvalidation.MainViewModel;
import com.mr.androidvalidation.dynamic_form.component.BaseComponent;
import com.mr.androidvalidation.factory.ComponentFactory;

import java.util.List;

import rx.Subscriber;
import rx.functions.Action1;

public class DynamicFormView extends LinearLayout {

    private List<BaseComponent> components;

    private Action1<Boolean> consumer;

    public DynamicFormView(Context context) {
        super(context);
        init();
    }

    public DynamicFormView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DynamicFormView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setConsumer(Action1<Boolean> consumer) {
        this.consumer = consumer;
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    public void setViewModel(MainViewModel viewModel) {
        components = ComponentFactory.getComponentsFromDescriptors(getContext(), viewModel.getViewDescriptors(getContext()));
        for (BaseComponent component : components)
            addView((View) component);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        observeFormComponents(components);
    }

    protected void observeFormComponents(List<BaseComponent> components) {
        DynamicFormUtil.getFormIsValidObservable(components)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        onFormValidationCheck(aBoolean);
                    }
                });
    }

    protected void onFormValidationCheck(boolean value) {
        consumer.call(value);
    }
}
