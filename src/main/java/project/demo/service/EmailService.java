package project.demo.service;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class EmailService {
	@Autowired
	JavaMailSender  javaMailSender;
	
	@Autowired
	SpringTemplateEngine templateEngine; //email template
	
	public void sendEmail(String to, String subject, String body) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,
					StandardCharsets.UTF_8.name());
			
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			helper.setFrom("kienkien1401@gmail.com");
			
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void sendBrithdayEmail(String to, String name) {
		String subject = "Happy Birthday ! " + name;
		
		Context ctx = new Context();// thymleaf
		ctx.setVariable("name", name); //name cua nguoi dung truyen len view
		
		String body = templateEngine.process("email/hpbd.html", ctx);
		
		sendEmail(to, subject, body);
	}
	
	public void testEmail() {
		String to = "Mail_nguoi_nhan@gmai.com";
		String subject = "Tieu de email";
		String body = "<h1>Noi dung email</h1>";
		
//		Context ctx = new Context();
//		String body = templateEngine.process("email/hpbd.html", ctx);
		
		sendEmail(to, subject, body);
	}
}
