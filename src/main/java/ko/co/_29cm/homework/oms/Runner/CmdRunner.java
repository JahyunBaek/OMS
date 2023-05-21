package ko.co._29cm.homework.oms.Runner;

import java.math.BigDecimal;
import java.util.*;

import ko.co._29cm.homework.oms.Dto.ProductDto;
import ko.co._29cm.homework.oms.Exception.ProductNotFoundException;
import ko.co._29cm.homework.oms.Exception.SoldOutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ko.co._29cm.homework.oms.Entity.ProductEntity;
import ko.co._29cm.homework.oms.Service.OrderService;
import ko.co._29cm.homework.oms.Util.StringManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CmdRunner implements CommandLineRunner {

    private final OrderService orderService;

    @Override
    public void run(String... args) throws Exception {

        try(Scanner scanner = new Scanner(System.in)){
            log.info("INIT DB...");
            System.out.println("Please Input File Name \n Example ===> [29CM 23 SS 공채] 백엔드 과제 _items.csv ");
            //String actualInput = scanner.nextLine();
            String pId;
            String pQty;
            String actualInput = "[29CM 23 SS 공채] 백엔드 과제 _items.csv";
            if(orderService.InitDB(actualInput)){
                log.info("DB INIT Completed...");
                         
                while (true) {
                    System.out.print("입력(o[order]): 주문, q[quit]: 종료) :");
                    actualInput = scanner.nextLine();
                    if(actualInput.equals("q") || actualInput.equals("quit")){
                        System.out.println("고객님의 주문 감사합니다.");
                        break;
                    }else if(actualInput.equals("o")){
                        List<ProductEntity> allProductList = orderService.getAllProduct();
                        System.out.println("상품번호\t상품명\t판매가격\t재고수");
                        allProductList.forEach( product -> {
                            System.out.println(product.getProductId() + "\t" + product.getName() + "\t" + product.getPrice() +"\t" +product.getQty());
                        });

                        Map<Long, ProductDto> productMap = new HashMap<>();

                        outer:while(true){
                            try{
                                ProductEntity product = new ProductEntity();
                                Long convertId = 0l;
                                Integer convertQty = 0;
                                System.out.print("상품번호: ");
                                pId = scanner.nextLine();
                                if(StringManager.IsEmpty(pId)){
                                    if(productMap.size() > 0){
                                        System.out.println("------------------------------------------------------------");
                                        orderService.saveOrder(productMap);
                                        productMap.clear();
                                        break outer;
                                    }else{
                                        System.out.println("주문 정보가 없습니다.");
                                    }
                                }else{
                                    convertId = Long.parseLong(pId);
                                    product = orderService.getProduct(Long.parseLong(pId));
                                    try {
                                        if (product == null) throw new ProductNotFoundException();
                                    }catch (ProductNotFoundException e){
                                        System.out.println("ProductNotFoundException 발생. 상품을 찾을 수 없습니다.");
                                        continue ;
                                    }
                                }
                                System.out.print("수량: ");
                                pQty = scanner.nextLine();
                                if(StringManager.IsEmpty(pQty)){
                                    if(productMap.size() > 0){
                                        System.out.println("------------------------------------------------------------");
                                        orderService.saveOrder(productMap);
                                        productMap.clear();
                                        break outer;
                                    }else{
                                        System.out.println("주문 정보가 없습니다.");
                                    }
                                }else{
                                    convertQty = Integer.parseInt(pQty);

                                    Integer finalConvertQty = convertQty;

                                    BigDecimal priceResult = product.getPrice().multiply(new BigDecimal(finalConvertQty));

                                    ProductEntity finalProduct = product;
                                    productMap.compute(convertId, (key, oldValue) -> {
                                        BigDecimal totalPrice = (oldValue != null) ? oldValue.getTotalPrice().add(priceResult) : priceResult;
                                        Integer orderQty = (oldValue != null) ? oldValue.getOrderQty() + finalConvertQty : finalConvertQty;
                                        return ProductDto.builder()
                                                .id(finalProduct.getProductId())
                                                .name(finalProduct.getName())
                                                .totalPrice(totalPrice)
                                                .orderQty(orderQty)
                                                .build();
                                    });
                                }

                            }catch (NumberFormatException e) {
                                log.error("유효하지 않은 숫자입니다. 다시 시도해주세요.");
                            }catch (IllegalArgumentException e) {
                                log.error("타입 변환 오류가 발생했습니다.");
                            }

                        }
                    }else{
                        System.out.println("유효하지 않은 명령어 입니다.");
                    }
                }
            }
            else {
                log.error("DB INIT Failed...");
            }
            
        } 
       
    }

}
