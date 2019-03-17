package dao.history.raw;

import dao.history.raw.interfaces.Action;
import domain.RawMessageInfo;
import javafx.util.Pair;
import org.rocksdb.*;
import utils.Utils;

import java.io.IOException;
import java.util.NoSuchElementException;


public class RawHistoryDao {

    private RocksDB storage;

    private ColumnFamilyHandle sentColumnFamily;

    private ColumnFamilyHandle sentMessageIdToRecordIdColumnFamily;

    private ColumnFamilyHandle receivedColumnFamily;

    private ColumnFamilyHandle childsCountColumnFamily;

    private final String SENT_COUNT_KEY = "SENT_MESSAGES_COUNT";

    public RawHistoryDao(RocksDB storage, ColumnFamilyHandle sentColumnFamily, ColumnFamilyHandle sentMessageIdToRecordIdColumnFamily,
                         ColumnFamilyHandle receivedColumnFamily, ColumnFamilyHandle childsCountColumnFamily) {
        this.storage = storage;
        this.sentColumnFamily = sentColumnFamily;
        this.receivedColumnFamily = receivedColumnFamily;
        this.childsCountColumnFamily = childsCountColumnFamily;
        this.sentMessageIdToRecordIdColumnFamily = sentMessageIdToRecordIdColumnFamily;
    }

    public RawMessageInfo getSentMessageByRecordId(String id) {
        return getById(id, sentColumnFamily);
    }

    public RawMessageInfo getSentMessageByMessageId(String id) throws NoSuchElementException, IOException {
        try {
            byte[] recordId = storage.get(sentMessageIdToRecordIdColumnFamily, id.getBytes());
            return getById(recordId, sentColumnFamily);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RawMessageInfo getReceivedMessageById(String id) throws NoSuchElementException {
        return getById(id, receivedColumnFamily);
    }

    private RawMessageInfo getById(String id, ColumnFamilyHandle columnFamilyHandle) {
        return getById(id.getBytes(), columnFamilyHandle);
    }

    private RawMessageInfo getById(byte[] id, ColumnFamilyHandle columnFamilyHandle) {
        try {
            byte[] bytes = storage.get(columnFamilyHandle, id);
            return (RawMessageInfo) Utils.getObjectFromBytes(bytes, RawMessageInfo.class);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void foreachSent(Action<Integer, RawMessageInfo> action) {
        int recordsCount = getRecordsCount();
        for (Integer i = 1; i <= recordsCount; i++) {
            RawMessageInfo rawMessageInfo = getSentMessageByRecordId(i.toString());
            if (rawMessageInfo != null) {
                boolean needBreak = action.handleAndBreakIfNeeded(new Pair<>(i, rawMessageInfo));
                if (needBreak)
                    break;
            }
        }
    }

    public int getRecordsCount() {
        try {
            byte[] bytes = storage.get(SENT_COUNT_KEY.getBytes());
            return Integer.parseInt(new String(bytes));
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateMessageChildsCount(String messageId, Integer count) {
        try {
            storage.put(childsCountColumnFamily, messageId.getBytes(), count.toString().getBytes());
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    public int getMessageChildsCount(String messageId) {
        try {
            byte[] bytes = storage.get(childsCountColumnFamily, messageId.getBytes());
            if (bytes != null)
                return Integer.parseInt(new String(bytes));
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
