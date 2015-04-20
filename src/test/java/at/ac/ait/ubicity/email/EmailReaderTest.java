package at.ac.ait.ubicity.email;

import java.io.IOException;

import javax.mail.MessagingException;

import org.junit.Ignore;
import org.junit.Test;

public class EmailReaderTest {

	@Ignore
	@Test
	public void test() throws MessagingException, IOException {
		EmailReader er = new EmailReader();

		er.init();
		er.read();
	}

}
