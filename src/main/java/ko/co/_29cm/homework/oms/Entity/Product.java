package ko.co._29cm.homework.oms.Entity;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer qty;
}
