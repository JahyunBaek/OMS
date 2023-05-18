package ko.co._29cm.homework.oms.Runner;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ko.co._29cm.homework.oms.Service.OrderService;
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
            String actualInput = scanner.nextLine();

            if(orderService.InitDB(actualInput)){
                log.info("DB INIT Completed...");
                         
                while (true) {
                    System.out.println("q 또는 r 을 입력하세요 ===>");
                    actualInput = scanner.nextLine();
                    if(actualInput.equals("q")){
                        System.out.println("시스템을 종료합니다.");
                        break;
                    }else if(actualInput.equals("r")){

                    }else{

                    }
                }
            }
            else {
                log.error("DB INIT Failed...");
            }
            
        }
        
       
    }
}
