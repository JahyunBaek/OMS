package ko.co._29cm.homework.oms.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ko.co._29cm.homework.oms.Entity.ProductEntity;
import ko.co._29cm.homework.oms.Repository.OrderRepository;
import ko.co._29cm.homework.oms.Util.CsvManager;
import ko.co._29cm.homework.oms.Util.FileManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

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

    public Optional<ProductEntity> getProduct(Long producId){
        return orderRepository.findById(producId);
    }
}
