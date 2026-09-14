package tutar.techs.mymerch.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name = "products")
@Getter 
@Setter
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name cannot be blank")
    @Column (nullable = false)
    private String name;

    private String description;
    
    private String category;
    

    @NotNull (message = "Price cannot be null")
    @DecimalMin (value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Column (nullable = false)
    private BigDecimal price;

    @NotNull (message = "Stock quantity cannot be null")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Column (name = "stock_quantity", nullable = false)
    private Integer stockQuantity; 
    
    @JsonIgnore 
    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;


}

