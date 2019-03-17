package dao.history.raw.base;

import dao.history.raw.interfaces.Action;
import javafx.util.Pair;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import utils.Utils;

public class BaseNumberIdentifiableRawEntityDao<V> extends BaseRawEntityDao<Integer, V> {

    private final byte[] entityRecordsCountKey = new byte[] {0};

    public BaseNumberIdentifiableRawEntityDao(RocksDB storage, ColumnFamilyHandle entityColumnHandle, Class entityClass) {
        super(storage, entityColumnHandle, entityClass);
    }

    @Override
    public Integer insert(V entity) {
        try {
            Integer newCount = getRecordsCount(entityColumnHandle) + 1;
            storage.put(entityColumnHandle, newCount.toString().getBytes(), Utils.getBytesFromObject(entity));
            updateRecordsCount(entityColumnHandle, newCount);
            return newCount;
        } catch (RocksDBException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public void foreach(Action<Integer, V> action) {
        int entitiesCount = getRecordsCount(entityColumnHandle);
        for (int i = 1; i <= entitiesCount; i++) {
            V rawEntity = get(i);
            if (rawEntity != null) {
                boolean needBreak = action.handleAndBreakIfNeeded(new Pair<>(i, rawEntity));
                if (needBreak)
                    break;
            }
        }
    }

    private int getRecordsCount(ColumnFamilyHandle columnFamilyHandle) {
        try {
            byte[] bytes = storage.get(columnFamilyHandle, entityRecordsCountKey);
            if (bytes != null)
                return Integer.parseInt(new String(bytes));
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void updateRecordsCount(ColumnFamilyHandle columnFamilyHandle, Integer count) {
        try {
            storage.put(columnFamilyHandle, entityRecordsCountKey, count.toString().getBytes());
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

}
