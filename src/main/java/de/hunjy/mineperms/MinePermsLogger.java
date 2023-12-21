package de.hunjy.mineperms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinePermsLogger {


    private static Logger logger;

    static {
        logger = Logger.getLogger(MinePerms.getInstance().getDescription().getName());
        logger.setLevel(Level.ALL);
    }

    public static void log(String msg) {
        logger.info(msg);
    }

    public static void warning(String msg) {
        logger.warning(msg);
    }

    public static Logger getLogger() {
        return logger;
    }


}
