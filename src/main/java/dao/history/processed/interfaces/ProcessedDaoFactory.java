package dao.history.processed.interfaces;

import dao.history.processed.*;

import java.io.Closeable;

public interface ProcessedDaoFactory extends Closeable {

    MessagesDao getMessagesDao();

    TracesDao getTracesDao();

    SpansDao getSpansDao();

    TraceUnitsDao getTraceUnitsDao();

    TracePartsDao getTracePartsDao();
}
