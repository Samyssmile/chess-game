package de.chess.reader;

import javax.management.InvalidApplicationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
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

        if (!Objects.isNull(inputStream)) {
            properties.load(inputStream);
        }
        return properties;
    }

}
