package com.agora.monitoring.ui;

import com.agora.monitoring.ui.pages.TablePage;
import com.agora.monitoring.web.WebApplication;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = WebApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class TableRowClickFanSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private TablePage page;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        page = new TablePage(driver, "http://localhost:" + port);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void TableRowClick_Fan() throws Exception {
        // Pré-condition : définir un seuil minimal pour le ventilateur 'fan_chassis'
        postJson("http://localhost:" + port + "/api/config/fans/fan_chassis", "{\"min\":800}");

        page.openFansPage();

        // Click sur la ligne id='fan_chassis' dans le tableau #fans
        page.clickRowByDataId("table#fans", "fan_chassis");

        // Vérifier que les champs sont pré-remplis (ids présents dans dashboard.html)
        String idValue = page.getInputValue("input#fan-id");
        String rpmValue = page.getInputValue("input#fan-min");

        assertEquals("fan_chassis", idValue, "L'ID du ventilateur doit être pré-rempli avec fan_chassis");
        assertEquals("800", rpmValue, "Le RPM minimal doit être pré-rempli avec 800");
    }

    private void postJson(String url, String jsonBody) throws Exception {
        java.net.URL u = new java.net.URL(url);
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) u.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        try (java.io.OutputStream os = con.getOutputStream()) {
            os.write(jsonBody.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
        int code = con.getResponseCode();
        if (code < 200 || code >= 300) {
            throw new RuntimeException("Failed POST " + url + " -> " + code);
        }
    }
}
