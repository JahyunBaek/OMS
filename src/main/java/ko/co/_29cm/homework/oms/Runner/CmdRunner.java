package ko.co._29cm.homework.oms.Runner;

import java.util.Optional;
import java.util.Scanner;

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
            System.out.println("Please Input File Name : ");
            //String actualInput = scanner.nextLine();
            String pId;
            String pQty;
            String actualInput = "";
            if(orderService.InitDB(actualInput)){
                log.info("DB INIT Completed...");
                         
                while (true) {
                    System.out.println("입력(o[order]): 주문, q[quit]: 종료) :");
                    actualInput = scanner.nextLine();
                    if(actualInput.equals("q") || actualInput.equals("quit")){
                        System.out.println("시스템을 종료합니다.");
                        break;
                    }else if(actualInput.equals("o")){
                        
                        System.out.println("상품번호: ");
                        pId = scanner.nextLine();
                        StringManager.IsEmpty(pId);
                        System.out.println("수량: ");
                        pQty = scanner.nextLine();
                        StringManager.IsEmpty(pQty);
                        //Optional<ProductEntity> productInfo =  orderService.getProduct(Long.parseLong(actualInput));
                        
                    }else{
                        System.out.println("'q'또는 'o'를 입력해 주세요.");
                    }
                }
            }
            else {
                log.error("DB INIT Failed...");
            }
            
        } 
       
    }

}
