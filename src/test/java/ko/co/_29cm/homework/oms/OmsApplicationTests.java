package ko.co._29cm.homework.oms;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import ko.co._29cm.homework.oms.Runner.CmdRunner;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;


import ko.co._29cm.homework.oms.Dto.ProductDto;
import ko.co._29cm.homework.oms.Entity.ProductEntity;
import ko.co._29cm.homework.oms.Repository.OrderRepository;
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
    public void MultidOrder() throws InterruptedException, ExecutionException{
    	ExecutorService executor = Executors.newFixedThreadPool(2);
    	Future<ProductEntity> futureFirstOrder = executor.submit(() -> {
    		Map<Long,ProductDto> productMap = new HashMap<>();
    		Long pId = 782858l;
    		Integer pQty = 27;
    		ProductEntity pInfo1 = orderService.getProduct(pId);
    		productMap.put(pId, ProductDto.builder().id(pInfo1.getProductId())
    		.name(pInfo1.getName()).orderQty(pQty).totalPrice(pInfo1.getPrice().multiply(new BigDecimal(pQty))).build());
			orderService.saveOrder(productMap);
    		return orderService.getProduct(pId);
        });

    	Future<ProductEntity> futureSecnodOrder = executor.submit(() -> {
    		Map<Long,ProductDto> productMap = new HashMap<>();
    		Long pId = 782858l;
    		Integer pQty = 29;
    		ProductEntity pInfo1 = orderService.getProduct(pId);
    		productMap.put(pId, ProductDto.builder().id(pInfo1.getProductId())
    		.name(pInfo1.getName()).orderQty(pQty).totalPrice(pInfo1.getPrice().multiply(new BigDecimal(pQty))).build());
			orderService.saveOrder(productMap);
    		return orderService.getProduct(pId);
        });

		ProductEntity p1 = futureFirstOrder.get();
		ProductEntity p2 = futureSecnodOrder.get();

		assertThat(p1.getQty()).isPositive();
		assertThat(p2.getQty()).isPositive();
    }
}
