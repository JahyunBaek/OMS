package ko.co._29cm.homework.oms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ko.co._29cm.homework.oms.Entity.ProductEntity;
import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<ProductEntity,Long>{
    
    ProductEntity findByProductId(Long productId);

}