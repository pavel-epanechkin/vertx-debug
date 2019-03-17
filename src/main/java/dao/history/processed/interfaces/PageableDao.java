package dao.history.processed.interfaces;

import dao.FilterMapper;
import dao.H2DB;
import dao.LimitParams;
import database.generated.public_.Tables;
import database.generated.public_.tables.pojos.Message;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.TableLike;
import org.jooq.impl.DSL;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.and;
import static org.jooq.impl.DSL.field;

public interface PageableDao<T> {

    DSLContext getDSLContext();

    Class getEntityClass();

    TableLike getEntityTable();

    default List<T> getByLikeFilter(Map<String, Object> filterMap) throws IOException {
        Condition filter = prepareLikeSelectionFilter(filterMap);
        return getByFilter(filter);
    }

    default int getCountByLikeFilter(Map<String, Object> filterMap) throws IOException {
        Condition filter = prepareLikeSelectionFilter(filterMap);
        return getCountByFilter(filter);
    }

    default List<T> getByLikeFilterWithLimit(Map<String, Object> filterMap, LimitParams limitParams) throws IOException {
        Condition filter = prepareLikeSelectionFilter(filterMap);
        return getByFilterWithLimit(filter, limitParams);
    }

    default List<T> getByFilter(Condition sqlFilter) throws IOException {
        List entities = getDSLContext()
                .select().from(getEntityTable())
                .where(sqlFilter)
                .fetchInto(getEntityClass());
        return entities;
    }

    default List<T> getByFilterWithLimit(Condition sqlFilter, LimitParams limitParams) throws IOException {
        List messages = getDSLContext()
                .select().from(getEntityTable())
                .where(sqlFilter)
                .offset(limitParams.getOffset())
                .limit(limitParams.getCount())
                .fetchInto(getEntityClass());
        return messages;
    }

    default int getCountByFilter(Condition filter) {
        return getDSLContext()
                .selectCount().from(getEntityTable())
                .where(filter)
                .fetch()
                .get(0).value1();
    }

    default Condition prepareLikeSelectionFilter(Map<String, Object> filterMap) {
        Condition mainFilter = prepareLikeFilter(filterMap);
        return mainFilter;
    }

    default Condition prepareLikeFilter(Map<String, Object> filterMap) {
        return prepareFilter(filterMap, (field, value) -> DSL.cast(field(field), String.class).likeIgnoreCase(value + "%"));
    }

    default Condition prepareFilter(Map<String, Object> filterMap, FilterMapper filterMapper) {
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
}
