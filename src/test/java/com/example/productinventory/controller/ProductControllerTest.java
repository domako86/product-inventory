package com.example.productinventory.controller;

import com.example.productinventory.entities.Product;
import com.example.productinventory.security.config.JwtAuthenticationFilter;
import com.example.productinventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    private List<Product> products;

    @BeforeEach
    void setUp(){
        products = Arrays.asList(
                new Product(null, "Pikachu Plushie", "Electric pokémon", new BigDecimal("9.99"), 10),
                new Product(null, "Charmander Plushie", "Fire pokémon", new BigDecimal("10.99"), 15),
                new Product(null, "Squirtle Plushie", "Water pokémon", new BigDecimal("11.99"), 20),
                new Product(null, "Bulbasaur Plushie", "Grass pokémon", new BigDecimal("12.99"), 25),
                new Product(null, "Jigglypuff Plushie", "Fairy pokémon", new BigDecimal("13.99"), 30),
                new Product(null, "Meowth Plushie", "Normal pokémon", new BigDecimal("14.99"), 35),
                new Product(null, "Psyduck Plushie", "Water pokémon", new BigDecimal("15.99"), 40),
                new Product(null, "Snorlax Plushie", "Normal pokémon", new BigDecimal("16.99"), 45),
                new Product(null, "Eevee Plushie", "Normal pokémon", new BigDecimal("17.99"), 50),
                new Product(null, "Vulpix Plushie", "Fire pokémon", new BigDecimal("18.99"), 55)
        );
    }

    @Test
    void createdProductOk() throws Exception{
        when (productService.createProduct(any(Product.class))).thenReturn(products.get(0));
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Pikachu Plushie\", \"description\": \"Electric pokémon\", \"price\": \"9.99\", \"quantity\": 10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pikachu Plushie"))
                .andExpect(jsonPath("$.description").value("Electric pokémon"))
                .andExpect(jsonPath("$.price").value(9.99))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void createdProductFail() throws Exception{
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"\", \"description\": \"\", \"price\": \"\", \"quantity\": \"\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void createProductsBulkOk() throws Exception{
        when(productService.createProductsBulk(any(List.class))).thenReturn(products.subList(0, 2));
        mockMvc.perform(post("/products/bulk")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content("[{\"name\": \"Pikachu Plushie\", \"description\": \"Electric pokémon\", \"price\": \"9.99\", \"quantity\": 10}," +
                           "{\"name\": \"Charmander Plushie\", \"description\": \"Fire pokémon\", \"price\": \"10.99\", \"quantity\": 15}]"))
                 .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].name").value("Pikachu Plushie"))
                .andExpect(jsonPath("$[0].description").value("Electric pokémon"))
                .andExpect(jsonPath("$[0].price").value(9.99))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[1].name").value("Charmander Plushie"))
                .andExpect(jsonPath("$[1].description").value("Fire pokémon"))
                .andExpect(jsonPath("$[1].price").value(10.99))
                .andExpect(jsonPath("$[1].quantity").value(15));
    }

    @Test
    void createdProductsBulkFail() throws Exception{
        mockMvc.perform(post("/products/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"name\": \"\", \"description\": \"\", \"price\": \"\", \"quantity\": \"\"}," +
                                  "{\"name\": \"\", \"description\": \"\", \"price\": \"\", \"quantity\": \"\"}]"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllProductsOk() throws Exception{
        //Pagination test with 5 elements
        Page<Product> productPage = new PageImpl<>(products.subList(0, 5));
        when(productService.getAllProducts(any(Pageable.class))).thenReturn(productPage);
        mockMvc.perform(get("/products")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Pikachu Plushie"))
                .andExpect(jsonPath("$.content[0].description").value("Electric pokémon"))
                .andExpect(jsonPath("$.content[0].price").value(9.99))
                .andExpect(jsonPath("$.content[0].quantity").value(10))
                .andExpect(jsonPath("$.content[1].name").value("Charmander Plushie"))
                .andExpect(jsonPath("$.content[1].description").value("Fire pokémon"))
                .andExpect(jsonPath("$.content[1].price").value(10.99))
                .andExpect(jsonPath("$.content[1].quantity").value(15))
                .andExpect(jsonPath("$.content[2].name").value("Squirtle Plushie"))
                .andExpect(jsonPath("$.content[2].description").value("Water pokémon"))
                .andExpect(jsonPath("$.content[2].price").value(11.99))
                .andExpect(jsonPath("$.content[2].quantity").value(20))
                .andExpect(jsonPath("$.content[3].name").value("Bulbasaur Plushie"))
                .andExpect(jsonPath("$.content[3].description").value("Grass pokémon"))
                .andExpect(jsonPath("$.content[3].price").value(12.99))
                .andExpect(jsonPath("$.content[3].quantity").value(25))
                .andExpect(jsonPath("$.content[4].name").value("Jigglypuff Plushie"))
                .andExpect(jsonPath("$.content[4].description").value("Fairy pokémon"))
                .andExpect(jsonPath("$.content[4].price").value(13.99))
                .andExpect(jsonPath("$.content[4].quantity").value(30));
    }

    @Test
    void getProductByIdOk() throws Exception{
        when (productService.getProductById(1L)).thenReturn(Optional.of(products.get(0)));
        mockMvc.perform(get("/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pikachu Plushie"))
                .andExpect(jsonPath("$.description").value("Electric pokémon"))
                .andExpect(jsonPath("$.price").value(9.99))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void getProductByIdNotFound() throws Exception{
        when (productService.getProductById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/products/1")
                 .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProductOk() throws Exception{
        Product updatedProduct =  new Product(1L, "Pikachu Plushie Upd", "Electric pokémon upd", new BigDecimal("19.99"), 110);
        updatedProduct.setVersion(1);
        when(productService.updateProduct(eq(1L),any(Product.class))).thenReturn(Optional.of(updatedProduct));
        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Pikachu Plushie Upd\", \"description\": \"Electric pokémon upd\", \"price\": \"19.99\", \"quantity\": 110}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pikachu Plushie Upd"))
                .andExpect(jsonPath("$.description").value("Electric pokémon upd"))
                .andExpect(jsonPath("$.price").value(19.99))
                .andExpect(jsonPath("$.quantity").value(110));

    }

    @Test
    void updateProductConflict() throws Exception{
        when(productService.updateProduct(eq(1L),any(Product.class))).thenThrow(new OptimisticLockingFailureException("The product has been modified by another transaction"));
        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Pikachu Plushie Upd\", \"description\": \"Electric pokémon upd\", \"price\": \"19.99\", \"quantity\": 110}"))
                .andExpect(status().isConflict());
    }

    @Test
    void updateProductNotFound() throws Exception{
        when(productService.updateProduct(eq(1L),any(Product.class))).thenReturn(Optional.empty());
        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"Pikachu Plushie Upd\", \"description\": \"Electric pokémon upd\", \"price\": \"19.99\", \"quantity\": 110}"))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteProductOk() throws Exception{
        when(productService.deleteProduct(1L)).thenReturn(Optional.of(products.get(0)));
        mockMvc.perform(delete("/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProductNotFound() throws Exception{
        when(productService.deleteProduct(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/products/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
