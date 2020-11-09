package de.chess.fx.app.client;

import de.chess.reader.ApplicationPropertiesReader;

import java.io.IOException;
import java.util.Properties;

public class ClientProperties {

    private final String propertieFileName = "client.properties";
    private Properties clientProperties;

    public ClientProperties() throws IOException {
        initProperties();
    }

    private void initProperties() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ApplicationPropertiesReader applicationPropertiesReader = new ApplicationPropertiesReader(classLoader.getResourceAsStream(propertieFileName));
        clientProperties = applicationPropertiesReader.readProperties(propertieFileName);
    }

    public Properties getClientProperties() {
        return clientProperties;
    }

    public String getApplicationTitle(){
        return clientProperties.getProperty("application.title");
    }
}
