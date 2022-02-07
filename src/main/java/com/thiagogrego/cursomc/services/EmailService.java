package com.thiagogrego.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.thiagogrego.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);
}
