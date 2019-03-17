package dao.history.processed;

import dao.H2DB;
import database.generated.public_.tables.daos.SpanDao;
import database.generated.public_.tables.daos.TraceUnitDao;


public class TraceUnitsDao extends TraceUnitDao {

    private H2DB storage;

    public TraceUnitsDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }

}
