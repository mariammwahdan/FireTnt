package com.example.BookingAndPayment.Payment;

import com.example.BookingAndPayment.Annotations.DistributedLock;
import com.example.BookingAndPayment.Payment.DTO.CreatePaymentDTO;
import com.example.BookingAndPayment.Redis.RedisClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RedisClient redisClient;
    private final ObjectMapper objectMapper;

    private static final String PAYMENT_CACHE_PREFIX = "payment";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, RedisClient redisClient, ObjectMapper objectMapper) {
        this.paymentRepository = paymentRepository;
        this.redisClient = redisClient;
        this.objectMapper = objectMapper;
    }

    public List<Payment> getAllPayments() {
        try {
            String cachedPayments = redisClient.get(PAYMENT_CACHE_PREFIX + ":all");
            if (cachedPayments != null) {
                return objectMapper.readValue(cachedPayments, List.class);
            }
        } catch (JsonProcessingException e) {
            // Fallback to DB if cache retrieval fails
        }

        List<Payment> payments = paymentRepository.findAll();
        try {
            String paymentsJson = objectMapper.writeValueAsString(payments);
            redisClient.set(PAYMENT_CACHE_PREFIX + ":all", paymentsJson, CACHE_TTL);
        } catch (JsonProcessingException e) {
            // Cache store failed, no problem
        }

        return payments;
    }

    public Payment getPaymentByBookingId(long bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }

    @DistributedLock(
            keyPrefix = PAYMENT_CACHE_PREFIX,
            keyIdentifierExpression = "#createPaymentDTO.bookingId",
            leaseTime = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public Payment createPayment(CreatePaymentDTO createPaymentDTO) {
        Payment payment = new Payment();
        payment.setBookingId(createPaymentDTO.getBookingId());
        payment.setAmount(createPaymentDTO.getAmount());
        payment.setStatus(createPaymentDTO.getStatus());
        payment.setCreatedAt(createPaymentDTO.getCreatedAt());

        Payment savedPayment = paymentRepository.save(payment);

        // 🧹 Invalidate cache after create
        redisClient.delete(PAYMENT_CACHE_PREFIX + ":all");

        return savedPayment;
    }

    @DistributedLock(
            keyPrefix = PAYMENT_CACHE_PREFIX,
            keyIdentifierExpression = "#id",
            leaseTime = 30,
            timeUnit = TimeUnit.SECONDS
    )
    public Payment updatePaymentStatus(long id, Payment.PaymentStatus status) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setStatus(status);

            Payment updatedPayment = paymentRepository.save(payment);

            // 🧹 Invalidate cache after update
            redisClient.delete(PAYMENT_CACHE_PREFIX + ":all");

            return updatedPayment;
        }
        return null;
    }
}