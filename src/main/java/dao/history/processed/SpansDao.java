package dao.history.processed;

import dao.H2DB;
import database.generated.public_.tables.daos.SpanDao;

import static org.jooq.impl.DSL.and;
import static org.jooq.impl.DSL.field;


public class SpansDao extends SpanDao {

    private H2DB storage;

    public SpansDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }

}
