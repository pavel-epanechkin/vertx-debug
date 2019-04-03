package dao.history.processed;

import dao.H2DB;
import database.generated.public_.Tables;
import database.generated.public_.tables.daos.SpanDao;
import database.generated.public_.tables.pojos.Message;
import database.generated.public_.tables.pojos.Span;

import java.util.List;

import static org.jooq.impl.DSL.and;
import static org.jooq.impl.DSL.field;


public class SpansDao extends SpanDao {

    private H2DB storage;

    public SpansDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }

    public List<Span> getSpansBlock(int blockNumber, int itemsInBlock) {
        return storage.query()
                .select().from(Tables.SPAN)
                .orderBy(Tables.SPAN.SPAN_ID)
                .offset(blockNumber * itemsInBlock)
                .limit(itemsInBlock)
                .fetchInto(Span.class);
    }

    public List<String> getSpanMessagesLabels(Integer spanId) {
        return storage.query()
                .select(Tables.MESSAGE.LABEL).from(Tables.SPAN, Tables.TRACE_UNIT, Tables.MESSAGE)
                .where(
                    and(
                        field(Tables.SPAN.SPAN_ID).eq(spanId),
                        field(Tables.SPAN.SPAN_ID).eq(field(Tables.TRACE_UNIT.SPAN_ID)),
                        field(Tables.TRACE_UNIT.MESSAGE_ID).eq(field(Tables.MESSAGE.ID))
                    )
                )
                .orderBy(Tables.TRACE_UNIT.ORDER_NUM)
                .fetchInto(String.class);
    }

}
