package com.targetindia.EcomStreaming;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.targetindia.EcomStreaming.controllers.ProductsController;
import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.service.ProductService;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductsController.class)
public class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private EntityManagerFactory entityManagerFactory;


    @Test
    public void testFetchProductsList() throws Exception {

        Products product1 = new Products();
        product1.setProductID(1L);
        product1.setProductName("Product1");

        Products product2 = new Products();
        product2.setProductID(2L);
        product2.setProductName("Product2");

        List<Products> productsList = Arrays.asList(product1, product2);
        when(productService.displayAllProducts()).thenReturn(productsList);

        ObjectMapper objectMapper = new ObjectMapper();
        String productsJson = objectMapper.writeValueAsString(productsList);

        mockMvc.perform(get("/api/v1/target/allProducts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(productsJson));
    }
}
