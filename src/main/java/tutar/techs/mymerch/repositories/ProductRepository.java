package tutar.techs.mymerch.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tutar.techs.mymerch.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Custom query methods can be defined here if needed

    
}
