package tutar.techs.mymerch.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import tutar.techs.mymerch.entities.Product;
import tutar.techs.mymerch.services.ProductService;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    
    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product) {
        // Logic to create a product
        return productService.createProduct(product);
    }
    
    @PutMapping("/{id}") 
    public Product updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        // Logic to update a product
        return productService.updateProduct(id, product);
    }

    @GetMapping 
    public List<Product> getProducts() {
        // Logic to get a product by ID
        return productService.getProducts();
    }

    @GetMapping ("/{id}")
    public Product getProductById(@PathVariable Long id) {
        // Logic to get a product by ID
        return productService.getProductById(id);
    }

    @DeleteMapping ("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        // Logic to delete a product by ID
        productService.deleteProduct(id);
    }

}
