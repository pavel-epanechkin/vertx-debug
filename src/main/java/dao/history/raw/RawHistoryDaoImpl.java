package dao.history.raw;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.RawMessageInfo;
import org.rocksdb.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;


public class RawHistoryDaoImpl implements RawHistoryDao {

    private RocksDB storage;

    private Options options;

    private final String COUNT_KEY = "RECORDS_COUNT";

    public RawHistoryDaoImpl(@NotNull String storagePath) {
        RocksDB.loadLibrary();
        options = new Options().setCreateIfMissing(true);
        try {
            storage = RocksDB.open(options, storagePath);
        } catch (RocksDBException err) {
            err.printStackTrace();
        }
    }

    @Override
    public RawMessageInfo getByMessageId(String id) {
        try {
            byte[] bytes = storage.get(id.getBytes());
            return getRawMessageFromBytes(bytes);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void foreach(RawMessageAction rawMessageAction) {
        int recordsCount = getRecordsCount();
        for (Integer i = 1; i <= recordsCount; i++) {
            RawMessageInfo rawMessageInfo = getByMessageId(i.toString());
            if (rawMessageInfo != null) {
                boolean needBreak = rawMessageAction.handleAndBreakIfNeeded(rawMessageInfo);
                if (needBreak)
                    break;
            }
        }
    }

    @Override
    public int getRecordsCount() {
        try {
            byte[] bytes = storage.get(COUNT_KEY.getBytes());
            return Integer.parseInt(new String(bytes));
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private RawMessageInfo getRawMessageFromBytes(byte[] bytes) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(bytes, RawMessageInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void close() throws IOException {
        options.close();
        storage.close();
    }
}
