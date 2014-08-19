package at.ac.ait.ubicity.email;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import net.xeoh.plugins.base.annotations.events.Init;
import net.xeoh.plugins.base.annotations.events.Shutdown;

import org.apache.log4j.Logger;

import at.ac.ait.ubicity.commons.interfaces.UbicityPlugin;
import at.ac.ait.ubicity.commons.util.PropertyLoader;

import com.sun.mail.imap.IMAPFolder;

@PluginImplementation
public class EmailReader implements UbicityPlugin {

	private static final Logger logger = Logger.getLogger(EmailReader.class);

	private String name;

	private Session session;

	private Store store;

	private boolean started;

	public String getName() {
		return name;
	}

	@Init
	public void init() {
		final PropertyLoader config = new PropertyLoader(
				EmailReader.class.getResource("/email.cfg"));

		this.name = config.getString("plugin.email.name");

		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");

		Session session = Session.getInstance(props, null);
		try {
			store = session.getStore("imaps");
			store.connect("imap.gmail.com",
					config.getString("plugin.email.user"),
					config.getString("plugin.email.password"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		started = true;
	}

	public boolean isStarted() {
		return started;
	}

	public void read() throws MessagingException, IOException {

		IMAPFolder inbox = (IMAPFolder) store.getFolder("INBOX");
		inbox.open(Folder.READ_ONLY);

		Message[] messages = inbox.getMessages();

		for (int i = 0; i < messages.length; i++) {

			Message msg = messages[i];

			Address[] from = msg.getFrom();
			for (Address address : from) {
				System.out.println("FROM: " + address.toString());
			}

			Address[] to = msg.getAllRecipients();
			for (Address address : to) {
				System.out.println("TO: " + address.toString());
			}

			System.out.println("SENT DATE:" + msg.getSentDate());
			System.out.println("SUBJECT:" + msg.getSubject());
			System.out.println("CONTENT:" + msg.getContent());

		}

	}

	@Shutdown
	public void shutdown() {
	}
}