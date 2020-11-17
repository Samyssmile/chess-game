package de.chess.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class WebTextLogger {
    private static final boolean APPEND = false;
    private static final String LOG_DIR = "log";
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    static private FileHandler fileHTML;
    static private Formatter formatterHTML;
    static private ConsoleHandler consoleHandler;

    static public void setup() throws IOException {

        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        logger.setLevel(Level.INFO);
        createLogDirIfNotExists();
        fileTxt = new FileHandler(LOG_DIR+File.separator+"Logging.txt", APPEND);
        fileHTML = new FileHandler(LOG_DIR+File.separator+"Logging.html", APPEND);
        consoleHandler = new ConsoleHandler();

        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);

        // create an HTML formatter
        formatterHTML = new HtmlFormatter();
        fileHTML.setFormatter(formatterHTML);
        logger.addHandler(fileHTML);

        consoleHandler.setFormatter(formatterTxt);
        logger.addHandler(consoleHandler);
    }

    private static void createLogDirIfNotExists() {
        File logLocation = new File(LOG_DIR);
        if (!logLocation.exists()){
            logLocation.mkdir();
        }
    }
}