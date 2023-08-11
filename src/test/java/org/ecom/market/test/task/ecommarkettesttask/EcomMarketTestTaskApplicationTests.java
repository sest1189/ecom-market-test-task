package org.ecom.market.test.task.ecommarkettesttask;

import org.ecom.market.test.task.ecommarkettesttask.controllers.MainController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ContextConfiguration(classes = EcomMarketTestTaskApplication.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class EcomMarketTestTaskApplicationTests {

	@Autowired
	private MainController mainController;
	private final ExecutorService executorService = Executors.newCachedThreadPool();

	@Test
	void RequestCounterRestrictionAnnotation_callMethodFromController_responsesHasBeenGotAccordinglyConfiguration() throws ExecutionException, InterruptedException {
		HttpServletRequest request1 = new HttpRequestTest("1.1.1.1");
		HttpServletRequest request2 = new HttpRequestTest("1.1.1.2");
		HttpServletRequest request3 = new HttpRequestTest("1.1.1.3");
		HttpServletRequest request4 = new HttpRequestTest("1.1.1.4");
		for (int i = 0; i < 4; i++) {
			if (i == 3) {
				Thread.sleep(2000);
			}
			Future<ResponseEntity> response1 = executorService.submit(() -> mainController.getData(request1));
			Future<ResponseEntity> response2 = executorService.submit(() -> mainController.getData(request2));
			Future<ResponseEntity> response3 = executorService.submit(() -> mainController.getData(request3));
			Future<ResponseEntity> response4 = executorService.submit(() -> mainController.getData(request4));
			if (i < 2) {
				assertEquals(200, response1.get().getStatusCode().value());
				assertEquals(200, response2.get().getStatusCode().value());
				assertEquals(200, response3.get().getStatusCode().value());
				assertEquals(200, response4.get().getStatusCode().value());
			} else if (i == 2) {
				assertEquals(502, response1.get().getStatusCode().value());
				assertEquals(502, response2.get().getStatusCode().value());
				assertEquals(502, response3.get().getStatusCode().value());
				assertEquals(502, response4.get().getStatusCode().value());

			} else {
				assertEquals(200, response1.get().getStatusCode().value());
				assertEquals(200, response2.get().getStatusCode().value());
				assertEquals(200, response3.get().getStatusCode().value());
				assertEquals(200, response4.get().getStatusCode().value());
			}
		}

	}
}
