package tutar.techs.mymerch.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tutar.techs.mymerch.entities.Product;
import tutar.techs.mymerch.repositories.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

     public Product createProduct(Product product) {
        // Logic to create a product
        return productRepository.save(product);
    }
    
    public Product updateProduct(Long Id, Product Product) {
        // Logic to update a product

        Product existingProduct = productRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + Id));
        
        existingProduct.setName(Product.getName());
        existingProduct.setDescription(Product.getDescription());
        existingProduct.setCategory(Product.getCategory());
        existingProduct.setPrice(Product.getPrice());
        existingProduct.setStockQuantity(Product.getStockQuantity());

        return productRepository.save(existingProduct);
    }

    public List<Product> getProducts() {
        // Logic to get a product by ID
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        // Logic to get a product by ID
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public void deleteProduct(Long id) {
        // Logic to delete a product by ID
        productRepository.deleteById(id);
    }

}
