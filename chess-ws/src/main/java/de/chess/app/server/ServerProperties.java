package de.chess.app.server;

import de.chess.app.ChessBackend;
import de.chess.reader.ApplicationPropertiesReader;

import java.io.IOException;
import java.io.InputStream;
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
        ApplicationPropertiesReader applicationPropertiesReader = new ApplicationPropertiesReader(classLoader.getResourceAsStream(propertieFileName));
        serverProperties = applicationPropertiesReader.readProperties(propertieFileName);
    }

    public Properties getServerProperties() {
        return serverProperties;
    }

    public int getServerPort() {
        return Integer.parseInt(serverProperties.getProperty("application.port"));
    }

    public String getServerName() {
        return serverProperties.getProperty("application.name");
    }

    public boolean isBlockingMode() {
        return Boolean.parseBoolean(serverProperties.getProperty("server.blocking"));
    }

    public String serverAddress() {
        return serverProperties.getProperty("server.address");
    }

    public InetAddress getLocalHostAddress() {
        InetAddress localhost=null;
        try {
            localhost = InetAddress.getByName(serverProperties.getProperty("server.address"));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return localhost;
    }
}
