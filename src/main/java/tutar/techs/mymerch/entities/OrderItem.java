package tutar.techs.mymerch.entities;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;

@Entity
@Table (name = "order_items")
@Getter 
@Setter
@AllArgsConstructor 
@NoArgsConstructor 
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Quantity cannot be null")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Price at purchase cannot be null")
    @Column(name = "price_at_purchase", nullable = false)
    private BigDecimal priceAtPurchase;

    @JsonBackReference
    @ManyToOne 
    @JoinColumn (name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn (name = "product_id", nullable = false)
    private Product product;
}
