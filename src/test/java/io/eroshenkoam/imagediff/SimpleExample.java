package io.eroshenkoam.imagediff;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import java.awt.image.BufferedImage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.eroshenkoam.imagediff.ScreenshotUtils.makeScreenshot;
import static io.eroshenkoam.imagediff.ScreenshotUtils.read;
import static io.eroshenkoam.imagediff.ScreenshotUtils.write;

public class SimpleExample {

    @BeforeEach
    public void setUp() {
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

        final Screenshot actual = makeScreenshot($("[data-fast-wzrd='weather']"));
        write(actual.getImage(), SimpleExample.class, "weather");

        final BufferedImage expected = read(SimpleExample.class, "weather");

        ImageDiff diff = new ImageDiffer().makeDiff(actual.getImage(), expected);
        write(diff.getMarkedImage(), SimpleExample.class, "diff");
    }

    @AfterEach
    public void afterEach() {
        WebDriverRunner.closeWebDriver();
    }

}
