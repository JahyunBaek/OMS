package ko.co._29cm.homework.oms.Runner;

import java.math.BigDecimal;
import java.util.*;

import ko.co._29cm.homework.oms.Dto.ProductDto;
import ko.co._29cm.homework.oms.Exception.ProductNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ko.co._29cm.homework.oms.Entity.ProductEntity;
import ko.co._29cm.homework.oms.Service.OrderService;
import ko.co._29cm.homework.oms.Util.StringManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!test")
public class CmdRunner implements CommandLineRunner {

    private final OrderService orderService;

    @Override
    public void run(String... args) {
            try (Scanner scanner = new Scanner(System.in)) {
                log.info("INIT DB...");
                System.out.println("Please Input File Name \n Example ===> [29CM 23 SS 공채] 백엔드 과제 _items.csv ");
                String actualInput = scanner.nextLine();
                //String actualInput = "[29CM 23 SS 공채] 백엔드 과제 _items.csv";

                if (orderService.InitDB(actualInput)) {
                    log.info("DB INIT Completed...");
                    handleUserInput(scanner);
                } else {
                    log.error("DB INIT Failed...");
                }
            }
    }

    private void handleUserInput(Scanner scanner) {
        while (true) {
            System.out.print("입력(o[order]): 주문, q[quit]: 종료) :");
            String actualInput = scanner.nextLine();

            if (actualInput.equals("q") || actualInput.equals("quit")) {
                System.out.println("고객님의 주문 감사합니다.");
                break;
            } else if (actualInput.equals("o")) {
                showAllProducts();
                orderProcess(scanner);
            } else {
                System.out.println("유효하지 않은 명령어 입니다.");
            }
        }
    }

    private void showAllProducts() {
        List<ProductEntity> allProductList = orderService.getAllProduct();
        System.out.println("상품번호\t상품명\t판매가격\t재고수");
        allProductList.forEach(product -> {
            System.out.println(product.getProductId() + "\t" + product.getName() + "\t" + product.getPrice() + "\t" + product.getQty());
        });
    }
    private void orderProcess(Scanner scanner) {
        Map<Long, ProductDto> productMap = new HashMap<>();

        while (true) {
            try {
                System.out.print("상품번호: ");
                String pId = scanner.nextLine();

                if (StringManager.IsEmpty(pId)) {
                    if (productMap.size() > 0) {
                        System.out.println("------------------------------------------------------------");
                        orderService.saveOrder(productMap);
                        productMap.clear();
                        break;
                    } else {
                        System.out.println("주문 정보가 없습니다.");
                    }
                } else {
                    Long convertId = Long.parseLong(pId);
                    Optional<ProductEntity> product = orderService.getProduct(convertId);

                    if (product == null) {
                        throw new ProductNotFoundException();
                    }

                    System.out.print("수량: ");
                    String pQty = scanner.nextLine();

                    if (StringManager.IsEmpty(pQty)) {
                        if (productMap.size() > 0) {
                            System.out.println("------------------------------------------------------------");
                            orderService.saveOrder(productMap);
                            productMap.clear();
                            break;
                        } else {
                            System.out.println("주문 정보가 없습니다.");
                        }
                    } else {
                        Integer convertQty = Integer.parseInt(pQty);
                        BigDecimal priceResult = product.get().getPrice().multiply(new BigDecimal(convertQty));
                        addProductToOrderMap(productMap, product, convertId, convertQty, priceResult);
                    }
                }
            } catch (NumberFormatException e) {
                log.error("NumberFormatException 발생. 유효하지 않은 숫자입니다. 다시 시도해주세요.");
            } catch (IllegalArgumentException e) {
                log.error("IllegalArgumentException 발생. 타입 변환 오류가 발생했습니다.");
            } catch (ProductNotFoundException e){
                log.error("ProductNotFoundException 발생. 상품을 찾을 수 없습니다.");
            }
        }
    }

    private void addProductToOrderMap(Map<Long, ProductDto> productMap, Optional<ProductEntity> product, Long convertId, Integer convertQty, BigDecimal priceResult) {
        productMap.compute(convertId, (key, oldValue) -> {
            BigDecimal totalPrice = (oldValue != null) ? oldValue.getTotalPrice().add(priceResult) : priceResult;
            Integer orderQty = (oldValue != null) ? oldValue.getOrderQty() + convertQty : convertQty;
            return ProductDto.builder()
                    .id(product.get().getProductId())
                    .name(product.get().getName())
                    .totalPrice(totalPrice)
                    .orderQty(orderQty)
                    .build();
        });
    }

}
