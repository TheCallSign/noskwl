/*
 * Copyright (C) 2015 Strawberry Studios
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.strawberrystudios.noskwl.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import static java.lang.System.out;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.ErrorManager;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
/**
 *
 * @author giddyc
 */
public class ConsoleHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
        if (getFormatter() == null) {
            Formatter formatter = new Formatter() {
                private final static String format = "[%1$s] %4$s: %5$s %n";
                private final Date dat = new Date();
                
                @Override
                public synchronized String format(LogRecord record) {
//                    SimpleDateFormat sf = ;
                    String formattedTime = "";
                    try {
                        out.println(dat);
                        formattedTime = (new SimpleDateFormat("HH:mm")).parse(dat.getTime()+"").toString();
                        
                    } catch (ParseException ex) {
                    }
                    dat.setTime(record.getMillis());
                    
                    String source;
                    if (record.getSourceClassName() != null) {
                        source = record.getSourceClassName();
                        if (record.getSourceMethodName() != null) {
                            source += " " + record.getSourceMethodName();
                        }
                    } else {
                        source = record.getLoggerName();
                    }
                    String message = formatMessage(record);
                    String throwable = "";
                    if (record.getThrown() != null) {
                        StringWriter sw = new StringWriter();
                        try (PrintWriter pw = new PrintWriter(sw)) {
                            pw.println();
                            record.getThrown().printStackTrace(pw);
                        }
                        throwable = sw.toString();
                    }
                    return String.format(format,
                            formattedTime,
                            source,
                            record.getLoggerName(),
                            record.getLevel(),
                            message,
                            throwable);
                }
            };
            
            setFormatter(formatter);
        }

        try {
            String message = getFormatter().format(record);
            if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
                System.err.write(message.getBytes());
            } else {
                System.out.write(message.getBytes());
            }
        } catch (Exception exception) {
            reportError(null, exception, ErrorManager.FORMAT_FAILURE);
        }

    }

    @Override
    public void close() throws SecurityException {
    }

    @Override
    public void flush() {
    }
}
