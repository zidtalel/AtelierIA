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
public class FanAddThresholdSeleniumTest {

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
    public void addValidFanThresholdPersists() throws Exception {
        String base = "http://localhost:" + port + "/";
        driver.get(base);

        DashboardPage page = new DashboardPage(driver);
        // Use existing simulated fan id
        page.setFanId("fan_cpu");
        page.setFanMin("900");
        page.clickSetFan();

        // Wait for reload
        Thread.sleep(1500);

        // Wait until the fan row exists and shows the threshold
        WebDriverWaitHelper.waitFor(() -> page.findFanRowById("fan_cpu") != null, 3000);
        org.openqa.selenium.WebElement row = page.findFanRowById("fan_cpu");
        assertThat(row).isNotNull();
        String thr = page.getCellText(row, 3);
        assertThat(thr).contains("900");

        // Also verify persisted config via API
        java.net.URL url = new java.net.URL(base + "api/config/all");
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int code = conn.getResponseCode();
        assertThat(code).isEqualTo(200);
        java.io.InputStream in = conn.getInputStream();
        String body = new String(in.readAllBytes());
        assertThat(body).contains("fan:fan_cpu:min");
        assertThat(body).contains("900");
    }
}
