package com.order.decoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import com.order.exceptions.InsufficientInventoryException;
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

@ContextConfiguration(classes = {InventoryServiceErrorDecoder.class})
@ExtendWith(SpringExtension.class)
class InventoryServiceErrorDecoderTest {
    @Autowired
    private InventoryServiceErrorDecoder inventoryServiceErrorDecoder;

    /**
     * Test {@link InventoryServiceErrorDecoder#decode(String, Response)}.
     * <ul>
     *   <li>Then return LocalizedMessage is {@code Conflict while processing inventory request}.</li>
     * </ul>
     * <p>
     * Method under test: {@link InventoryServiceErrorDecoder#decode(String, Response)}
     */
    @Test
    @DisplayName("Test decode(String, Response); then return LocalizedMessage is 'Conflict while processing inventory request'")
    void testDecode_thenReturnLocalizedMessageIsConflictWhileProcessingInventoryRequest()
            throws UnsupportedEncodingException {
        // Arrange
        Builder bodyResult = Response.builder().body(mock(Body.class));
        Builder reasonResult = bodyResult.headers(new HashMap<>())
                .protocolVersion(ProtocolVersion.HTTP_1_0)
                .reason("Just cause");
        HashMap<String, Collection<String>> headers = new HashMap<>();
        byte[] body = "AXAXAXAX".getBytes("UTF-8");
        Builder requestResult = reasonResult
                .request(Request.create("GET", "https://example.org/example", headers, body, Charset.forName("UTF-8")));
        Response response = requestResult.requestTemplate(new RequestTemplate()).status(409).build();

        // Act
        Exception actualDecodeResult = inventoryServiceErrorDecoder.decode("Method Key", response);

        // Assert
        assertTrue(actualDecodeResult instanceof InsufficientInventoryException);
        assertEquals("Conflict while processing inventory request", actualDecodeResult.getLocalizedMessage());
        assertEquals("Conflict while processing inventory request", actualDecodeResult.getMessage());
        assertNull(actualDecodeResult.getCause());
        assertEquals(0, actualDecodeResult.getSuppressed().length);
    }

    /**
     * Test {@link InventoryServiceErrorDecoder#decode(String, Response)}.
     * <ul>
     *   <li>Then return LocalizedMessage is {@code Generic error from inventory service}.</li>
     * </ul>
     * <p>
     * Method under test: {@link InventoryServiceErrorDecoder#decode(String, Response)}
     */
    @Test
    @DisplayName("Test decode(String, Response); then return LocalizedMessage is 'Generic error from inventory service'")
    void testDecode_thenReturnLocalizedMessageIsGenericErrorFromInventoryService() throws UnsupportedEncodingException {
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
        Exception actualDecodeResult = inventoryServiceErrorDecoder.decode("Method Key", response);

        // Assert
        assertEquals("Generic error from inventory service", actualDecodeResult.getLocalizedMessage());
        assertEquals("Generic error from inventory service", actualDecodeResult.getMessage());
        assertNull(actualDecodeResult.getCause());
        assertEquals(0, actualDecodeResult.getSuppressed().length);
    }

    /**
     * Test {@link InventoryServiceErrorDecoder#decode(String, Response)}.
     * <ul>
     *   <li>Then return LocalizedMessage is {@code Insufficient inventory available}.</li>
     * </ul>
     * <p>
     * Method under test: {@link InventoryServiceErrorDecoder#decode(String, Response)}
     */
    @Test
    @DisplayName("Test decode(String, Response); then return LocalizedMessage is 'Insufficient inventory available'")
    void testDecode_thenReturnLocalizedMessageIsInsufficientInventoryAvailable() throws UnsupportedEncodingException {
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
        Exception actualDecodeResult = inventoryServiceErrorDecoder.decode("Method Key", response);

        // Assert
        assertTrue(actualDecodeResult instanceof InsufficientInventoryException);
        assertEquals("Insufficient inventory available", actualDecodeResult.getLocalizedMessage());
        assertEquals("Insufficient inventory available", actualDecodeResult.getMessage());
        assertNull(actualDecodeResult.getCause());
        assertEquals(0, actualDecodeResult.getSuppressed().length);
    }
}
