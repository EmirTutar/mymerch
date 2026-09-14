package tutar.techs.mymerch.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
@Builder 
public class OrderItemRequest {
    @NotNull (message = "Product ID cannot be null")
    private Long productId;
    
    @NotNull (message = "Quantity cannot be null")
    @Min (value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
