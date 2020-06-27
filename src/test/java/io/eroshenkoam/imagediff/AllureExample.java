package io.eroshenkoam.imagediff;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Attachment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.eroshenkoam.imagediff.ScreenshotUtils.getBytes;
import static io.eroshenkoam.imagediff.ScreenshotUtils.makeScreenshot;
import static io.eroshenkoam.imagediff.ScreenshotUtils.read;
import static io.eroshenkoam.imagediff.ScreenshotUtils.write;
import static io.qameta.allure.Allure.label;

public class AllureExample {

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.browserSize = "1200x800";
    }


    @Test
    public void testFullPage() throws Exception {
        label("testType", "screenshotDiff");

        final String request = "Погода в Санкт-Петербурге";
        open("https://yandex.ru");

        $("#text").sendKeys(request);
        $("#text").submit();
        $("[data-fast-wzrd='weather']").should(Condition.exist);

        final Screenshot actual = makeScreenshot($("[data-fast-wzrd='weather']"));
        write(actual.getImage(), AllureExample.class, "weather");

        final BufferedImage expected = read(AllureExample.class, "weather");

        ImageDiff diff = new ImageDiffer().makeDiff(actual.getImage(), expected);
        write(diff.getMarkedImage(), AllureExample.class, "diff");

        addScreenshot("expected", expected);
        addScreenshot("actual", actual.getImage());
        addScreenshot("diff", diff.getMarkedImage());
    }

    @Attachment(value = "{name}", type = "image/png")
    public byte[] addScreenshot(final String name, final BufferedImage image) throws IOException {
        return getBytes(image);
    }

    @AfterEach
    public void afterEach() {
        WebDriverRunner.closeWebDriver();
    }

}
