package com.inventory.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.diffblue.cover.annotations.MethodsUnderTest;
import com.inventory.dto.ErrorDetails;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ContextConfiguration(classes = {AppErrorHandler.class})
@ExtendWith(SpringExtension.class)
class AppErrorHandlerTest {
    @Autowired
    private AppErrorHandler appErrorHandler;

    /**
     * Test {@link AppErrorHandler#handle500(Exception)}.
     * <ul>
     *   <li>When {@link Exception#Exception(String)} with {@code foo}.</li>
     *   <li>Then StatusCode return {@link HttpStatus}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppErrorHandler#handle500(Exception)}
     */
    @Test
    @DisplayName("Test handle500(Exception); when Exception(String) with 'foo'; then StatusCode return HttpStatus")
    @Tag("MaintainedByDiffblue")
    @MethodsUnderTest({"ResponseEntity AppErrorHandler.handle500(Exception)"})
    void testHandle500_whenExceptionWithFoo_thenStatusCodeReturnHttpStatus() {
        // Arrange and Act
        ResponseEntity<ErrorDetails> actualHandle500Result = appErrorHandler.handle500(new Exception("foo"));

        // Assert
        HttpStatusCode statusCode = actualHandle500Result.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        ErrorDetails body = actualHandle500Result.getBody();
        assertEquals("foo", body.getErrorMessage());
        assertEquals(500, body.getErrorCode());
        assertEquals(500, actualHandle500Result.getStatusCodeValue());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, statusCode);
        assertTrue(actualHandle500Result.hasBody());
        assertTrue(actualHandle500Result.getHeaders().isEmpty());
    }

    /**
     * Test {@link AppErrorHandler#handle400(MethodArgumentNotValidException)}.
     * <ul>
     *   <li>Then StatusCode return {@link HttpStatus}.</li>
     * </ul>
     * <p>
     * Method under test: {@link AppErrorHandler#handle400(MethodArgumentNotValidException)}
     */
    @Test
    @DisplayName("Test handle400(MethodArgumentNotValidException); then StatusCode return HttpStatus")
    @Tag("MaintainedByDiffblue")
    @MethodsUnderTest({"ResponseEntity AppErrorHandler.handle400(MethodArgumentNotValidException)"})
    void testHandle400_thenStatusCodeReturnHttpStatus() {
        // Arrange and Act
        ResponseEntity<ErrorDetails> actualHandle400Result = appErrorHandler
                .handle400(new MethodArgumentNotValidException(null, new BindException("Target", "Object Name")));

        // Assert
        HttpStatusCode statusCode = actualHandle400Result.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        ErrorDetails body = actualHandle400Result.getBody();
        assertEquals("", body.getErrorMessage());
        assertEquals(400, body.getErrorCode());
        assertEquals(400, actualHandle400Result.getStatusCodeValue());
        assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        assertTrue(actualHandle400Result.hasBody());
        assertTrue(actualHandle400Result.getHeaders().isEmpty());
    }
}
