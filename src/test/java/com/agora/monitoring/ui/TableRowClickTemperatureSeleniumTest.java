package com.agora.monitoring.ui;

import com.agora.monitoring.ui.pages.TablePage;
import com.agora.monitoring.web.WebApplication;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.google.gson.Gson;
import java.util.Map;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
public class TableRowClickTemperatureSeleniumTest {

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
    public void TableRowClick_Temperature() throws Exception {
        // Pré-condition : s'assurer que le threshold pour le capteur 101 est défini à 50
        postJson("http://localhost:" + port + "/api/config/sensors/101", "{\"max\":50}");

        // Attendre que la configuration soit bien persistée et visible via /api/config/all
        waitForThreshold("101", 50.0, 5000);

        page.openTemperaturesPage();

        // Click sur la ligne data-id=101 dans le tableau #temperatures-table
    page.clickRowByDataId("table#sensors", "101");

    // Vérifier que les champs sont pré-remplis (ids présents dans dashboard.html)
    String idValue = page.getInputValue("input#temp-sensor-id");
    String thresholdValue = page.getInputValue("input#temp-max");

        assertEquals("101", idValue, "L'ID du capteur doit être pré-rempli avec 101");
        assertEquals("50", thresholdValue, "Le seuil doit être pré-rempli avec 50");
    }

    private Map<String, Double> getConfigAll() throws Exception {
        String url = "http://localhost:" + port + "/api/config/all";
        java.net.URL u = new java.net.URL(url);
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) u.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        int code = con.getResponseCode();
        if (code < 200 || code >= 300) {
            throw new RuntimeException("Failed GET " + url + " -> " + code);
        }
        try (InputStream is = con.getInputStream()) {
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Gson g = new Gson();
            java.lang.reflect.Type t = new com.google.gson.reflect.TypeToken<Map<String, Double>>(){}.getType();
            Map<String, Double> m = g.fromJson(body, t);
            return m != null ? m : java.util.Collections.emptyMap();
        }
    }

    private void waitForThreshold(String key, double expected, long timeoutMs) throws Exception {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMs) {
            try {
                Map<String, Double> cfg = getConfigAll();
                if (cfg.containsKey(key)) {
                    Double v = cfg.get(key);
                    if (v != null && Math.abs(v - expected) < 0.0001) return;
                }
            } catch (Exception e) {
                // ignore and retry
            }
            Thread.sleep(200);
        }
        throw new RuntimeException("Timeout waiting for threshold " + key + "=" + expected);
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
