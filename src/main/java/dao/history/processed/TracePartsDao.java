package dao.history.processed;

import dao.H2DB;
import database.generated.public_.tables.daos.SpanDao;
import database.generated.public_.tables.daos.TracePartDao;


public class TracePartsDao extends TracePartDao {

    private H2DB storage;

    public TracePartsDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }

}
