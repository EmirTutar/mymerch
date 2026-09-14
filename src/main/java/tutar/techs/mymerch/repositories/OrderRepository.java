package tutar.techs.mymerch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tutar.techs.mymerch.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Custom query methods can be defined here if needed
    
}
