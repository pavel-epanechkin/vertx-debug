package dao.history.processed;

import dao.H2DB;
import database.generated.public_.Tables;
import database.generated.public_.tables.daos.GraphPatternDao;
import database.generated.public_.tables.daos.SpanDao;
import database.generated.public_.tables.pojos.Graph;
import database.generated.public_.tables.pojos.GraphPattern;
import database.generated.public_.tables.pojos.TracePattern;

import java.util.List;

import static org.jooq.impl.DSL.*;


public class GraphPatternsDao extends GraphPatternDao {

    private H2DB storage;

    public GraphPatternsDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }

    public List<GraphPattern> getGraphPatternsBlock(int blockNumber, int itemsInBlock) {
        return storage.query()
                .select().from(Tables.GRAPH_PATTERN)
                .orderBy(Tables.GRAPH_PATTERN.PATTERN_ID)
                .offset(blockNumber * itemsInBlock)
                .limit(itemsInBlock)
                .fetchInto(GraphPattern.class);
    }

    public List<Integer> getGraphPatternTracePatternMessageLabels(Integer graphPatternId, Integer tracePatternIndex) {
        return storage.query()
            .select(Tables.MESSAGE.LABEL_ID).from(Tables.TRACE_PART, Tables.SPAN, Tables.TRACE_UNIT, Tables.MESSAGE)
            .where(
                and (
                    field(Tables.TRACE_PART.TRACE_ID).eq(
                        select(Tables.TRACE.TRACE_ID).from(Tables.TRACE, Tables.TRACE_PATTERN)
                        .where(
                            and (
                                field(Tables.TRACE.GRAPH_ID).eq(
                                    select(Tables.GRAPH.GRAPH_ID).from(Tables.GRAPH_PATTERN, Tables.GRAPH)
                                        .where(
                                            and(
                                                field(Tables.GRAPH_PATTERN.PATTERN_ID).eq(graphPatternId),
                                                field(Tables.GRAPH_PATTERN.PATTERN_ID).eq(field(Tables.GRAPH.GRAPH_PATTERN_ID))
                                            )
                                        )
                                        .limit(1)
                                ),
                                field(Tables.TRACE.TRACE_PATTERN_ID).eq(field(Tables.TRACE_PATTERN.PATTERN_ID))
                            )
                        )
                        .offset(tracePatternIndex)
                        .limit(1)
                    ),
                    field(Tables.TRACE_PART.SPAN_ID).eq(field(Tables.SPAN.SPAN_ID)),
                    field(Tables.SPAN.SPAN_ID).eq(field(Tables.TRACE_UNIT.SPAN_ID)),
                    field(Tables.TRACE_UNIT.MESSAGE_ID).eq(field(Tables.MESSAGE.ID))
                )
            )
            .fetchInto(Integer.class);
    }

}
