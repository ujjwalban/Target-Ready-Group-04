package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.controllers.CustomerController;
import com.targetindia.EcomStreaming.repository.OrderRepository;
import com.targetindia.EcomStreaming.service.ArchivedOrdersServiceImpl;
import com.targetindia.EcomStreaming.service.KafkaConsumer;
import com.targetindia.EcomStreaming.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class EcomStreamingApplicationTests {

	@MockBean
	private OrderRepository orderRepository;
	@MockBean
	private CustomerController customerController;
	@MockBean
	private ArchivedOrdersServiceImpl archivedOrdersService;
	@MockBean
	private KafkaConsumer kafkaConsumer;
	@MockBean
	private OrderServiceImpl orderService;
	@Test
	void contextLoads() {
	}

}
