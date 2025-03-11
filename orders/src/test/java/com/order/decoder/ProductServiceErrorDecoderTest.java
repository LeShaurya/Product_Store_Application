package com.order.decoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.order.exceptions.BadRequestException;
import com.order.exceptions.ProductNotFoundException;
import feign.Request;
import feign.Request.ProtocolVersion;
import feign.RequestTemplate;
import feign.Response;
import feign.Response.Body;
import feign.Response.Builder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ProductServiceErrorDecoder.class})
@ExtendWith(SpringExtension.class)
class ProductServiceErrorDecoderTest {
    @Autowired
    private ProductServiceErrorDecoder productServiceErrorDecoder;

    /**
     * Test {@link ProductServiceErrorDecoder#decode(String, Response)}.
     * <ul>
     *   <li>Then return {@link BadRequestException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceErrorDecoder#decode(String, Response)}
     */
    @Test
    @DisplayName("Test decode(String, Response); then return BadRequestException")
    void testDecode_thenReturnBadRequestException() throws UnsupportedEncodingException {
        // Arrange
        Builder bodyResult = Response.builder().body(mock(Body.class));
        Builder reasonResult = bodyResult.headers(new HashMap<>())
                .protocolVersion(ProtocolVersion.HTTP_1_0)
                .reason("Just cause");
        HashMap<String, Collection<String>> headers = new HashMap<>();
        byte[] body = "AXAXAXAX".getBytes("UTF-8");
        Builder requestResult = reasonResult
                .request(Request.create("GET", "https://example.org/example", headers, body, Charset.forName("UTF-8")));
        Response response = requestResult.requestTemplate(new RequestTemplate()).status(400).build();

        // Act
        Exception actualDecodeResult = productServiceErrorDecoder.decode("Method Key", response);

        // Assert
        assertTrue(actualDecodeResult instanceof BadRequestException);
        assertEquals("Bad request to product service", actualDecodeResult.getLocalizedMessage());
        assertEquals("Bad request to product service", actualDecodeResult.getMessage());
        assertNull(actualDecodeResult.getCause());
        assertEquals(0, actualDecodeResult.getSuppressed().length);
    }

    /**
     * Test {@link ProductServiceErrorDecoder#decode(String, Response)}.
     * <ul>
     *   <li>Then return LocalizedMessage is {@code Generic error with the product service}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceErrorDecoder#decode(String, Response)}
     */
    @Test
    @DisplayName("Test decode(String, Response); then return LocalizedMessage is 'Generic error with the product service'")
    void testDecode_thenReturnLocalizedMessageIsGenericErrorWithTheProductService() throws UnsupportedEncodingException {
        // Arrange
        Builder bodyResult = Response.builder().body(mock(Body.class));
        Builder reasonResult = bodyResult.headers(new HashMap<>())
                .protocolVersion(ProtocolVersion.HTTP_1_0)
                .reason("Just cause");
        HashMap<String, Collection<String>> headers = new HashMap<>();
        byte[] body = "AXAXAXAX".getBytes("UTF-8");
        Builder requestResult = reasonResult
                .request(Request.create("GET", "https://example.org/example", headers, body, Charset.forName("UTF-8")));
        Response response = requestResult.requestTemplate(new RequestTemplate()).status(200).build();

        // Act
        Exception actualDecodeResult = productServiceErrorDecoder.decode("Method Key", response);

        // Assert
        assertEquals("Generic error with the product service", actualDecodeResult.getLocalizedMessage());
        assertEquals("Generic error with the product service", actualDecodeResult.getMessage());
        assertNull(actualDecodeResult.getCause());
        assertEquals(0, actualDecodeResult.getSuppressed().length);
    }

    /**
     * Test {@link ProductServiceErrorDecoder#decode(String, Response)}.
     * <ul>
     *   <li>Then return {@link ProductNotFoundException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link ProductServiceErrorDecoder#decode(String, Response)}
     */
    @Test
    @DisplayName("Test decode(String, Response); then return ProductNotFoundException")
    void testDecode_thenReturnProductNotFoundException() throws UnsupportedEncodingException {
        // Arrange
        Builder bodyResult = Response.builder().body(mock(Body.class));
        Builder reasonResult = bodyResult.headers(new HashMap<>())
                .protocolVersion(ProtocolVersion.HTTP_1_0)
                .reason("Just cause");
        HashMap<String, Collection<String>> headers = new HashMap<>();
        byte[] body = "AXAXAXAX".getBytes("UTF-8");
        Builder requestResult = reasonResult
                .request(Request.create("GET", "https://example.org/example", headers, body, Charset.forName("UTF-8")));
        Response response = requestResult.requestTemplate(new RequestTemplate()).status(404).build();

        // Act
        Exception actualDecodeResult = productServiceErrorDecoder.decode("Method Key", response);

        // Assert
        assertInstanceOf(ProductNotFoundException.class, actualDecodeResult);
        assertEquals("Product not found in the product service", actualDecodeResult.getLocalizedMessage());
        assertEquals("Product not found in the product service", actualDecodeResult.getMessage());
        assertNull(actualDecodeResult.getCause());
        assertEquals(0, actualDecodeResult.getSuppressed().length);
    }
}
