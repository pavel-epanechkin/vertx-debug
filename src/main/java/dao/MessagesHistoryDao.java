package dao;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public interface MessagesHistoryDao extends Closeable {

    MessageInfo getByRecordId(Integer id) throws NoSuchElementException, IOException;

    List<MessageInfo> getByEqFilter(Map<String, Object> filterMap, Long startDate, Long endDate) throws IOException;

    int getCountByEqFilter(Map<String, Object> filterMap, Long startDate, Long endDate) throws IOException;

    List<MessageInfo> getByEqFilterWithLimit(Map<String, Object> filterMap, LimitParams limitParams,
                                             Long startDate, Long endDate) throws IOException;

    List<MessageInfo> getByLikeFilter(Map<String, Object> filterMap, Long startDate, Long endDate) throws IOException;

    int getCountByLikeFilter(Map<String, Object> filterMap, Long startDate, Long endDate) throws IOException;

    List<MessageInfo> getByLikeFilterWithLimit(Map<String, Object> filterMap, LimitParams limitParams,
                                             Long startDate, Long endDate) throws IOException;

}
