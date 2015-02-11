package org.egov.search.util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.junit.Assert.fail;

public class JsonMatcher {

    public static Matcher<? super String> matches(final String expectedJson) {
        return new BaseMatcher<String>() {
            @Override
            public boolean matches(Object o) {
                try {
                    JSONAssert.assertEquals(expectedJson, ((String) o), true);

                } catch (org.json.JSONException e) {
                    fail(e.getMessage());
                }

                return true;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected " + expectedJson);
            }
        };

    }

}
