package ko.co._29cm.homework.oms.Dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal totalPrice;
    private Integer orderQty;
}
