package ko.co._29cm.homework.oms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ko.co._29cm.homework.oms.Entity.ProductEntity;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;


public interface OrderRepository extends JpaRepository<ProductEntity,Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    ProductEntity findByProductId(Long productId);

}