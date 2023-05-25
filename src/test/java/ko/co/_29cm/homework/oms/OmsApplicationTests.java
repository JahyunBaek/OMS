package ko.co._29cm.homework.oms;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;


import ko.co._29cm.homework.oms.Dto.ProductDto;
import ko.co._29cm.homework.oms.Entity.ProductEntity;
import ko.co._29cm.homework.oms.Service.OrderService;


@SpringBootTest
@ActiveProfiles("test")
class OmsApplicationTests {

	@Autowired
    private OrderService orderService;

    private static ExecutorService executorService;


	@Test
	@Order(0)
	@DisplayName("DB Init")
	public void init(){
		String actualInput = "[29CM 23 SS 공채] 백엔드 과제 _items.csv";
		boolean temp = orderService.InitDB(actualInput);
		System.out.println("상품번호\t상품명\t판매가격\t재고수");
		orderService.getAllProduct().forEach(System.out::println);
		System.out.println(temp);
	}

	@BeforeEach
    void setupValues() {
        ReflectionTestUtils.setField(orderService, "deliveryFee", new BigDecimal(25));
		ReflectionTestUtils.setField(orderService, "defaultPath", "src/main/resources/sample/");
	}

    @Test
	@Order(1)
	@DisplayName("Order Test")
    @Execution(ExecutionMode.CONCURRENT)
    public void MultiOrder() throws InterruptedException, ExecutionException{
    	ExecutorService executor = Executors.newFixedThreadPool(2);
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
		}, executor);

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
		}, executor);


		futureFirstOrder.whenCompleteAsync((result, exception) -> {
			if (exception != null) {
				System.out.println("오류가발생하였습니다. : " + exception);
			} else {
				System.out.println("주문이 종료되었습니다.["+ result+"]");
			}
		});
		futureSecondOrder.whenCompleteAsync((result, exception) -> {
			if (exception != null) {
				System.out.println("오류가발생하였습니다. : " + exception);
			} else {
				System.out.println("주문이 종료되었습니다.["+ result+"]");
			}
		});
		boolean firstResult = futureFirstOrder.get();
		boolean secondResult = futureSecondOrder.get();

		assertThat(firstResult).isTrue();
		assertThat(secondResult).isFalse();
    }
}
