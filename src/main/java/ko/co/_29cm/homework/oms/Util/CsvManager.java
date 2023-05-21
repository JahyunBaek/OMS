package ko.co._29cm.homework.oms.Util;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import ko.co._29cm.homework.oms.Entity.ProductEntity;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class CsvManager implements FileManager{

    public List<ProductEntity> ReadFile(String csvFileName){
        
        try {
            Path path = Paths.get(csvFileName);
    
            return Files.lines(path)
                    .skip(1)
                    .map(line -> line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"))
                    .map(tokens -> ProductEntity.builder()
                    .productId(Long.parseLong(tokens[0]))
                    .name(tokens[1])
                    .price(new BigDecimal(tokens[2]))
                    .qty(Integer.valueOf(tokens[3]))
                    .build())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error(csvFileName, e);
            return null;
        }     
    }

}