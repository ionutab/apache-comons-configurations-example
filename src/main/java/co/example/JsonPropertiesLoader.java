package co.example;

import org.apache.commons.configuration2.JSONConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;

public class JsonPropertiesLoader {

    private static JsonPropertiesLoader instance;
    private JSONConfiguration configuration;

    private JsonPropertiesLoader() {
        try {
            Parameters params = new Parameters();

            // Specify the JSON file you want to read
            File configFile = new File("application.json");

            // Create a JSONConfiguration instance
            configuration = new FileBasedConfigurationBuilder<>(JSONConfiguration.class)
                    .configure(params.fileBased().setFile(configFile))
                    .getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static synchronized JsonPropertiesLoader getInstance() {
        if (instance == null) {
            instance = new JsonPropertiesLoader();
        }
        return instance;
    }

    public JSONConfiguration getConfiguration() {
        return configuration;
    }

    public static void main(String... args) {
        MyApplicationConfiguration myApplicationConfiguration = new MyApplicationConfiguration(JsonPropertiesLoader.getInstance().getConfiguration());
        System.out.println(myApplicationConfiguration);
    }
}
