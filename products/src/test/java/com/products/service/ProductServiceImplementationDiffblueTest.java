package com.products.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.products.dto.ProductDto;
import com.products.exceptions.ProductNotFoundException;
import com.products.model.Product;
import com.products.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProductServiceImplementation.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ProductServiceImplementationDiffblueTest {
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceImplementation productServiceImplementation;

    /**
     * Test {@link ProductServiceImplementation#getAllProducts()}.
     * <ul>
     *   <li>Given {@link Product#Product()} Category is {@code Category}.</li>
     *   <li>Then return size is one.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#getAllProducts()}
     */
    @Test
    @DisplayName("Test getAllProducts(); given Product() Category is 'Category'; then return size is one")
    @Tag("MaintainedByDiffblue")
    void testGetAllProducts_givenProductCategoryIsCategory_thenReturnSizeIsOne() {
        // Arrange
        Product product = new Product();
        product.setCategory("Category");
        product.setId(ObjectId.get());
        product.setPrice(new BigDecimal("2.3"));
        product.setProductName("Product Name");
        product.setSkuCode("Sku Code");
        product.setVendor("Vendor");

        ArrayList<Product> productList = new ArrayList<>();
        productList.add(product);
        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<ProductDto> actualAllProducts = productServiceImplementation.getAllProducts();

        // Assert
        verify(productRepository).findAll();
        assertEquals(1, actualAllProducts.size());
        ProductDto getResult = actualAllProducts.get(0);
        assertEquals("Category", getResult.getCategory());
        assertEquals("Product Name", getResult.getProductName());
        assertEquals("Sku Code", getResult.getSkuCode());
        assertEquals("Vendor", getResult.getVendor());
    }

    /**
     * Test {@link ProductServiceImplementation#getAllProducts()}.
     * <ul>
     *   <li>Given {@link ProductRepository} {@link ListCrudRepository#findAll()} return {@link ArrayList#ArrayList()}.</li>
     *   <li>Then return Empty.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#getAllProducts()}
     */
    @Test
    @DisplayName("Test getAllProducts(); given ProductRepository findAll() return ArrayList(); then return Empty")
    @Tag("MaintainedByDiffblue")
    void testGetAllProducts_givenProductRepositoryFindAllReturnArrayList_thenReturnEmpty() {
        // Arrange
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<ProductDto> actualAllProducts = productServiceImplementation.getAllProducts();

        // Assert
        verify(productRepository).findAll();
        assertTrue(actualAllProducts.isEmpty());
    }

    /**
     * Test {@link ProductServiceImplementation#getAllProducts()}.
     * <ul>
     *   <li>Then return size is two.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#getAllProducts()}
     */
    @Test
    @DisplayName("Test getAllProducts(); then return size is two")
    @Tag("MaintainedByDiffblue")
    void testGetAllProducts_thenReturnSizeIsTwo() {
        // Arrange
        Product product = new Product();
        product.setCategory("Category");
        product.setId(ObjectId.get());
        product.setPrice(new BigDecimal("2.3"));
        product.setProductName("Product Name");
        product.setSkuCode("Sku Code");
        product.setVendor("Vendor");

        Product product2 = new Product();
        product2.setCategory("com.products.model.Product");
        product2.setId(ObjectId.get());
        product2.setPrice(new BigDecimal("2.3"));
        product2.setProductName("com.products.model.Product");
        product2.setSkuCode("com.products.model.Product");
        product2.setVendor("com.products.model.Product");

        ArrayList<Product> productList = new ArrayList<>();
        productList.add(product2);
        productList.add(product);
        when(productRepository.findAll()).thenReturn(productList);

        // Act
        List<ProductDto> actualAllProducts = productServiceImplementation.getAllProducts();

        // Assert
        verify(productRepository).findAll();
        assertEquals(2, actualAllProducts.size());
        ProductDto getResult = actualAllProducts.get(1);
        assertEquals("Category", getResult.getCategory());
        assertEquals("Product Name", getResult.getProductName());
        assertEquals("Sku Code", getResult.getSkuCode());
        assertEquals("Vendor", getResult.getVendor());
        ProductDto getResult2 = actualAllProducts.get(0);
        assertEquals("com.products.model.Product", getResult2.getCategory());
        assertEquals("com.products.model.Product", getResult2.getProductName());
        assertEquals("com.products.model.Product", getResult2.getSkuCode());
        assertEquals("com.products.model.Product", getResult2.getVendor());
        BigDecimal expectedPrice = new BigDecimal("2.3");
        assertEquals(expectedPrice, getResult.getPrice());
    }

    /**
     * Test {@link ProductServiceImplementation#getAllProducts()}.
     * <ul>
     *   <li>Then throw {@link ProductNotFoundException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#getAllProducts()}
     */
    @Test
    @DisplayName("Test getAllProducts(); then throw ProductNotFoundException")
    @Tag("MaintainedByDiffblue")
    void testGetAllProducts_thenThrowProductNotFoundException() {
        // Arrange
        when(productRepository.findAll()).thenThrow(new ProductNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ProductNotFoundException.class, () -> productServiceImplementation.getAllProducts());
        verify(productRepository).findAll();
    }

    /**
     * Test {@link ProductServiceImplementation#getBySkuName(String)}.
     * <p>
     * Method under test: {@link ProductServiceImplementation#getBySkuName(String)}
     */
    @Test
    @DisplayName("Test getBySkuName(String)")
    @Tag("MaintainedByDiffblue")
    void testGetBySkuName() {
        // Arrange
        when(productRepository.findBySkuCode(Mockito.<String>any()))
                .thenThrow(new ProductNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ProductNotFoundException.class, () -> productServiceImplementation.getBySkuName("Sku Name"));
        verify(productRepository).findBySkuCode(eq("Sku Name"));
    }

    /**
     * Test {@link ProductServiceImplementation#getBySkuName(String)}.
     * <ul>
     *   <li>Given {@link Product#Product()} Category is {@code Category}.</li>
     *   <li>Then return {@code Category}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#getBySkuName(String)}
     */
    @Test
    @DisplayName("Test getBySkuName(String); given Product() Category is 'Category'; then return 'Category'")
    @Tag("MaintainedByDiffblue")
    void testGetBySkuName_givenProductCategoryIsCategory_thenReturnCategory() {
        // Arrange
        Product product = new Product();
        product.setCategory("Category");
        product.setId(ObjectId.get());
        product.setPrice(new BigDecimal("2.3"));
        product.setProductName("Product Name");
        product.setSkuCode("Sku Code");
        product.setVendor("Vendor");
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findBySkuCode(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        ProductDto actualBySkuName = productServiceImplementation.getBySkuName("Sku Name");

        // Assert
        verify(productRepository).findBySkuCode(eq("Sku Name"));
        assertEquals("Category", actualBySkuName.getCategory());
        assertEquals("Product Name", actualBySkuName.getProductName());
        assertEquals("Sku Code", actualBySkuName.getSkuCode());
        assertEquals("Vendor", actualBySkuName.getVendor());
        BigDecimal expectedPrice = new BigDecimal("2.3");
        assertEquals(expectedPrice, actualBySkuName.getPrice());
    }

    /**
     * Test {@link ProductServiceImplementation#getBySkuName(String)}.
     * <ul>
     *   <li>Given {@link ProductRepository} {@link ProductRepository#findBySkuCode(String)} return empty.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#getBySkuName(String)}
     */
    @Test
    @DisplayName("Test getBySkuName(String); given ProductRepository findBySkuCode(String) return empty")
    @Tag("MaintainedByDiffblue")
    void testGetBySkuName_givenProductRepositoryFindBySkuCodeReturnEmpty() {
        // Arrange
        Optional<Product> emptyResult = Optional.empty();
        when(productRepository.findBySkuCode(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ProductNotFoundException.class, () -> productServiceImplementation.getBySkuName("Sku Name"));
        verify(productRepository).findBySkuCode(eq("Sku Name"));
    }

    /**
     * Test {@link ProductServiceImplementation#createProduct(ProductDto)}.
     * <ul>
     *   <li>When {@link ProductDto#ProductDto()}.</li>
     *   <li>Then return {@code Category}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#createProduct(ProductDto)}
     */
    @Test
    @DisplayName("Test createProduct(ProductDto); when ProductDto(); then return 'Category'")
    @Tag("MaintainedByDiffblue")
    void testCreateProduct_whenProductDto_thenReturnCategory() {
        // Arrange
        Product product = new Product();
        product.setCategory("Category");
        product.setId(ObjectId.get());
        product.setPrice(new BigDecimal("2.3"));
        product.setProductName("Product Name");
        product.setSkuCode("Sku Code");
        product.setVendor("Vendor");
        when(productRepository.save(Mockito.<Product>any())).thenReturn(product);

        // Act
        ProductDto actualCreateProductResult = productServiceImplementation.createProduct(new ProductDto());

        // Assert
        verify(productRepository).save(isA(Product.class));
        assertEquals("Category", actualCreateProductResult.getCategory());
        assertEquals("Product Name", actualCreateProductResult.getProductName());
        assertEquals("Sku Code", actualCreateProductResult.getSkuCode());
        assertEquals("Vendor", actualCreateProductResult.getVendor());
        BigDecimal expectedPrice = new BigDecimal("2.3");
        assertEquals(expectedPrice, actualCreateProductResult.getPrice());
    }

    /**
     * Test {@link ProductServiceImplementation#updateProduct(String, ProductDto)}.
     * <p>
     * Method under test: {@link ProductServiceImplementation#updateProduct(String, ProductDto)}
     */
    @Test
    @DisplayName("Test updateProduct(String, ProductDto)")
    @Tag("MaintainedByDiffblue")
    void testUpdateProduct() {
        // Arrange
        Product product = new Product();
        product.setCategory("Category");
        product.setId(ObjectId.get());
        product.setPrice(new BigDecimal("2.3"));
        product.setProductName("Product Name");
        product.setSkuCode("Sku Code");
        product.setVendor("Vendor");
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.save(Mockito.<Product>any())).thenThrow(new ProductNotFoundException("An error occurred"));
        when(productRepository.findBySkuCode(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(ProductNotFoundException.class,
                () -> productServiceImplementation.updateProduct("Sku Code", new ProductDto()));
        verify(productRepository).findBySkuCode(eq("Sku Code"));
        verify(productRepository).save(isA(Product.class));
    }

    /**
     * Test {@link ProductServiceImplementation#updateProduct(String, ProductDto)}.
     * <ul>
     *   <li>Given {@link ProductRepository} {@link ProductRepository#findBySkuCode(String)} return empty.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#updateProduct(String, ProductDto)}
     */
    @Test
    @DisplayName("Test updateProduct(String, ProductDto); given ProductRepository findBySkuCode(String) return empty")
    @Tag("MaintainedByDiffblue")
    void testUpdateProduct_givenProductRepositoryFindBySkuCodeReturnEmpty() {
        // Arrange
        Optional<Product> emptyResult = Optional.empty();
        when(productRepository.findBySkuCode(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ProductNotFoundException.class,
                () -> productServiceImplementation.updateProduct("Sku Code", new ProductDto()));
        verify(productRepository).findBySkuCode(eq("Sku Code"));
    }

    /**
     * Test {@link ProductServiceImplementation#updateProduct(String, ProductDto)}.
     * <ul>
     *   <li>Given {@link ProductRepository} {@link CrudRepository#save(Object)} return {@link Product#Product()}.</li>
     *   <li>Then return {@code Category}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#updateProduct(String, ProductDto)}
     */
    @Test
    @DisplayName("Test updateProduct(String, ProductDto); given ProductRepository save(Object) return Product(); then return 'Category'")
    @Tag("MaintainedByDiffblue")
    void testUpdateProduct_givenProductRepositorySaveReturnProduct_thenReturnCategory() {
        // Arrange
        Product product = new Product();
        product.setCategory("Category");
        product.setId(ObjectId.get());
        product.setPrice(new BigDecimal("2.3"));
        product.setProductName("Product Name");
        product.setSkuCode("Sku Code");
        product.setVendor("Vendor");
        Optional<Product> ofResult = Optional.of(product);

        Product product2 = new Product();
        product2.setCategory("Category");
        product2.setId(ObjectId.get());
        product2.setPrice(new BigDecimal("2.3"));
        product2.setProductName("Product Name");
        product2.setSkuCode("Sku Code");
        product2.setVendor("Vendor");
        when(productRepository.save(Mockito.<Product>any())).thenReturn(product2);
        when(productRepository.findBySkuCode(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        ProductDto actualUpdateProductResult = productServiceImplementation.updateProduct("Sku Code", new ProductDto());

        // Assert
        verify(productRepository).findBySkuCode(eq("Sku Code"));
        verify(productRepository).save(isA(Product.class));
        assertEquals("Category", actualUpdateProductResult.getCategory());
        assertEquals("Product Name", actualUpdateProductResult.getProductName());
        assertEquals("Sku Code", actualUpdateProductResult.getSkuCode());
        assertEquals("Vendor", actualUpdateProductResult.getVendor());
        BigDecimal expectedPrice = new BigDecimal("2.3");
        assertEquals(expectedPrice, actualUpdateProductResult.getPrice());
    }

    /**
     * Test {@link ProductServiceImplementation#deleteProduct(String)}.
     * <p>
     * Method under test: {@link ProductServiceImplementation#deleteProduct(String)}
     */
    @Test
    @DisplayName("Test deleteProduct(String)")
    @Tag("MaintainedByDiffblue")
    void testDeleteProduct() {
        // Arrange
        Product product = new Product();
        product.setCategory("Category");
        product.setId(ObjectId.get());
        product.setPrice(new BigDecimal("2.3"));
        product.setProductName("Product Name");
        product.setSkuCode("Sku Code");
        product.setVendor("Vendor");
        Optional<Product> ofResult = Optional.of(product);
        doThrow(new ProductNotFoundException("An error occurred")).when(productRepository).delete(Mockito.<Product>any());
        when(productRepository.findBySkuCode(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(ProductNotFoundException.class, () -> productServiceImplementation.deleteProduct("Sku Code"));
        verify(productRepository).findBySkuCode(eq("Sku Code"));
        verify(productRepository).delete(isA(Product.class));
    }

    /**
     * Test {@link ProductServiceImplementation#deleteProduct(String)}.
     * <ul>
     *   <li>Given {@link ProductRepository} {@link CrudRepository#delete(Object)} does nothing.</li>
     *   <li>Then calls {@link CrudRepository#delete(Object)}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#deleteProduct(String)}
     */
    @Test
    @DisplayName("Test deleteProduct(String); given ProductRepository delete(Object) does nothing; then calls delete(Object)")
    @Tag("MaintainedByDiffblue")
    void testDeleteProduct_givenProductRepositoryDeleteDoesNothing_thenCallsDelete() {
        // Arrange
        Product product = new Product();
        product.setCategory("Category");
        product.setId(ObjectId.get());
        product.setPrice(new BigDecimal("2.3"));
        product.setProductName("Product Name");
        product.setSkuCode("Sku Code");
        product.setVendor("Vendor");
        Optional<Product> ofResult = Optional.of(product);
        doNothing().when(productRepository).delete(Mockito.<Product>any());
        when(productRepository.findBySkuCode(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        productServiceImplementation.deleteProduct("Sku Code");

        // Assert
        verify(productRepository).findBySkuCode(eq("Sku Code"));
        verify(productRepository).delete(isA(Product.class));
    }

    /**
     * Test {@link ProductServiceImplementation#deleteProduct(String)}.
     * <ul>
     *   <li>Given {@link ProductRepository} {@link ProductRepository#findBySkuCode(String)} return empty.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#deleteProduct(String)}
     */
    @Test
    @DisplayName("Test deleteProduct(String); given ProductRepository findBySkuCode(String) return empty")
    @Tag("MaintainedByDiffblue")
    void testDeleteProduct_givenProductRepositoryFindBySkuCodeReturnEmpty() {
        // Arrange
        Optional<Product> emptyResult = Optional.empty();
        when(productRepository.findBySkuCode(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ProductNotFoundException.class, () -> productServiceImplementation.deleteProduct("Sku Code"));
        verify(productRepository).findBySkuCode(eq("Sku Code"));
    }

    /**
     * Test {@link ProductServiceImplementation#productExists(String)}.
     * <ul>
     *   <li>Given {@link Product#Product()} Category is {@code Category}.</li>
     *   <li>Then return {@code true}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#productExists(String)}
     */
    @Test
    @DisplayName("Test productExists(String); given Product() Category is 'Category'; then return 'true'")
    @Tag("MaintainedByDiffblue")
    void testProductExists_givenProductCategoryIsCategory_thenReturnTrue() {
        // Arrange
        Product product = new Product();
        product.setCategory("Category");
        product.setId(ObjectId.get());
        product.setPrice(new BigDecimal("2.3"));
        product.setProductName("Product Name");
        product.setSkuCode("Sku Code");
        product.setVendor("Vendor");
        Optional<Product> ofResult = Optional.of(product);
        when(productRepository.findBySkuCode(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        boolean actualProductExistsResult = productServiceImplementation.productExists("Sku Code");

        // Assert
        verify(productRepository).findBySkuCode(eq("Sku Code"));
        assertTrue(actualProductExistsResult);
    }

    /**
     * Test {@link ProductServiceImplementation#productExists(String)}.
     * <ul>
     *   <li>Given {@link ProductRepository} {@link ProductRepository#findBySkuCode(String)} return empty.</li>
     *   <li>Then return {@code false}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#productExists(String)}
     */
    @Test
    @DisplayName("Test productExists(String); given ProductRepository findBySkuCode(String) return empty; then return 'false'")
    @Tag("MaintainedByDiffblue")
    void testProductExists_givenProductRepositoryFindBySkuCodeReturnEmpty_thenReturnFalse() {
        // Arrange
        Optional<Product> emptyResult = Optional.empty();
        when(productRepository.findBySkuCode(Mockito.<String>any())).thenReturn(emptyResult);

        // Act
        boolean actualProductExistsResult = productServiceImplementation.productExists("Sku Code");

        // Assert
        verify(productRepository).findBySkuCode(eq("Sku Code"));
        assertFalse(actualProductExistsResult);
    }

    /**
     * Test {@link ProductServiceImplementation#productExists(String)}.
     * <ul>
     *   <li>Then throw {@link ProductNotFoundException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceImplementation#productExists(String)}
     */
    @Test
    @DisplayName("Test productExists(String); then throw ProductNotFoundException")
    @Tag("MaintainedByDiffblue")
    void testProductExists_thenThrowProductNotFoundException() {
        // Arrange
        when(productRepository.findBySkuCode(Mockito.<String>any()))
                .thenThrow(new ProductNotFoundException("An error occurred"));

        // Act and Assert
        assertThrows(ProductNotFoundException.class, () -> productServiceImplementation.productExists("Sku Code"));
        verify(productRepository).findBySkuCode(eq("Sku Code"));
    }
}
