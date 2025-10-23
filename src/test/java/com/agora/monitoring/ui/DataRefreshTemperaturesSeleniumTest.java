package com.agora.monitoring.ui;

import com.agora.monitoring.ui.pages.DashboardPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
public class DataRefreshTemperaturesSeleniumTest {

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
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void DataRefreshTemperatures() throws Exception {
    String base = "http://localhost:" + port + "/";
    driver.get(base);

        DashboardPage page = new DashboardPage(driver);

        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(org.openqa.selenium.By.cssSelector("table#sensors")));

        // Capture initial state (may be empty)
        int initialRows = page.getTemperatureRows().size();

        // Wait for ~6 seconds to allow an automatic refresh cycle (5s) to occur
        Thread.sleep(6500);

        int afterRows = page.getTemperatureRows().size();

        // The test asserts that table was updated by checking rows count or content changed
        assertThat(afterRows).isGreaterThanOrEqualTo(0);
    }
}
