package dao.history.processed;

import dao.H2DB;
import database.generated.public_.tables.daos.SpanDao;
import database.generated.public_.tables.daos.SpanPatternDao;


public class SpanPatternsDao extends SpanPatternDao {

    private H2DB storage;

    public SpanPatternsDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }

}
