package dao.history.processed;

import dao.H2DB;
import dao.LimitParams;
import dao.history.processed.interfaces.PageableDao;
import database.generated.public_.Tables;
import database.generated.public_.tables.daos.TraceDao;
import database.generated.public_.tables.pojos.Span;
import database.generated.public_.tables.pojos.Trace;
import dto.db.TracePartInfo;
import org.jooq.DSLContext;
import org.jooq.TableLike;

import java.util.List;

import static org.jooq.impl.DSL.and;
import static org.jooq.impl.DSL.field;


public class TracesDao extends TraceDao implements PageableDao<Trace> {

    private H2DB storage;

    public TracesDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }

    @Override
    public DSLContext getDSLContext() {
        return storage.query();
    }

    @Override
    public Class getEntityClass() {
        return Trace.class;
    }

    @Override
    public TableLike getEntityTable() {
        return Tables.TRACE;
    }

    public List<Trace> getTracesForMessage(Integer messageId, LimitParams limitParams) {
        return storage.query()
                .select().from(Tables.TRACE, Tables.TRACE_PART, Tables.TRACE_UNIT)
                .where(
                        and(
                                field(Tables.TRACE.TRACE_ID).eq(field(Tables.TRACE_PART.TRACE_ID)),
                                field(Tables.TRACE_PART.SPAN_ID).eq(field(Tables.TRACE_UNIT.SPAN_ID)),
                                field(Tables.TRACE_UNIT.MESSAGE_ID).eq(messageId)
                        )
                )
                .orderBy(Tables.TRACE.TRACE_ID)
                .offset(limitParams.getOffset())
                .limit(limitParams.getCount())
                .fetchInto(Trace.class);
    }

    public int getTracesForMessageCount(Integer messageId) {
        return storage.query()
                .selectCount().from(Tables.TRACE, Tables.TRACE_PART, Tables.TRACE_UNIT)
                .where(
                        and(
                                field(Tables.TRACE.TRACE_ID).eq(field(Tables.TRACE_PART.TRACE_ID)),
                                field(Tables.TRACE_PART.SPAN_ID).eq(field(Tables.TRACE_UNIT.SPAN_ID)),
                                field(Tables.TRACE_UNIT.MESSAGE_ID).eq(messageId)
                        )
                )
                .fetch()
                .get(0).value1();
    }

    public List<TracePartInfo> getTracesPartsInfo(List<Integer> traceIds) {
        return storage.query()
                .select().from(Tables.TRACE, Tables.TRACE_PART, Tables.SPAN)
                .where(
                        and(
                                field(Tables.TRACE.TRACE_ID).in(traceIds),
                                field(Tables.TRACE.TRACE_ID).eq(field(Tables.TRACE_PART.TRACE_ID)),
                                field(Tables.TRACE_PART.SPAN_ID).eq(field(Tables.SPAN.SPAN_ID))
                        )
                )
                .orderBy(Tables.TRACE.TRACE_ID, Tables.TRACE_PART.ORDER_NUMBER)
                .fetchInto(TracePartInfo.class);
    }

    public List<Trace> getTracesBlock(int blockNumber, int itemsInBlock) {
        return storage.query()
                .select().from(Tables.TRACE)
                .orderBy(Tables.TRACE.TRACE_ID)
                .offset(blockNumber * itemsInBlock)
                .limit(itemsInBlock)
                .fetchInto(Trace.class);
    }

    public List<Integer> getTraceSpanPatternsIds(Integer traceId) {
        return storage.query()
                .select(Tables.SPAN_PATTERN.PATTERN_ID).from(Tables.TRACE, Tables.TRACE_PART, Tables.SPAN, Tables.SPAN_PATTERN)
                .where(
                        and(
                                field(Tables.TRACE.TRACE_ID).eq(traceId),
                                field(Tables.TRACE.TRACE_ID).eq(field(Tables.TRACE_PART.TRACE_ID)),
                                field(Tables.TRACE_PART.SPAN_ID).eq(field(Tables.SPAN.SPAN_ID)),
                                field(Tables.SPAN.SPAN_PATTERN_ID).eq(field(Tables.SPAN_PATTERN.PATTERN_ID))
                        )
                )
                .orderBy(Tables.TRACE_PART.ORDER_NUMBER)
                .fetchInto(Integer.class);
    }

}
