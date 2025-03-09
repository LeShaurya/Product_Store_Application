package com.order.proxy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.order.exceptions.ProductNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProductProxyFallback.class})
@ExtendWith(SpringExtension.class)
class ProductProxyFallbackDiffblueTest {
    @Autowired
    private ProductProxyFallback productProxyFallback;

    /**
     * Test {@link ProductProxyFallback#getProductBySkuCode(String)}.
     * <p>
     * Method under test: {@link ProductProxyFallback#getProductBySkuCode(String)}
     */
    @Test
    @DisplayName("Test getProductBySkuCode(String)")
    @Tag("MaintainedByDiffblue")
    void testGetProductBySkuCode() {
        // Arrange, Act and Assert
        assertThrows(ProductNotFoundException.class, () -> productProxyFallback.getProductBySkuCode("Sku Code"));
    }

    /**
     * Test {@link ProductProxyFallback#productExists(String)}.
     * <p>
     * Method under test: {@link ProductProxyFallback#productExists(String)}
     */
    @Test
    @DisplayName("Test productExists(String)")
    @Tag("MaintainedByDiffblue")
    void testProductExists() {
        // Arrange, Act and Assert
        assertFalse(productProxyFallback.productExists("Sku Code"));
    }
}
