package tutar.techs.mymerch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tutar.techs.mymerch.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // Custom query methods can be defined here if needed
    
}
