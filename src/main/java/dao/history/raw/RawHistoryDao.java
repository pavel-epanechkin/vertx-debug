package dao.history.raw;

import domain.RawMessageInfo;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public interface RawHistoryDao extends Closeable {

    RawMessageInfo getByMessageId(String id) throws NoSuchElementException, IOException;

    void foreach(RawMessageAction rawMessageAction);

    int getRecordsCount();

}
