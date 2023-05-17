package ko.co._29cm.homework.oms.Runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CmdRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // 콘솔 애플리케이션의 로직을 구현합니다.
        System.out.println("Hello, Console Application!");
        // 추가적인 로직을 작성합니다.
    }
}
