package com.agora.monitoring.ui;

public class WebDriverWaitHelper {
    public static void waitFor(java.util.concurrent.Callable<Boolean> cond, long timeoutMs) throws Exception {
        long start = System.currentTimeMillis();
        while (true) {
            try {
                if (cond.call()) return;
            } catch (Exception e) {
                // swallow and retry until timeout
            }
            if (System.currentTimeMillis() - start > timeoutMs) throw new RuntimeException("Timeout waiting for condition");
            Thread.sleep(100);
        }
    }
}
