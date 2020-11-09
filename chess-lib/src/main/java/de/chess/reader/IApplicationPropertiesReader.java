package de.chess.reader;

import java.io.IOException;
import java.util.Properties;

public interface IApplicationPropertiesReader {

    public Properties readProperties(String propertyFileName) throws IOException;
}
