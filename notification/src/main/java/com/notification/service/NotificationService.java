package com.notification.service;

import com.common.OrderDto;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class NotificationService {
    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @PostConstruct
    public void init() {
        log.info("Initializing Twilio client with account SID: {}", accountSid);
        Twilio.init(accountSid, authToken);
        log.info("Twilio client successfully initialized");
    }

    @KafkaListener(topics = "order-created")
    public void consume(OrderDto order) {
        log.info("Received order notification event for order: {}", order.getSkuCode());

        String messageBody = String.format(
                "Hello %s, your order for %s (SKU: %s, Quantity: %d) is confirmed. It will be shipped to %s.",
                order.getCustomerName(),
                order.getProductName(),
                order.getSkuCode(),
                order.getQuantity(),
                order.getShippingAddress()
        );

        log.debug("Sending SMS to customer: {}, phone: {}", order.getCustomerName(), order.getCustomerPhone());

        try {
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(order.getCustomerPhone()),
                    new com.twilio.type.PhoneNumber(twilioPhoneNumber),
                    messageBody
            ).create();

            log.info("SMS notification sent successfully for order: {}, Twilio SID: {}",
                    order.getSkuCode(), message.getSid());
        } catch (Exception e) {
            log.error("Failed to send SMS notification for order: {}", order.getSkuCode(), e);
        }
    }
}