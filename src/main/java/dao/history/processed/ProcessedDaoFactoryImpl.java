package dao.history.processed;

import dao.H2DB;

import javax.validation.constraints.NotNull;

public class ProcessedDaoFactoryImpl implements dao.history.processed.interfaces.ProcessedDaoFactory {

    private H2DB storage;

    private final String INIT_SCRIPT_PATH = "src/main/resources/init_history_db.sql";

    public ProcessedDaoFactoryImpl(@NotNull String storagePath) {
        storage = new H2DB(storagePath, INIT_SCRIPT_PATH);
    }

    @Override
    public MessagesDao getMessagesDao() {
        return new MessagesDao(storage);
    }

    @Override
    public TracesDao getTracesDao() {
        return new TracesDao(storage);
    }

    @Override
    public SpansDao getSpansDao() {
        return new SpansDao(storage);
    }

    @Override
    public TraceUnitsDao getTraceUnitsDao() {
        return new TraceUnitsDao(storage);
    }

    @Override
    public TracePartsDao getTracePartsDao() {
        return new TracePartsDao(storage);
    }

    @Override
    public GraphsDao getGraphsDao() {
        return new GraphsDao(storage);
    }

    @Override
    public SpanPatternsDao getSpanPatternsDao() {
        return new SpanPatternsDao(storage);
    }

    @Override
    public TracePatternsDao getTracePatternsDao() {
        return new TracePatternsDao(storage);
    }

    @Override
    public GraphPatternsDao getGraphPatternsDao() {
        return new GraphPatternsDao(storage);
    }

    public void close() {
        storage.close();
    }
}
