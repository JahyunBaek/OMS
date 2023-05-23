package ko.co._29cm.homework.oms.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ko.co._29cm.homework.oms.Dto.ProductDto;
import ko.co._29cm.homework.oms.Exception.ProductNotFoundException;
import ko.co._29cm.homework.oms.Exception.SoldOutException;
import ko.co._29cm.homework.oms.Util.StringManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ko.co._29cm.homework.oms.Entity.ProductEntity;
import ko.co._29cm.homework.oms.Repository.OrderRepository;
import ko.co._29cm.homework.oms.Util.CsvManager;
import ko.co._29cm.homework.oms.Util.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${Price.deliveryFee}")
    private BigDecimal deliveryFee;

    private final OrderRepository orderRepository;

    private static String defaultPath = "src/main/resources/sample//";
    public boolean InitDB(String csvFileName){
        
        FileManager manager = new CsvManager();

        List<ProductEntity> productList = manager.ReadFile(defaultPath+csvFileName);
        
        if(productList == null || productList.size() == 0)
            return false;
        else{
            orderRepository.saveAll(productList);
            return true;
        }
    }

    public ProductEntity getProduct(Long i){
        return orderRepository.findByProductId(i);
    }

    public List<ProductEntity> getAllProduct(){
        return orderRepository.findAll();
    }

    @Transactional
    public boolean saveOrder(Map<Long,ProductDto> productMap){

        BigDecimal sumPrice = new BigDecimal(0);

        for (Map.Entry<Long, ProductDto> entry : productMap.entrySet()) {
            Long id = entry.getKey();
            ProductEntity product = orderRepository.findByProductId(id);
            Integer calcQty = product.getQty() - entry.getValue().getOrderQty();
            try {
                if (calcQty < 0) throw new SoldOutException();
            }catch (SoldOutException e){
                System.out.println("SoldOutException 발생. 주문한상품량이 재고량보다 큽니다. [상풍명 : "
                        +product.getName()+"], 재고량["+product.getQty() +"]");
                return false;
            }

            product.setQty(calcQty);

            ProductEntity save = orderRepository.save(product);

            sumPrice = sumPrice.add(entry.getValue().getTotalPrice());
            System.out.println(entry.getValue().getName()+"-"+entry.getValue().getOrderQty()+"개");


            System.out.println("------------------------------------------------------------");
            System.out.println("주문금액: "+ StringManager.getFormatStr(sumPrice) +"원");


            if(sumPrice.compareTo(new BigDecimal(50000)) < 0){
                System.out.println("배송비: "+ StringManager.getFormatStr(deliveryFee) +"원");
                sumPrice = sumPrice.add(deliveryFee);
            }

            System.out.println("------------------------------------------------------------");
            System.out.println("결제금액: "+ StringManager.getFormatStr(sumPrice) +"원");
            System.out.println("------------------------------------------------------------");
        }
        return true;
    }
}
