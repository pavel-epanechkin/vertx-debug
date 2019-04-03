package dao.history.processed;

import dao.H2DB;
import database.generated.public_.Tables;
import database.generated.public_.tables.daos.GraphDao;
import database.generated.public_.tables.pojos.Graph;


import java.util.List;

import static org.jooq.impl.DSL.and;
import static org.jooq.impl.DSL.field;


public class GraphsDao extends GraphDao {

    private H2DB storage;

    public GraphsDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }

    public List<Graph> getGraphsBlock(int blockNumber, int itemsInBlock) {
        return storage.query()
                .select().from(Tables.GRAPH)
                .orderBy(Tables.GRAPH.GRAPH_ID)
                .offset(blockNumber * itemsInBlock)
                .limit(itemsInBlock)
                .fetchInto(Graph.class);
    }

    public List<Integer> getGraphTracePatternsIds(Integer graphId) {
        return storage.query()
                .select(Tables.TRACE_PATTERN.PATTERN_ID).from(Tables.GRAPH, Tables.TRACE, Tables.TRACE_PATTERN)
                .where(
                        and(
                                field(Tables.GRAPH.GRAPH_ID).eq(graphId),
                                field(Tables.GRAPH.GRAPH_ID).eq(field(Tables.TRACE.GRAPH_ID)),
                                field(Tables.TRACE.TRACE_PATTERN_ID).eq(field(Tables.TRACE_PATTERN.PATTERN_ID))
                        )
                )
                .orderBy(Tables.TRACE_PATTERN.PATTERN_ID)
                .fetchInto(Integer.class);
    }
}
