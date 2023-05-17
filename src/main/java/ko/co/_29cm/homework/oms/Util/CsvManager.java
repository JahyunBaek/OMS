package ko.co._29cm.homework.oms.Util;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import ko.co._29cm.homework.oms.Entity.Product;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class CsvManager {

    public static List<Product> ReadCsvFile(){
        String csvFile = "data.csv";

        try {
            Path path = Paths.get(csvFile);

            return Files.lines(path)
                    .skip(1) // 첫 번째 행은 헤더이므로 건너뜀
                    .map(line -> line.split(","))
                    .map(tokens -> Product.builder()
                    .id(Long.parseLong(tokens[0]))
                    .name(tokens[1])
                    .price(new BigDecimal(tokens[2]))
                    .qty(Integer.valueOf(tokens[3]))
                    .build())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }     
    }

}