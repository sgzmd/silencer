package info.romankirillov.silencer;

import android.media.AudioManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SilencerEspressoTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule(MainActivity.class);


    @Test
    public void snoozeFor3seconds() throws InterruptedException {
        onView(withId(R.id.durationSpinner)).perform(click());
        setCustomDuration(0, 0);
        onView(withId(R.id.silence_go)).perform(click());

        Thread.sleep(1000);

        assertEquals(
                AudioManager.RINGER_MODE_VIBRATE,
                Silencer.getCurrentRingerMode(InstrumentationRegistry.getTargetContext()));
    }

    @Test
    public void checkCustomTextUpdated() {
        onView(withId(R.id.durationSpinner)).perform(click());

        setCustomDuration(3, 3);

        onView(withId(R.id.customDurationText)).check(matches(isDisplayed()));
        onView(withId(R.id.customDurationText)).check(matches(withText("3 hours 3 minutes")));
        onView(withId(R.id.durationSpinner)).perform(click());
        onView(withText("15 minutes")).perform(click());
        onView(withId(R.id.customDurationText)).check(matches(not(isDisplayed())));
    }

    private static void setCustomDuration(int hours, int minutes) {
        onView(withText("Custom time")).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(hours, minutes));
        onView(withText("OK")).perform(click());
    }

    @Before
    public void before() {
        closeOldDialogues();

        onView(withId(R.id.durationSpinner)).perform(click());
        onView(withText("15 minutes")).perform(click());

        Silencer.unsilence(InstrumentationRegistry.getTargetContext());
    }

    private static void closeOldDialogues() {
        // Closing artifacts from potential previous runs
        try {
            onView(withText("Cancel")).perform(click());
        } catch (Throwable e) {}
    }

    @After
    public void after() {
        Silencer.unsilence(InstrumentationRegistry.getTargetContext());
    }
}
