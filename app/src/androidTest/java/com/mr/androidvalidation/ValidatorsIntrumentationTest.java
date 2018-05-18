package com.mr.androidvalidation;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase;
import android.test.ActivityUnitTestCase;
import android.text.InputType;

import com.mr.androidvalidation.descriptor.DynamicFormDescriptor;
import com.mr.androidvalidation.descriptor.RxDynamicFormValidator;
import com.mr.androidvalidation.descriptor.ValidatorDescriptor;
import com.mr.androidvalidation.dynamic_form.component.TextInputComponent;
import com.mr.androidvalidation.factory.ComponentFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * Instrumentation test cases for form validators.
 */
@RunWith(AndroidJUnit4.class)
public class ValidatorsIntrumentationTest {


    private Context appContext;

    private ArrayList<DynamicFormDescriptor<?>> viewDescriptors;

    @Before
    public void init() {
        appContext = InstrumentationRegistry.getTargetContext();
        MainViewModel mainViewModel = new MainViewModel();
        viewDescriptors = mainViewModel.getViewDescriptors(appContext);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.

        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.mr.androidvalidation", appContext.getPackageName());
    }

    @Test
    public void minLengthValidator() {
        getInstrumentation().runOnMainSync(() -> {
            //0 for name validator
            TextInputComponent component = new TextInputComponent(appContext);
            component.init(viewDescriptors.get(0));

            component.setText("xyz");
            assertTrue(component.getError() == null);
        });
    }

    @Test
    public void minLengthValidatorFailure() {
        getInstrumentation().runOnMainSync(() -> {
            //0 for name validator
            TextInputComponent component = new TextInputComponent(appContext);
            component.init(viewDescriptors.get(0));

            component.setText("x");
            System.out.println(component.getError().toString());
            assertTrue(component.getError() != null);
        });
    }

    @Test
    public void notEmptyValidator() {
        getInstrumentation().runOnMainSync(() -> {
            //0 for name validator
            TextInputComponent component = new TextInputComponent(appContext);
            component.init(viewDescriptors.get(0));

            component.setText("");
            System.out.println(component.getError().toString());
            assertTrue(component.getError() != null);
        });
    }

    @Test
    public void phoneNumberFailureValidator() {
        getInstrumentation().runOnMainSync(() -> {
            //0 for phone validator
            TextInputComponent component = new TextInputComponent(appContext);
            component.init(viewDescriptors.get(1));

            component.setText("xy");
            assertTrue(component.getError() != null);
        });
    }

    @Test
    public void phoneNumberSuccessValidator() {
        getInstrumentation().runOnMainSync(() -> {
            //1 for phone validator
            TextInputComponent component = new TextInputComponent(appContext);
            component.init(viewDescriptors.get(1));

            component.setText("8600362940");
            assertTrue(component.getError() == null);
        });
    }

    @Test
    public void noValidatorTest() {
        getInstrumentation().runOnMainSync(() -> {
            //1 for phone validator
            TextInputComponent component = new TextInputComponent(appContext);
            component.init(viewDescriptors.get(viewDescriptors.size() - 1));

            component.setText("hello world");
            assertTrue(component.getError() == null);
        });
    }

    @Test
    public void noValidatorTestNoError() {
        getInstrumentation().runOnMainSync(() -> {
            //1 for phone validator
            TextInputComponent component = new TextInputComponent(appContext);
            component.init(viewDescriptors.get(viewDescriptors.size() - 1));

            component.setText("");
            assertTrue(component.getError() == null);
        });
    }
}
