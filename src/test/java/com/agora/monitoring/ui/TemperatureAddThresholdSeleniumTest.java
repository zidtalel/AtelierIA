package com.agora.monitoring.ui;

import com.agora.monitoring.ui.pages.DashboardPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.agora.monitoring.web.WebApplication.class)
public class TemperatureAddThresholdSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void addValidThresholdPersists() throws Exception {
        String base = "http://localhost:" + port + "/";
        driver.get(base);

        DashboardPage page = new DashboardPage(driver);
        // use valid sensor 101
        page.setTempSensorId("101");
        page.setTempMax("55");
        page.clickSetTemp();

        // Wait a bit for reload to happen
        Thread.sleep(1500);

        // After reload, verify the sensor row shows threshold 55 or that config API returns it
        WebDriverWaitHelper.waitFor(() -> page.findTemperatureRowById("101") != null, 3000);
        org.openqa.selenium.WebElement row = page.findTemperatureRowById("101");
        assertThat(row).isNotNull();
        String thr = page.getCellText(row, 3);
        assertThat(thr).contains("55");
    }
}
