package dao.history.processed.interfaces;

import dao.history.processed.*;
import database.generated.public_.tables.daos.TraceLabelDao;

import java.io.Closeable;

public interface ProcessedDaoFactory extends Closeable {

    MessagesDao getMessagesDao();

    TracesDao getTracesDao();

    SpansDao getSpansDao();

    TraceUnitsDao getTraceUnitsDao();

    TracePartsDao getTracePartsDao();

    GraphsDao getGraphsDao();

    TraceLabelsDao getTraceLabelsDao();

    SpanPatternsDao getSpanPatternsDao();

    TracePatternsDao getTracePatternsDao();

    GraphPatternsDao getGraphPatternsDao();

    GraphPatternsStructureAnomaliesDao getGraphPatternsStructureAnomaliesDao();
}
