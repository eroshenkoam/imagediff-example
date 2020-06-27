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

    private VisualRegressionTracker visualRegressionTracker;


    @BeforeEach
    public void setUp() {
        final VisualRegressionTrackerConfig config = new VisualRegressionTrackerConfig(
                "http://localhost:4200",
                "4ab8cf1c-9aff-41b3-bdb5-cd4a59fa3ece",
                "Y7MC7VH2BQM1TVK6RS29NWF314P5",
                "master"
        );

        visualRegressionTracker = new VisualRegressionTracker(config);

        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.browserSize = "1200x800";
    }

    @Test
    public void testFullPage() throws Exception {
        final String request = "Погода в Санкт-Петербурге";
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


    private TestRunOptions defaultOpts() {
        return TestRunOptions.builder().diffTollerancePercent(0).build();
    }

    @AfterEach
    public void afterEach() {
        WebDriverRunner.closeWebDriver();
    }

}
