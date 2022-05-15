package com.codeFellow.taskmaster;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;

import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;

import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;



import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.codeFellow.taskmaster.repo.AppDatabase;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.codeFellow.taskmaster", appContext.getPackageName());
    }



//    @Rule
//    public ActivityScenarioRule<MainActivity> mainActivity1=new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public ActivityTestRule<MainActivity> mainActivity =
            new ActivityTestRule<>(MainActivity.class);


    // test to check if username is changed from sitting
    @Test
    public void test1(){
        openActionBarOverflowOrOptionsMenu(mainActivity.getActivity());
        onView(withText("Sitting"))
                .perform(click());

        onView(withId(R.id.edit_text_name)).perform(clearText());

        onView(withId(R.id.edit_text_name)).perform(typeText("faisal"));

        onView(withId(R.id.btn_submit)).perform(click());

//        String text=mainActivity.getActivity().findViewById(R.id.text_view_my_task).toString();

//        Espresso.onView(withId(R.id.text_view_my_task)).check(matches(isDisplayed()));

    }

    // add task test
    @Test
    public void addTaskTest(){
        onView(withId(R.id.btnAddTask)).perform(click());
        onView(withId(R.id.taskTitleEditTxt)).perform(typeText("task10"));
        onView(withId(R.id.taskDescriptionEditTxt)).perform(typeText("task10"));

        onView(withId(R.id.addButton)).perform(click());
    }

    // recycler view test to check onClick event
    // to check if the task have the correct data in detail page
    @Test
    public void test2(){

        // you need to add task first
        onView(withId(R.id.recycler_view_task)).perform(actionOnItemAtPosition(1,click()));

        // check the title
        onView(withId(R.id.text_view_title)).check(matches(withText("task2")));

        // check the description
        onView(withId(R.id.text_view_lorem)).check(matches(withText("task2")));

    }




}