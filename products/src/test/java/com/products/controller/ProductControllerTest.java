package com.products.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.products.dto.ProductDto;
import com.products.model.Product;
import com.products.repository.ProductRepository;
import com.products.service.ProductService;
import com.products.service.ProductServiceImplementation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.ContentResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ProductController.class, AppErrorHandler.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ProductControllerTest {
    @Autowired
    private AppErrorHandler appErrorHandler;

    @Autowired
    private ProductController productController;

    @MockBean
    private ProductService productService;

    /**
     * Test {@link ProductController#getAllProducts()}.
     * <p>
     * Method under test: {@link ProductController#getAllProducts()}
     */
    @Test
    @DisplayName("Test getAllProducts()")
    void testGetAllProducts() throws Exception {
        // Arrange
        when(productService.getAllProducts()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(appErrorHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link ProductController#getBySkuCode(String)}.
     * <ul>
     *   <li>Then content string {@code []}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductController#getBySkuCode(String)}
     */
    @Test
    @DisplayName("Test getBySkuCode(String); then content string '[]'")
    void testGetBySkuCode_thenContentStringLeftSquareBracketRightSquareBracket() throws Exception {
        // Arrange
        when(productService.getAllProducts()).thenReturn(new ArrayList<>());
        when(productService.getBySkuName(Mockito.<String>any())).thenReturn(new ProductDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products/{skuCode}", "",
                "Uri Variables");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(appErrorHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link ProductController#getBySkuCode(String)}.
     * <p>
     * Method under test: {@link ProductController#getBySkuCode(String)}
     */
    @Test
    @DisplayName("Test getBySkuCode(String)")
    void testGetBySkuCode() throws Exception {
        // Arrange
        when(productService.getBySkuName(Mockito.<String>any())).thenReturn(new ProductDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products/{skuCode}", "Sku Code");

        MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(appErrorHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"skuCode\":null,\"productName\":null,\"category\":null,\"price\":null,\"vendor\":null}"));
    }

    /**
     * Test {@link ProductController#createProduct(ProductDto)}.
     * <p>
     * Method under test: {@link ProductController#createProduct(ProductDto)}
     */
    @Test
    @DisplayName("Test createProduct(ProductDto)")
    void testCreateProduct() throws Exception {
        when(productService.createProduct(Mockito.<ProductDto>any())).thenReturn(new ProductDto());

        ProductDto productDto = new ProductDto();
        productDto.setCategory("Category");
        productDto.setPrice(new BigDecimal("2.3"));
        productDto.setProductName("Product Name");
        productDto.setSkuCode("Sku Code");
        productDto.setVendor("Vendor");
        String content = (new ObjectMapper()).writeValueAsString(productDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(appErrorHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"skuCode\":null,\"productName\":null,\"category\":null,\"price\":null,\"vendor\":null}"));
    }

    /**
     * Test {@link ProductController#updateProduct(String, ProductDto)}.
     * <p>
     * Method under test: {@link ProductController#updateProduct(String, ProductDto)}
     */
    @Test
    @DisplayName("Test updateProduct(String, ProductDto)")
    void testUpdateProduct() throws Exception {
        when(productService.updateProduct(Mockito.<String>any(), Mockito.<ProductDto>any())).thenReturn(new ProductDto());

        ProductDto productDto = new ProductDto();
        productDto.setCategory("Category");
        productDto.setPrice(new BigDecimal("2.3"));
        productDto.setProductName("Product Name");
        productDto.setSkuCode("Sku Code");
        productDto.setVendor("Vendor");
        String content = (new ObjectMapper()).writeValueAsString(productDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/products/{skuCode}", "Sku Code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(appErrorHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"skuCode\":null,\"productName\":null,\"category\":null,\"price\":null,\"vendor\":null}"));
    }

    /**
     * Test {@link ProductController#deleteProduct(String)}.
     * <ul>
     *   <li>Given {@link Product#Product()} Category is {@code Category}.</li>
     *   <li>Then calls {@link ProductRepository#findBySkuCode(String)}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductController#deleteProduct(String)}
     */
    @Test
    @DisplayName("Test deleteProduct(String); given Product() Category is 'Category'; then calls findBySkuCode(String)")
    void testDeleteProduct_givenProductCategoryIsCategory_thenCallsFindBySkuCode() {


        Product product = new Product();
        product.setCategory("Category");
        product.setId(ObjectId.get());
        product.setPrice(new BigDecimal("2.3"));
        product.setProductName("Product Name");
        product.setSkuCode("Sku Code");
        product.setVendor("Vendor");
        Optional<Product> ofResult = Optional.of(product);
        ProductRepository productRepository = mock(ProductRepository.class);
        doNothing().when(productRepository).delete(Mockito.<Product>any());
        when(productRepository.findBySkuCode(Mockito.<String>any())).thenReturn(ofResult);

        (new ProductController(new ProductServiceImplementation(productRepository))).deleteProduct("Sku Code");

        verify(productRepository).findBySkuCode(eq("Sku Code"));
        verify(productRepository).delete(isA(Product.class));
    }

    /**
     * Test {@link ProductController#productExists(String)}.
     * <ul>
     *   <li>Then content string {@link Boolean#FALSE} toString.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductController#productExists(String)}
     */
    @Test
    @DisplayName("Test productExists(String); then content string FALSE toString")
    void testProductExists_thenContentStringFalseToString() throws Exception {
        when(productService.productExists(Mockito.<String>any())).thenReturn(false);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products/{skuCode}/exists",
                "Sku Code");

        ResultActions resultActions = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(appErrorHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string(Boolean.FALSE.toString()));
    }

    /**
     * Test {@link ProductController#productExists(String)}.
     * <ul>
     *   <li>Then content string {@link Boolean#TRUE} toString.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductController#productExists(String)}
     */
    @Test
    @DisplayName("Test productExists(String); then content string TRUE toString")
    void testProductExists_thenContentStringTrueToString() throws Exception {
        when(productService.productExists(Mockito.<String>any())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/products/{skuCode}/exists",
                "Sku Code");

        ResultActions resultActions = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(appErrorHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
        ContentResultMatchers contentResult = MockMvcResultMatchers.content();
        resultActions.andExpect(contentResult.string(Boolean.TRUE.toString()));
    }
}
