package com.agora.monitoring.ui;

import com.agora.monitoring.web.WebApplication;
import com.agora.monitoring.ui.pages.DashboardPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = WebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlertOnFanThresholdSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--headless=new");
        opts.addArguments("--no-sandbox");
        opts.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(opts);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test
    public void Alert_OnFanThreshold() throws Exception {
        String base = "http://localhost:" + port + "/";

        // Set fan min RPM for fan_chassis = 800
        postJson("http://localhost:" + port + "/api/config/fans/fan_chassis", "{\"min\":800}");

        // Inject lower RPM to trigger alert
        postJson("http://localhost:" + port + "/api/test/fans/fan_chassis", "{\"rpm\":700}");

        // Trigger fan poll
        postJson("http://localhost:" + port + "/api/test/control/poll/fans", "{}");

        DashboardPage page = new DashboardPage(driver);
        page.open(base);

        WebElement row = page.findSensorRowById("fan_chassis");
        assertTrue(page.isRowAlert(row), "Fan row fan_chassis should be highlighted as alert");

        boolean found = page.getAlertListItems().stream().anyMatch(li -> li.getText().contains("fan_chassis"));
        assertTrue(found, "Alert list should contain an entry for fan_chassis");
    }

    private void postJson(String urlStr, String body) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        conn.getOutputStream().write(body.getBytes(StandardCharsets.UTF_8));
        int code = conn.getResponseCode();
        if (code < 200 || code >= 300) throw new RuntimeException("HTTP " + code + " for " + urlStr);
    }
}
