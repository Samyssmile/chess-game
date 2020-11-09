package de.chess.reader;

import javax.management.InvalidApplicationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationPropertiesReader implements IApplicationPropertiesReader {

    private final InputStream inputStream;

    public ApplicationPropertiesReader(InputStream inputStream) {
        assert inputStream!=null;
        this.inputStream = inputStream;
    }

    @Override
    public Properties readProperties(String propertieFileName) throws IOException {
        Properties properties = new Properties();

        if (inputStream != null) {
            properties.load(inputStream);
        }
        return properties;
    }

}
