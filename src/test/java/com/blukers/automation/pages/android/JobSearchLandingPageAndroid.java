package com.blukers.automation.pages.android;
import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage;

public class JobSearchLandingPageAndroid extends BasePage {

    public JobSearchLandingPageAndroid(){
        super("JobSearchLandingPage",Platform.ANDROID);
    }


    public boolean isJobSearchPageVisibile(){
        return isDisplayed("ExploreJobs");
    }
}
