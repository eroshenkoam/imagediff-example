package io.eroshenkoam.imagediff;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.visual_regression_tracker.sdk_java.TestRunOptions;
import io.visual_regression_tracker.sdk_java.VisualRegressionTracker;
import io.visual_regression_tracker.sdk_java.VisualRegressionTrackerConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.eroshenkoam.imagediff.ScreenshotUtils.makeScreenshotAsString;

public class VisualExample {

    private final static String ENDPOINT = "http://localhost:4200";
    private final static String PROJECT_ID = "05c3e558-d2db-4a2c-9be7-dcc7d02e74bf";
    private final static String API_KEY = "4BVRSTR2M34G5PG67DH16F1DWH6P";
    private final static String BRANCH = "master";

    private VisualRegressionTracker visualRegressionTracker;

    @BeforeEach
    public void setUp() {
        final VisualRegressionTrackerConfig config = new VisualRegressionTrackerConfig(
                ENDPOINT, PROJECT_ID, API_KEY, BRANCH
        );
        visualRegressionTracker = new VisualRegressionTracker(config);

        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.browserSize = "1200x800";
    }

    @Test
    public void testFullPage() throws Exception {
        final String request = "Погода в Москве";
//        final String request = "Погода в Санкт-Петербурге";

        open("https://yandex.ru");

        $("#text").sendKeys(request);
        $("#text").submit();
        $("[data-fast-wzrd='weather']").should(Condition.exist);

        visualRegressionTracker.track(
                "Weather Wizard",
                makeScreenshotAsString($("[data-fast-wzrd='weather']")),
                defaultOpts()
        );
    }

    @AfterEach
    public void afterEach() {
        WebDriverRunner.closeWebDriver();
    }

    private TestRunOptions defaultOpts() {
        return TestRunOptions.builder().diffTollerancePercent(0).build();
    }

}
