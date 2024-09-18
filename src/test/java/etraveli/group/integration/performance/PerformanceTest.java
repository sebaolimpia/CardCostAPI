package etraveli.group.integration.performance;

import static etraveli.group.common.Constants.BASE_URL;
import static etraveli.group.util.Constants.PASSWORD;
import static etraveli.group.util.Constants.USER;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import etraveli.group.integration.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import java.util.concurrent.ExecutorService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PerformanceTest extends IntegrationTest {

    private static final int REQUESTS_PER_MINUTE = 7000;

    private static final int CONCURRENT_THREADS = 117;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void performanceTest() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ExecutorService executorService = newFixedThreadPool(CONCURRENT_THREADS);

        for (int i = 0; i < REQUESTS_PER_MINUTE; i++) {
            executorService.submit(() -> {
                var requestEntity = createRequestEntityWithAuthorizationToken(USER, PASSWORD, null, GET);

                // WHEN
                ResponseEntity<String> responseEntity =
                        testRestTemplate.exchange(BASE_URL + "/all", GET, requestEntity, String.class);

                assertEquals(OK, responseEntity.getStatusCode());
            });
        }

        // Shutdown the executor service and wait for all tasks to finish
        executorService.shutdown();
        executorService.awaitTermination(1, MINUTES);

        stopWatch.stop();
        System.out.printf("Total time taken for %d requests: %d seconds.%n",
                REQUESTS_PER_MINUTE, stopWatch.getTotalTimeMillis() / 1000);
    }
}
