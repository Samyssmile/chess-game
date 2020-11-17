package de.chess.fx.app.client;

import de.chess.reader.ApplicationPropertiesReader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class ProductionClientProperties implements IClientProperties {

    private static final String PROPERTY_FILE_NAME = "client.properties";
    private Properties clientProperties;
    private static IClientProperties instance = null;

    private ProductionClientProperties() throws IOException {
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

    @Override
    public InetAddress getLocalHostAddress() {
        InetAddress localhost=null;
        try {
            localhost = InetAddress.getByName(this.clientProperties.getProperty("server.address"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return localhost;
    }

    public static IClientProperties instance() throws IOException {
        if (instance == null) {
            instance = new ProductionClientProperties();
        }
        return instance;
    }


}
