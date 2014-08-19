package at.ac.ait.ubicity.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.Init;
import net.xeoh.plugins.base.annotations.events.Shutdown;

import org.apache.log4j.Logger;

import at.ac.ait.ubicity.commons.interfaces.UbicityPlugin;
import at.ac.ait.ubicity.commons.util.PropertyLoader;

@PluginImplementation
public class EmailSender implements UbicityPlugin {

	private static final Logger logger = Logger.getLogger(EmailSender.class);

	private String name;

	private Session session;

	private boolean started;

	public String getName() {
		return name;
	}

	@Init
	public void init() {
		final PropertyLoader config = new PropertyLoader(
				EmailSender.class.getResource("/email.cfg"));

		this.name = config.getString("plugin.email.name");

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(config
								.getString("plugin.email.user"), config
								.getString("plugin.email.password"));
					}
				});
		started = true;
	}

	public boolean isStarted() {
		return started;
	}

	public void send(String txt) throws MessagingException {

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("ubicity.ait@gmail.com"));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("christoph.ruggenthaler@ait.ac.at"));
		message.setSubject("Testing Subject");
		message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");

		Transport.send(message);
	}

	@Shutdown
	public void shutdown() {
	}
}