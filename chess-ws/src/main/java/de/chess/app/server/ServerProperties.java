package de.chess.app.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerProperties {

  private final String propertieFileName = "server.properties";
  private Properties serverProperties;

  public ServerProperties() throws IOException {
    initProperties();
  }

  private void initProperties() throws IOException {
    serverProperties = new Properties();

    try (InputStream inputStream =
        getClass().getClassLoader().getResourceAsStream(propertieFileName)) {
      if (inputStream != null) {
        serverProperties.load(inputStream);
      }
    }
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
}
