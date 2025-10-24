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
public class AlertOnTemperatureThresholdSeleniumTest {

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
    public void Alert_OnTemperatureThreshold() throws Exception {
        String base = "http://localhost:" + port + "/";

        // Set threshold for sensor 101 = 40
        postJson("http://localhost:" + port + "/api/config/sensors/101", "{\"max\":40}");

        // Inject a high temperature base for sensor 101
        postJson("http://localhost:" + port + "/api/test/sensors/101", "{\"value\":45}");

        // Trigger monitor poll to create alert
        postJson("http://localhost:" + port + "/api/test/control/poll/temps", "{}");

        DashboardPage page = new DashboardPage(driver);
        page.open(base);

        WebElement row = page.findSensorRowById("101");
        // row should have class 'alert-row'
        assertTrue(page.isRowAlert(row), "Sensor row 101 should be highlighted as alert");

        // At least one alert list item should reference sensor 101
        boolean found = page.getAlertListItems().stream().anyMatch(li -> li.getText().contains("101"));
        assertTrue(found, "Alert list should contain an entry for sensor 101");
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
