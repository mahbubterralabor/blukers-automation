package com.blukers.automation.pages.android;

import com.blukers.automation.config.Platform;
import com.blukers.automation.pages.base.BasePage_Backup;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class PersonalDetailsAndroid extends BasePage_Backup {

    public PersonalDetailsAndroid() {
        super("PersonalDetails", Platform.ANDROID);
    }

    /* -------------------- Screen checks -------------------- */

    public boolean isPersonalDetailsDisplayed() {
        return isDisplayed("gender_male_button") || isDisplayed("gender_female_button");
    }

    /* -------------------- High-level action -------------------- */

    /**
     * Completes Personal Details screen and moves to the next screen.
     * Keeps step definitions clean: Steps call ONE page API method.
     */
    public void completePersonalDetailsAndContinue(LocalDate dob) {
        selectMale();     // or expose selectFemale() and decide in steps/config
        openDobPicker();
        selectDob(dob);
        tapNext();
    }

    /* -------------------- Gender -------------------- */

    public void selectMale() {
        click("gender_male_button");
    }

    public void selectFemale() {
        click("gender_female_button");
    }

    /* -------------------- DOB (Date Picker) -------------------- */

    public void openDobPicker() {
        click("dob_open");
    }

    public void selectDob(LocalDate dob) {
        navigateToYearAndMonthIfNeeded(dob);
        selectDayFromGrid(dob.getDayOfMonth());
        click("dob_confirm");
    }

    /**
     * Handles:
     * 1) DOB is already in the currently displayed year/month -> do nothing
     * 2) DOB is different -> open year/month grids only when needed
     * 3) Overlapping elements -> prefer grid "android.view.View" for selections
     */
    private void navigateToYearAndMonthIfNeeded(LocalDate dob) {

        String targetYear = String.valueOf(dob.getYear());

        // Header shows FULL month (e.g., "February"), grid items are SHORT (e.g., "Feb")
        String targetMonthHeader = dob.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String targetMonthGrid = dob.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        // -------- YEAR --------
        String currentYear = getHeaderYear();
        if (!currentYear.equals(targetYear)) {
            tapHeaderYearChip();
            selectFromGridPreferView(targetYear);
        }

        // -------- MONTH --------
        String currentMonthHeader = getHeaderMonth(); // e.g. "February"
        if (!currentMonthHeader.equalsIgnoreCase(targetMonthHeader)) {
            tapHeaderMonthChip();
            selectFromGridPreferView(targetMonthGrid);
        }
    }

    private void selectDayFromGrid(int dayOfMonth) {
        selectFromGridPreferView(String.valueOf(dayOfMonth));
    }

    /* -------------------- Header chip helpers -------------------- */

    private void tapHeaderYearChip() {
        WebElement yearChip = findHeaderChipByRegex("\\d{4}");
        yearChip.click(); // opens year grid
    }

    private void tapHeaderMonthChip() {
        // Month chip is a Button with letters, e.g. "February"
        WebElement monthChip = findHeaderChipByRegex("[A-Za-z]{3,12}");
        monthChip.click(); // opens month grid
    }

    private String getHeaderYear() {
        return findHeaderChipByRegex("\\d{4}").getAttribute("content-desc");
    }

    private String getHeaderMonth() {
        return findHeaderChipByRegex("[A-Za-z]{3,12}").getAttribute("content-desc");
    }

    private WebElement findHeaderChipByRegex(String regex) {
        List<WebElement> buttons = driver.findElements(AppiumBy.className("android.widget.Button"));

        for (WebElement btn : buttons) {
            String desc = btn.getAttribute("content-desc");
            if (desc != null && desc.matches(regex) && btn.isDisplayed() && btn.isEnabled()) {
                return btn;
            }
        }

        throw new IllegalStateException("Header chip not found for pattern: " + regex);
    }

    /* -------------------- Grid selection helpers -------------------- */

    /**
     * Because of overlapping elements in the picker (Appium Inspector shows duplicates),
     * we:
     * - locate ALL matches by accessibilityId
     * - prefer android.view.View elements (grid items)
     * - click the first displayed+enabled element that matches the preference
     */
    private void selectFromGridPreferView(String accessibilityId) {
        List<WebElement> elements = driver.findElements(AppiumBy.accessibilityId(accessibilityId));
        if (elements == null || elements.isEmpty()) {
            throw new IllegalStateException("No elements found for accessibilityId: " + accessibilityId);
        }

        // First pass: prefer grid items
        WebElement preferred = firstDisplayedEnabledByClass(elements, "android.view.View");
        if (preferred != null) {
            preferred.click();
            return;
        }

        // Second pass: fallback to any displayed+enabled
        WebElement fallback = firstDisplayedEnabled(elements);
        if (fallback != null) {
            fallback.click();
            return;
        }

        throw new IllegalStateException("Unable to click element for accessibilityId: " + accessibilityId);
    }

    private WebElement firstDisplayedEnabledByClass(List<WebElement> elements, String className) {
        for (WebElement el : elements) {
            String cls = safeAttr(el, "className");
            if (className.equals(cls) && el.isDisplayed() && el.isEnabled()) {
                return el;
            }
        }
        return null;
    }

    private WebElement firstDisplayedEnabled(List<WebElement> elements) {
        for (WebElement el : elements) {
            if (el.isDisplayed() && el.isEnabled()) {
                return el;
            }
        }
        return null;
    }

    private String safeAttr(WebElement el, String name) {
        try {
            return el.getAttribute(name);
        } catch (Exception ignored) {
            return null;
        }
    }

    /* -------------------- Navigation -------------------- */

    public void tapNext() {
        click("next_button");
    }
}