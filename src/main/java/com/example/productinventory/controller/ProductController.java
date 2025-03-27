package com.example.productinventory.controller;

import com.example.productinventory.entities.Product;
import com.example.productinventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Product Inventory", description = "API for managing product inventory")
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //Method for single creation of product
    @Operation(summary = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(@Parameter(description = "Product to be created", required = true) @Valid @RequestBody Product product){
        Product createdProd = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProd);
    }

    //Method for bulk creation of products
    @Operation(summary = "Create products in bulk")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Product.class)),
                            examples = @ExampleObject(value = "[{\"name\": \"Pikachu Plushie\", \"description\": \"Electric pokémon\", \"price\": \"9.99\", \"quantity\": \"10\"}, {\"name\": \"Charmander Plushie\", \"description\": \"Fire pokémon\", \"price\": \"10.99\", \"quantity\": \"15\"}]"))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/bulk")
    public ResponseEntity<List<Product>> createProductsBulk (@Parameter(description = "List of Product to be created", required = true) @Valid @RequestBody List<Product> products){
        List<Product> bulkProducts = productService.createProductsBulk(products);
        return ResponseEntity.status(HttpStatus.CREATED).body(bulkProducts);
    }

    //Method for getting all products created
    @Operation(summary = "Retrieve all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<PagedModel<Product>> getAllProducts(@Parameter(description = "Pagination information", required = false) Pageable pageable){
        Page<Product> productPage = productService.getAllProducts(pageable);
        PagedModel<Product> pagedModel = new PagedModel<>(productPage);
        return ResponseEntity.ok(pagedModel);
    }

    //Method for retrieve a product by Id
    @Operation(summary = "Retrieve product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById (@Parameter(description = "ID of the Product to retrieve", required = true) @PathVariable Long id){
        return productService.getProductById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    //Method for update a product
    @Operation(summary = "Update an existing product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "409", description = "Conflict due to optimistic locking")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct (@Parameter(description = "ID of the Product to update", required = true) @PathVariable Long id,
                                                  @Parameter(description = "Updated product info", required = true) @Valid @RequestBody Product productInfo){
        try{
            return productService.updateProduct(id, productInfo).map(ResponseEntity::ok)
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }catch (OptimisticLockingFailureException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

    }

    //Method for delete a product
    @Operation(summary = "Delete product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct (@Parameter(description = "ID of the Product to delete", required = true) @PathVariable Long id){
        Optional<Product> product = productService.deleteProduct(id);
        if(product.isPresent()){
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
