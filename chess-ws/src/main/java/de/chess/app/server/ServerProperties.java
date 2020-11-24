package de.chess.app.server;

import de.chess.reader.ApplicationPropertiesReader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class ServerProperties {

    private final String propertieFileName = "server.properties";
    private Properties serverProperties;

    public ServerProperties() throws IOException {
        initProperties();
    }

    private void initProperties() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ApplicationPropertiesReader applicationPropertiesReader = new ApplicationPropertiesReader(classLoader.getResourceAsStream(this.propertieFileName));
        this.serverProperties = applicationPropertiesReader.readProperties(this.propertieFileName);
    }

    public Properties getServerProperties() {
        return this.serverProperties;
    }

    public int getServerPort() {
        return Integer.parseInt(this.serverProperties.getProperty("application.port"));
    }

    public String getServerName() {
        return this.serverProperties.getProperty("application.name");
    }

    public boolean isBlockingMode() {
        return Boolean.parseBoolean(this.serverProperties.getProperty("server.blocking"));
    }

    public String serverAddress() {
        return this.serverProperties.getProperty("server.address");
    }

    public InetAddress getLocalHostAddress() {
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return localhost;
    }

    public int getBacklogLimit() {
        return Integer.parseInt(this.serverProperties.getProperty("application.backlog"));
    }
}
