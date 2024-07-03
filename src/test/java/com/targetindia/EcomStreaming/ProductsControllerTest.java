package com.targetindia.EcomStreaming;

import com.targetindia.EcomStreaming.controllers.ProductsController;
import com.targetindia.EcomStreaming.entites.Products;
import com.targetindia.EcomStreaming.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProductsControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ProductsController productsController;

    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productsController).build();
    }

    @Test
    void testFetchProductsList() throws Exception {
        Products product1 = new Products(1L, "Product 1", 20L, 10.0);
        Products product2 = new Products(2L, "Product 2", 30L, 15.0);

        List<Products> productsList = Arrays.asList(product1, product2);
        when(productService.displayAllProducts()).thenReturn(productsList);

        mockMvc.perform(get("/api/v1/target/allProducts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].productID").value(1))
                .andExpect(jsonPath("$[0].productName").value("Product 1"))
                .andExpect(jsonPath("$[0].stockLevel").value(20))
                .andExpect(jsonPath("$[0].productPrice").value(10.0))
                .andExpect(jsonPath("$[1].productID").value(2))
                .andExpect(jsonPath("$[1].productName").value("Product 2"))
                .andExpect(jsonPath("$[1].stockLevel").value(30))
                .andExpect(jsonPath("$[1].productPrice").value(15.0));

        verify(productService, times(1)).displayAllProducts();
    }
}
