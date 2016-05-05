package info.romankirillov.silencer;

import android.media.AudioManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TimePicker;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SilencerEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);


    @Test
    public void snoozeFor3seconds() throws InterruptedException {
        onView(withId(R.id.durationSpinner)).perform(click());
        onView(withText("Custom time")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(0, 0));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.silence_go)).perform(click());

        Thread.sleep(1000);

        assertEquals(
                AudioManager.RINGER_MODE_VIBRATE,
                Silencer.getCurrentRingerMode(InstrumentationRegistry.getTargetContext()));
    }

    @Before
    public void before() {

        // Closing artifacts from potential previous runs
        try {
            onView(withText("Cancel")).perform(click());
        } catch (Throwable e) {}

        onView(withId(R.id.durationSpinner)).perform(click());
        onView(withText("15 minutes")).perform(click());

        Silencer.unsilence(InstrumentationRegistry.getTargetContext());
    }

    @After
    public void after() {
        onView(withId(R.id.durationSpinner)).perform(click());
        onView(withText("15 minutes")).perform(click());

        Silencer.unsilence(InstrumentationRegistry.getTargetContext());
    }

}
