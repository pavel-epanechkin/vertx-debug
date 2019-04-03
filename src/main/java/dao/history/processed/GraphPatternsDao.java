package dao.history.processed;

import dao.H2DB;
import database.generated.public_.tables.daos.GraphPatternDao;
import database.generated.public_.tables.daos.SpanDao;


public class GraphPatternsDao extends GraphPatternDao {

    private H2DB storage;

    public GraphPatternsDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }

}
