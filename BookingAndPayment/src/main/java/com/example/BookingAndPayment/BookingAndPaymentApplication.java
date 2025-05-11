package com.example.BookingAndPayment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class BookingAndPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingAndPaymentApplication.class, args);
	}

}
