package ko.co._29cm.homework.oms.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import ko.co._29cm.homework.oms.Entity.ProductEntity;
import ko.co._29cm.homework.oms.Repository.OrderRepository;
import ko.co._29cm.homework.oms.Util.CsvManager;
import ko.co._29cm.homework.oms.Util.FileManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderService;

    public boolean InitDB(String csvFileName){
        
        FileManager manager = new CsvManager();

        List<ProductEntity> productList = manager.ReadFile(csvFileName);
        
        if(productList == null || productList.size() == 0)
            return false;
        else{
            orderService.saveAll(productList);
            return true;
        }
    }
}
