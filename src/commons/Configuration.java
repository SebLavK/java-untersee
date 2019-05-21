package commons;

import main.Main;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Properties;

public class Configuration {

	private static Properties gamelog;

	private Configuration() {

	}

	public static void loadProperties() {
		try {
			gamelog = new Properties();
			gamelog.load(Main.class.getResourceAsStream("/properties/gamelog.properties"));
			System.out.println(composeMessage("update.sonar.new.contact","235", "S1"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String composeMessage(String key, String... params) {
		return MessageFormat.format(gamelog.getProperty(key), params);
	}
}
