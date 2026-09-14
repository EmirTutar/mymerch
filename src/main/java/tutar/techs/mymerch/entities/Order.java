package tutar.techs.mymerch.entities;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table (name = "orders")
@Getter 
@Setter
@AllArgsConstructor 
@NoArgsConstructor 
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name cannot be blank")
    @Column (name = "customer_name", nullable = false)
    private String customerName;

    @NotBlank(message = "Customer email cannot be blank")
    @Column (name = "customer_email", nullable = false)
    private String customerEmail;

    @NotBlank(message = "Order status cannot be blank")
    @Column (name = "status", nullable = false)
    private String status;

    @NotNull (message = "Total price cannot be null")
    @DecimalMin (value = "0.0", inclusive = false, message = "Total price must be greater than zero")
    @Column (name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @OneToMany (mappedBy = "order")
    private List<OrderItem> orderItems;

    @Column (name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist 
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
