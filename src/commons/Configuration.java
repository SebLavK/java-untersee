package commons;

import main.Main;

import java.io.IOException;
import java.util.Properties;

public class Configuration {

	private static Properties gamelog;

	private Configuration() {

	}

	public static void loadProperties() {
		try {
			gamelog = new Properties();
			gamelog.load(Main.class.getResourceAsStream("/properties/gamelog.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getGameLogValue(String key) {
		return gamelog.getProperty(key);
	}
}
