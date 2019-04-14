package dao.history.processed;

import dao.H2DB;
import dao.LimitParams;
import dao.history.processed.interfaces.PageableDao;
import database.generated.public_.Tables;
import database.generated.public_.tables.daos.TraceDao;
import database.generated.public_.tables.daos.TraceLabelDao;
import database.generated.public_.tables.pojos.Trace;
import database.generated.public_.tables.pojos.TraceLabel;
import dto.db.TracePartInfo;
import org.jooq.DSLContext;
import org.jooq.TableLike;

import java.util.List;

import static org.jooq.impl.DSL.and;
import static org.jooq.impl.DSL.field;


public class TraceLabelsDao extends TraceLabelDao {

    private H2DB storage;

    public TraceLabelsDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }

}
