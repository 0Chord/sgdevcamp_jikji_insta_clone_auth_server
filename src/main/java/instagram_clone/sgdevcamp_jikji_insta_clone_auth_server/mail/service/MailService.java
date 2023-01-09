package instagram_clone.sgdevcamp_jikji_insta_clone_auth_server.mail.service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {
	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;
	private String authCode;

	public void createCode(){
		Random random = new Random();
		StringBuffer key = new StringBuffer();

		for(int i=0;i<8;i++){
			int index = random.nextInt(3);

			switch (index){
				case 0:
					key.append((char)((int)random.nextInt(26)+97));
					break;
				case 1:
					key.append((char)((int)random.nextInt(26)+65));
					break;
				case 2:
					key.append(random.nextInt(9));
					break;
			}
		}
		authCode = key.toString();
	}

	public MimeMessage createMailForm(String email) throws MessagingException, UnsupportedEncodingException{
		createCode();
		String setFrom = "smilestagram.clone@gmail.com";
		String title = authCode + "is your Smilestagram code";

		MimeMessage message = mailSender.createMimeMessage();
		message.addRecipients(MimeMessage.RecipientType.TO,email);
		message.setSubject(title);
		message.setFrom(setFrom);
		message.setText(setContext(email,authCode),"utf-8","html");

		return message;
	}

	public String sendMail(String toEmail) throws MessagingException, UnsupportedEncodingException{
		MimeMessage emailForm = createMailForm(toEmail);
		mailSender.send(emailForm);

		return authCode;
	}

	public String setContext(String email,String code){
		Context context = new Context();
		context.setVariable("email",email);
		context.setVariable("code",code);
		return templateEngine.process("mail",context);
	}
}
