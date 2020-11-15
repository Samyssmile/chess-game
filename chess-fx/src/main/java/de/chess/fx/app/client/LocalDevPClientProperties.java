package de.chess.fx.app.client;

import de.chess.reader.ApplicationPropertiesReader;

import java.io.IOException;
import java.util.Properties;

public class LocalDevPClientProperties implements IClientProperties {
    private static final String PROPERTY_FILE_NAME = "dev_client.properties";
    private static IClientProperties instance = null;
    private Properties clientProperties;

    private LocalDevPClientProperties() throws IOException {
        initProperties();
    }

    @Override
    public void initProperties() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ApplicationPropertiesReader applicationPropertiesReader = new ApplicationPropertiesReader(classLoader.getResourceAsStream(getPropertyFileName()));
        setClientProperties(applicationPropertiesReader.readProperties(getPropertyFileName()));
    }

    @Override
    public String getPropertyFileName() {
        return PROPERTY_FILE_NAME;
    }

    @Override
    public Properties getClientProperties() {
        return clientProperties;
    }

    @Override
    public void setClientProperties(Properties properties) {
        this.clientProperties = properties;
    }

    public static IClientProperties instance() throws IOException {
        if (instance == null) {
            instance = new LocalDevPClientProperties();
        }
        return instance;
    }
}
