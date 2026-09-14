package tutar.techs.mymerch.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor
@Builder 
public class OrderRequest {
    @NotBlank (message = "Customer name cannot be null")
    private String customerName;

    @NotBlank (message = "Customer email cannot be null")
    @Email (message = "Customer email should be valid")
    private String customerEmail;

    @Valid
    @NotEmpty(message = "Order items cannot be null")
    private List<OrderItemRequest> orderItems;
    
}
