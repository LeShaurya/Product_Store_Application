package com.notification.service;

import com.common.OrderDto;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    private NotificationService notificationService;

    @Mock
    private Message message;

    @Mock
    private MessageCreator messageCreator;

    private final String ACCOUNT_SID = "";
    private final String AUTH_TOKEN = "";
    private final String TWILIO_PHONE = "";
    
    @BeforeEach
    public void setUp() {
        notificationService = new NotificationService();
        
        ReflectionTestUtils.setField(notificationService, "accountSid", ACCOUNT_SID);
        ReflectionTestUtils.setField(notificationService, "authToken", AUTH_TOKEN);
        ReflectionTestUtils.setField(notificationService, "twilioPhoneNumber", TWILIO_PHONE);
    }

    @Test
    public void testInit() {
        // Use MockedStatic to mock Twilio.init static method
        try (MockedStatic<Twilio> mockedTwilio = mockStatic(Twilio.class)) {
            notificationService.init();
            
            mockedTwilio.verify(() -> Twilio.init(ACCOUNT_SID, AUTH_TOKEN), times(1));
        }
    }

    @Test
    public void testConsume() {
        OrderDto order = new OrderDto();
        order.setSkuCode("PROD-123");
        order.setProductName("Test Product");
        order.setQuantity(2);
        order.setCustomerName("John Doe");
        order.setCustomerPhone("+19876543210");
        order.setShippingAddress("123 Test St, Test City");

        String expectedMessage = String.format(
                "Hello %s, your order for %s (SKU: %s, Quantity: %d) is confirmed. It will be shipped to %s.",
                order.getCustomerName(),
                order.getProductName(),
                order.getSkuCode(),
                order.getQuantity(),
                order.getShippingAddress()
        );

        when(messageCreator.create()).thenReturn(message);
        when(message.getSid()).thenReturn("MSG123456");

        // Use MockedStatic to mock Message.creator static method
        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            // Setup the static method mock to return our mocked creator
            mockedMessage.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    anyString()
            )).thenReturn(messageCreator);

            notificationService.consume(order);

            ArgumentCaptor<PhoneNumber> toPhoneCaptor = ArgumentCaptor.forClass(PhoneNumber.class);
            ArgumentCaptor<PhoneNumber> fromPhoneCaptor = ArgumentCaptor.forClass(PhoneNumber.class);
            ArgumentCaptor<String> messageBodyCaptor = ArgumentCaptor.forClass(String.class);

            mockedMessage.verify(() -> 
                Message.creator(
                    toPhoneCaptor.capture(),
                    fromPhoneCaptor.capture(),
                    messageBodyCaptor.capture()
                ), times(1)
            );

            assertEquals(order.getCustomerPhone(), toPhoneCaptor.getValue().getEndpoint());
            assertEquals(TWILIO_PHONE, fromPhoneCaptor.getValue().getEndpoint());
            assertEquals(expectedMessage, messageBodyCaptor.getValue());
            
            verify(messageCreator, times(1)).create();
        }
    }

    @Test
    public void testConsumeHandlesException() {
        // Create test order
        OrderDto order = new OrderDto();
        order.setSkuCode("PROD-123");
        order.setProductName("Test Product");
        order.setQuantity(2);
        order.setCustomerName("John Doe");
        order.setCustomerPhone("+19876543210");
        order.setShippingAddress("123 Test St, Test City");

        when(messageCreator.create()).thenThrow(new RuntimeException("Test exception"));

        try (MockedStatic<Message> mockedMessage = mockStatic(Message.class)) {
            mockedMessage.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    anyString()
            )).thenReturn(messageCreator);

            notificationService.consume(order);

            verify(messageCreator, times(1)).create();
        }
    }
}