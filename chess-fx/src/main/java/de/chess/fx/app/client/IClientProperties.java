package de.chess.fx.app.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

public interface IClientProperties {

    public void initProperties() throws IOException;
    public String getPropertyFileName();

    public Properties getClientProperties();

    public default String getApplicationTitle(){
        return getClientProperties().getProperty("application.title");
    }

    public default String getServerAddress(){
        return getClientProperties().getProperty("server.address.local");
    }

    public default int getServerPort(){
        return Integer.parseInt( getClientProperties().getProperty("server.address.port"));
    }
    public void setClientProperties(Properties properties);


    public InetAddress getLocalHostAddress();
}
