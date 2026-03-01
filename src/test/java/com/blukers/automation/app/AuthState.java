package com.blukers.automation.app;

import com.blukers.automation.pages.android.HomePageAndroid;
import com.blukers.automation.pages.android.ProfilePageAndroid;

public final class AuthState {
    private AuthState() {}

    public static void ensureLogOut() {
        HomePageAndroid home = new HomePageAndroid();

        // already logged out
        if (home.isLoginButtonDisplayed()) return;

        ProfilePageAndroid profile = new ProfilePageAndroid();

        // bottom icon does not exist on Profile page itself
        if (!profile.isOnProfilePage()) {
            profile.openFromBottomNav();
        }

        profile.logout();

        // verify logout landed back in logged-out state
        if (!home.isLoginButtonDisplayed()) {
            throw new IllegalStateException("Logout attempted but Home(Login) not detected. Check logout behavior.");
        }
    }
}