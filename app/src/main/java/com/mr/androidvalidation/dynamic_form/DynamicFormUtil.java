package com.mr.androidvalidation.dynamic_form;

import android.view.View;

import com.mr.androidvalidation.dynamic_form.component.BaseComponent;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.FuncN;

public class DynamicFormUtil {

    public static Observable<Boolean> getFormIsValidObservable(List<BaseComponent> components) {
        ArrayList<Observable<Boolean>> observables = new ArrayList<>();
        for (BaseComponent component : components) {
            observables.add(component.getIsValidObservable());
        }
        return Observable.combineLatest(observables, new FuncN<Boolean>() {
            @Override
            public Boolean call(Object... args) {
                for (Object o : args) {
                    if (o instanceof Boolean) {
                        Boolean value = ((Boolean) o);
                        if (!value) {
                            return false;
                        }
                    }
                }
                return true;
            }
        });
    }

    public static void setFocusOrder(List<BaseComponent> components) {
        int childCount = 0;
        while (childCount < components.size()) {
            View component = (View) components.get(childCount);
            component.setId(childCount);
            if (childCount == 0) {
                component.setNextFocusDownId(childCount + 1);
            } else if (childCount < components.size() - 1) {
                component.setNextFocusUpId(childCount - 1);
                component.setNextFocusDownId(childCount + 1);
            } else if (childCount == components.size() - 1) {
                ((BaseComponent) component).setIsLastChild(true);
            }
            childCount++;
        }
    }
}