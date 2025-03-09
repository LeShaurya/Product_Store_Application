package com.notification.service;

import com.common.OrderDto;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


@Service
public class NotificationService {
    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;

    @PostConstruct
    public void init() {    Twilio.init(accountSid, authToken);}


    @KafkaListener(topics = "order-created")
    public void consume(OrderDto order) {
        String messageBody = String.format(
                "Hello %s, your order for %s (SKU: %s, Quantity: %d) is confirmed. It will be shipped to %s.",
                order.getCustomerName(),
                order.getProductName(),
                order.getSkuCode(),
                order.getQuantity(),
                order.getShippingAddress()
        );

        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(order.getCustomerPhone()),
                new com.twilio.type.PhoneNumber(twilioPhoneNumber),
                messageBody
        ).create();
    }
}
