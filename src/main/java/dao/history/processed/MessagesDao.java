package dao.history.processed;

import dao.H2DB;
import dao.LimitParams;
import dao.history.processed.interfaces.PageableDao;
import database.generated.public_.Tables;
import database.generated.public_.tables.daos.MessageDao;
import database.generated.public_.tables.pojos.Message;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.TableLike;

import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.and;
import static org.jooq.impl.DSL.field;


public class MessagesDao extends MessageDao implements PageableDao<Message> {

    private H2DB storage;

    public MessagesDao(H2DB storage) {
        super(storage.query().configuration());
        this.storage = storage;
    }

    @Override
    public DSLContext getDSLContext() {
        return storage.query();
    }

    @Override
    public Class getEntityClass() {
        return Message.class;
    }

    @Override
    public TableLike getEntityTable() {
        return Tables.MESSAGE;
    }

    public List<Message> getMessagesByTraceId(Integer traceId, Map<String, Object> filterMap, LimitParams limitParams) {
        Condition filter = prepareLikeSelectionFilter(filterMap);
        return storage.query()
                .select().from(Tables.TRACE_PART, Tables.TRACE_UNIT, Tables.MESSAGE)
                .where(
                    and(
                        filter,
                        field(Tables.TRACE_PART.TRACE_ID).eq(traceId),
                        field(Tables.TRACE_PART.SPAN_ID).eq(field(Tables.TRACE_UNIT.SPAN_ID)),
                        field(Tables.TRACE_UNIT.MESSAGE_ID).eq(field(Tables.MESSAGE.ID))
                    )
                )
                .orderBy(Tables.TRACE_PART.ORDER_NUMBER, Tables.TRACE_UNIT.ORDER_NUM)
                .offset(limitParams.getOffset())
                .limit(limitParams.getCount())
                .fetchInto(Message.class);
    }

    public int getMessagesCountByTraceId(Integer traceId) {
        return storage.query()
                .selectCount().from(Tables.TRACE_PART, Tables.TRACE_UNIT, Tables.MESSAGE)
                .where(
                        and(
                                field(Tables.TRACE_PART.TRACE_ID).eq(traceId),
                                field(Tables.TRACE_PART.SPAN_ID).eq(field(Tables.TRACE_UNIT.SPAN_ID)),
                                field(Tables.TRACE_UNIT.MESSAGE_ID).eq(field(Tables.MESSAGE.ID))
                        )
                )
                .fetch()
                .get(0).value1();
    }

    public Message fetchOneByLabel(String label) {
        return storage.query()
                .select().from(Tables.MESSAGE)
                .where(
                    field(Tables.MESSAGE.LABEL).eq(label)

                )
                .limit(1)
                .fetchOneInto(Message.class);
    }

}
