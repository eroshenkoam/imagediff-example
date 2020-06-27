package io.eroshenkoam.imagediff;

import com.codeborne.selenide.WebDriverRunner;
import org.apache.xerces.impl.dv.util.Base64;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ScreenshotUtils {

    public static final Path WORKSPACE = Paths.get("build/screenshots");

    public static final String IMAGE_TYPE = "PNG";

    private ScreenshotUtils() {
    }

    public static String makeScreenshotAsString(final WebElement element) throws IOException {
        return convert(createAShot().takeScreenshot(WebDriverRunner.getWebDriver(), element).getImage());
    }

    public static Screenshot makeScreenshot(final WebElement element) {
        return createAShot().takeScreenshot(WebDriverRunner.getWebDriver(), element);
    }

    public static void write(final BufferedImage image, final Class<?> clazz, final String id) throws IOException {
        final Path file = WORKSPACE.resolve(String.format("%s-%s.png", clazz.getCanonicalName(), id));
        if (Files.notExists(WORKSPACE)) {
            Files.createDirectories(WORKSPACE);
        }
        ImageIO.write(image, IMAGE_TYPE, file.toFile());
    }

    public static BufferedImage read(final Class<?> clazz, final String id) throws IOException {
        final Path file = WORKSPACE.resolve(String.format("%s-%s.png", clazz.getCanonicalName(), id));
        return ImageIO.read(file.toFile());
    }

    public static byte[] getBytes(final BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, IMAGE_TYPE, out);
        return out.toByteArray();
    }

    public static String convert(final BufferedImage image) throws IOException {
        return Base64.encode(getBytes(image));
    }

    private static AShot createAShot() {
        return new AShot()
                .coordsProvider(new WebDriverCoordsProvider())
                .shootingStrategy(ShootingStrategies.scaling(2));
    }
}
