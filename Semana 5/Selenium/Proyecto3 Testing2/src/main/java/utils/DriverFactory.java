package utils;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createChromeDriver() {

        Map<String, Object> preferences = new HashMap<>();

        preferences.put("credentials_enable_service", false);
        preferences.put("profile.password_manager_enabled", false);
        preferences.put(
            "profile.password_manager_leak_detection",
            false
        );

        ChromeOptions options = new ChromeOptions();

        options.setExperimentalOption(
            "prefs",
            preferences
        );

        options.addArguments(
            "--disable-features=PasswordLeakDetection"
        );

        WebDriver driver = new ChromeDriver(options);

        driver.manage()
              .window()
              .maximize();

        return driver;
    }
}