package dao;

import org.jooq.Condition;
import org.jooq.SQL;

public interface FilterMapper {

    Condition map(String field, Object value);

}
