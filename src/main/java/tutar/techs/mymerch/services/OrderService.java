package tutar.techs.mymerch.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import tutar.techs.mymerch.entities.Order;
import jakarta.transaction.Transactional;
import lombok.*;
import tutar.techs.mymerch.dto.OrderItemRequest;
import tutar.techs.mymerch.dto.OrderRequest;
import tutar.techs.mymerch.entities.OrderItem;
import tutar.techs.mymerch.entities.Product;
import tutar.techs.mymerch.repositories.OrderRepository;
import tutar.techs.mymerch.repositories.ProductRepository;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional 
    public Order createOrder(OrderRequest orderRequest) {
            // Logic to create an order

            List<OrderItem> orderItems = new ArrayList<>();
            BigDecimal totalPrice = BigDecimal.ZERO;

            Order order = new Order();
            order.setCustomerName(orderRequest.getCustomerName());
            order.setCustomerEmail(orderRequest.getCustomerEmail());
            order.setStatus("CONFIRMED");
            
            for (OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.getProductId()));

                //Check the product stock
                if (product.getStockQuantity() < itemRequest.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getName());
                }

                //calculate the total price
                BigDecimal priceOfItem = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

                totalPrice = totalPrice.add(priceOfItem);

                //Update the product stock
                product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
                productRepository.save(product);

                //Build pattern to make obj

                OrderItem orderItem = OrderItem.builder()
                        .product(product)
                        .quantity(itemRequest.getQuantity())
                        .priceAtPurchase(product.getPrice())
                        .order(order)
                        .build();

                orderItems.add(orderItem);
            }

            order.setTotalPrice(totalPrice);
            order.setOrderItems(orderItems);

            return orderRepository.save(order);
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
}
