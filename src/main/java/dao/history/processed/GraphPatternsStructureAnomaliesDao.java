package dao.history.processed;

import dao.H2DB;
import database.generated.public_.Tables;
import database.generated.public_.tables.daos.GraphPatternDao;
import database.generated.public_.tables.daos.GraphPatternStructureAnomalyDao;
import database.generated.public_.tables.pojos.GraphPattern;

import java.util.List;

import static org.jooq.impl.DSL.*;


public class GraphPatternsStructureAnomaliesDao extends GraphPatternStructureAnomalyDao {

    private H2DB storage;

    public GraphPatternsStructureAnomaliesDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }
}
