package de.chess.app.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerPropertiesTest {

  private ServerProperties serverProperties;

  @BeforeEach
  void setUp() throws IOException {
    serverProperties = new ServerProperties();
  }

  @Test
  void getServerProperties() {
    Properties serverProperties = this.serverProperties.getServerProperties();
    assertTrue(serverProperties != null);
  }

  @Test
  void serverPortSetTo8085() {
    Properties serverProperties = this.serverProperties.getServerProperties();
    assertTrue(serverProperties != null);
    String port = serverProperties.getProperty("application.port");
    assertTrue(port.equals("8085"));
  }
}
