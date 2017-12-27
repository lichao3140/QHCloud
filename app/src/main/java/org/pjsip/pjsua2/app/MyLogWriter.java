package org.pjsip.pjsua2.app;

import org.pjsip.pjsua2.LogEntry;
import org.pjsip.pjsua2.LogWriter;

public class MyLogWriter extends LogWriter{
	@Override
    public void write(LogEntry entry)
    {
	System.out.println(entry.getMsg());
    }
}
