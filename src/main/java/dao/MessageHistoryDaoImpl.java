package dao;

import org.jooq.Condition;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.jooq.impl.DSL.and;
import static org.jooq.impl.DSL.field;


public class MessageHistoryDaoImpl implements MessagesHistoryDao {

    private H2DB storage;

    private final String INIT_SCRIPT_PATH = "src/main/resources/init_history_db.sql";

    private final String MESSAGES_TABLE = "messages";

    private final String DATE_FIELD_NAME = "timestamp";

    public MessageHistoryDaoImpl(@NotNull String storagePath) {
        storage = new H2DB(storagePath, INIT_SCRIPT_PATH);
    }

    @Override
    public MessageInfo getByRecordId(Integer id) throws NoSuchElementException, IOException {
        MessageInfo messageInfo = storage.query()
                .select().from(MESSAGES_TABLE)
                .where(field("recordId").eq(id))
                .limit(1)
                .fetchOneInto(MessageInfo.class);

        return messageInfo;
    }

    @Override
    public List<MessageInfo> getByEqFilter(Map<String, Object> filterMap, Long startDate, Long endDate) throws IOException {
        Condition filter = prepareEqSelectionFilter(filterMap, startDate, endDate);
        return getByFilter(filter);
    }

    @Override
    public int getCountByEqFilter(Map<String, Object> filterMap, Long startDate, Long endDate) throws IOException {
        Condition filter = prepareEqSelectionFilter(filterMap, startDate, endDate);
        return getCountByFilter(filter);
    }

    @Override
    public List<MessageInfo> getByEqFilterWithLimit(Map<String, Object> filterMap, LimitParams limitParams, Long startDate, Long endDate) throws IOException {
        Condition filter = prepareEqSelectionFilter(filterMap, startDate, endDate);
        return getByFilterWithLimit(filter, limitParams);
    }

    @Override
    public List<MessageInfo> getByLikeFilter(Map<String, Object> filterMap, Long startDate, Long endDate) throws IOException {
        Condition filter = prepareLikeSelectionFilter(filterMap, startDate, endDate);
        return getByFilter(filter);
    }

    @Override
    public int getCountByLikeFilter(Map<String, Object> filterMap, Long startDate, Long endDate) throws IOException {
        Condition filter = prepareLikeSelectionFilter(filterMap, startDate, endDate);
        return getCountByFilter(filter);
    }

    @Override
    public List<MessageInfo> getByLikeFilterWithLimit(Map<String, Object> filterMap, LimitParams limitParams, Long startDate, Long endDate) throws IOException {
        Condition filter = prepareLikeSelectionFilter(filterMap, startDate, endDate);
        return getByFilterWithLimit(filter, limitParams);
    }

    @Override
    public void close() throws IOException {
        storage.close();
    }

    private List<MessageInfo> getByFilter(Condition sqlFilter) throws IOException {
        List<MessageInfo> messageInfos = storage.query()
                .select().from(MESSAGES_TABLE)
                .where(sqlFilter)
                .fetchInto(MessageInfo.class);
        return messageInfos;
    }

    private List<MessageInfo> getByFilterWithLimit(Condition sqlFilter, LimitParams limitParams) throws IOException {
        List<MessageInfo> messageInfos = storage.query()
                .select().from(MESSAGES_TABLE)
                .where(sqlFilter)
                .offset(limitParams.getOffset())
                .limit(limitParams.getCount())
                .fetchInto(MessageInfo.class);
        return messageInfos;
    }

    private int getCountByFilter(Condition filter) {
        return storage.query()
                .selectCount().from(MESSAGES_TABLE)
                .where(filter)
                .fetch()
                .get(0).value1();
    }

    private Condition prepareEqSelectionFilter(Map<String, Object> filterMap, Long startDate, Long endDate) {
        Condition mainFilter = prepareEqFilter(filterMap);
        return prepareSelectionFilter(mainFilter, startDate, endDate);
    }

    private Condition prepareLikeSelectionFilter(Map<String, Object> filterMap, Long startDate, Long endDate) {
        Condition mainFilter = prepareLikeFilter(filterMap);
        return prepareSelectionFilter(mainFilter, startDate, endDate);
    }

    private Condition prepareSelectionFilter(Condition mainFilter, Long startDate, Long endDate) {
        Condition dateFilter = prepareDateFilter(startDate, endDate);

        if (mainFilter != null && dateFilter != null)
            return and(mainFilter, dateFilter);
        else if (mainFilter != null)
            return mainFilter;
        if (dateFilter != null)
            return dateFilter;

        return null;
    }

    private Condition prepareEqFilter(Map<String, Object> filterMap) {
        return prepareFilter(filterMap, (field, value) -> field(field).eq(value));
    }

    private Condition prepareLikeFilter(Map<String, Object> filterMap) {
        return prepareFilter(filterMap, (field, value) -> field(field).like(value + "%"));
    }

    private Condition prepareFilter(Map<String, Object> filterMap, FilterMapper filterMapper) {
        Condition[] filters = new Condition[filterMap.size()];

        int i = 0;
        for (Map.Entry entry : filterMap.entrySet()) {
            Condition filter = filterMapper.map(entry.getKey().toString(), entry.getValue());
            filters[i++] = filter;
        }
        if (filters.length > 0)
            return and(filters);

        return null;
    }

    private Condition prepareDateFilter(Long startDate, Long endDate) {
        if (startDate != null && endDate != null) {
            return and(field(DATE_FIELD_NAME).between(startDate, endDate));
        }
        else if (startDate != null) {
            return field(DATE_FIELD_NAME).ge(startDate);
        }
        else if (endDate != null) {
            return field(DATE_FIELD_NAME).le(endDate);
        }
        return null;
    }
}
