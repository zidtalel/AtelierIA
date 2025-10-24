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
public class TemperatureAddThresholdNonNumericSeleniumTest {

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
    public void nonNumericThresholdShowsValidation() throws Exception {
        String base = "http://localhost:" + port + "/";
        driver.get(base);

        DashboardPage page = new DashboardPage(driver);
        // use valid sensor 101
        page.setTempSensorId("101");
        page.setTempMax("abc");
        page.clickSetTemp();

        String alert = page.getAlertTextIfPresent(3000);
        assertThat(alert).isNotNull();
        assertThat(alert).contains("Invalid max value");
    }
}
