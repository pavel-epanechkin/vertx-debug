package dao.history.processed;

import dao.H2DB;
import database.generated.public_.tables.daos.SpanDao;
import database.generated.public_.tables.daos.TracePatternDao;


public class TracePatternsDao extends TracePatternDao {

    private H2DB storage;

    public TracePatternsDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }

}
