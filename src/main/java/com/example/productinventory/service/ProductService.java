package com.example.productinventory.service;

import com.example.productinventory.entities.Product;
import com.example.productinventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> createProductsBulk (List<Product> products){
        return productRepository.saveAll(products);
    }

    public Page<Product> getAllProducts(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    public Optional<Product> getProductById (Long id){
        return productRepository.findById(id);
    }

    public Optional<Product> updateProduct (Long id, Product productInfo){
        return productRepository.findById(id)
                .map(
                        product -> {
                            if(!product.getVersion().equals(productInfo.getVersion())){
                                throw new OptimisticLockingFailureException("The product has been modified by another transaction");
                            }
                            product.setName(productInfo.getName());
                            product.setDescription(productInfo.getDescription());
                            product.setPrice(productInfo.getPrice());
                            product.setQuantity(productInfo.getQuantity());
                            //product.setVersion(productInfo.getVersion());
                            return productRepository.save(product);
                        }
                );
    }

    public Optional<Product> deleteProduct (Long id){
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(productRepository::delete);
        return product;
    }


    //Products Filter
    public Page<Product> filterProductsByName (String name, Pageable pageable){
        return productRepository.findByName(name, pageable);
    }

    public Page<Product> filterProductsByPrice (BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable){
        return productRepository.findByPrice(minPrice, maxPrice, pageable);
    }

    public Page<Product> filterSortProducts(String name, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        if (name != null && minPrice != null && maxPrice != null) {
            return productRepository.findByNamePrice(name, minPrice, maxPrice, pageable);
        } else if (name != null) {
            return filterProductsByName(name, pageable);
        } else if (minPrice != null && maxPrice != null) {
            return filterProductsByPrice(minPrice, maxPrice, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }
}
