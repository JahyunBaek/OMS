package ko.co._29cm.homework.oms;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;


import ko.co._29cm.homework.oms.Dto.ProductDto;
import ko.co._29cm.homework.oms.Entity.ProductEntity;
import ko.co._29cm.homework.oms.Service.OrderService;


@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class OmsApplicationTests {

	@Autowired
    private OrderService orderService;

    private static ExecutorService executorService;

	@BeforeAll
    static void setUpExecutorService() {
        executorService = Executors.newFixedThreadPool(2);
    }

	@Test
	@Order(1)
	@DisplayName("DB INIT")
	public void InitDB(){
		String actualInput = "[29CM 23 SS 공채] 백엔드 과제 _items.csv";
		boolean initResult = orderService.InitDB(actualInput);

		assertThat(initResult).isTrue();
	}

    @Test
	@Order(2)
	@DisplayName("Order Test")
    public void MultiOrder() throws InterruptedException, ExecutionException{

		CompletableFuture<Boolean> futureFirstOrder = CompletableFuture.supplyAsync(() -> {
			Map<Long, ProductDto> productMap = new HashMap<>();
			Long pId = 782858L;
			Integer pQty = 27;
			Optional<ProductEntity> pInfo1 = orderService.getProduct(pId);
			productMap.put(pId, ProductDto.builder()
					.id(pInfo1.get().getProductId())
					.name(pInfo1.get().getName())
					.orderQty(pQty)
					.totalPrice(pInfo1.get().getPrice().multiply(new BigDecimal(pQty)))
					.build());
				return orderService.saveOrder(productMap);
		}, executorService);

		CompletableFuture<Boolean> futureSecondOrder = CompletableFuture.supplyAsync(() -> {
			Map<Long, ProductDto> productMap = new HashMap<>();
			Long pId = 782858L;
			Integer pQty = 29;
			Optional<ProductEntity> pInfo1 = orderService.getProduct(pId);
			productMap.put(pId, ProductDto.builder()
					.id(pInfo1.get().getProductId())
					.name(pInfo1.get().getName())
					.orderQty(pQty)
					.totalPrice(pInfo1.get().getPrice().multiply(new BigDecimal(pQty)))
					.build());
			return orderService.saveOrder(productMap);
		}, executorService);


		futureFirstOrder.whenCompleteAsync((result, exception) -> {
			callbackTest(exception,result);
		});
		futureSecondOrder.whenCompleteAsync((result, exception) -> {
			callbackTest(exception,result);
		});
		boolean firstResult = futureFirstOrder.get();
		boolean secondResult = futureSecondOrder.get();

		assertThat(firstResult).isNotEqualTo(secondResult);
    }

    public void callbackTest(Throwable e, Boolean result){
		if (e != null) {
			log.error("오류가발생하였습니다." + e.getMessage());
		} else {
			log.info("주문이 종료되었습니다.["+ result+"]");
		}
	}
}
