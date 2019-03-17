package dao.history.raw.base;

import dao.history.raw.interfaces.Action;
import javafx.util.Pair;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksIterator;
import utils.Utils;

import javax.ws.rs.NotSupportedException;

public class BaseStringIdentifiableRawEntityDao<V> extends BaseRawEntityDao<String, V> {

    public BaseStringIdentifiableRawEntityDao(RocksDB storage, ColumnFamilyHandle entityColumnHandle, Class entityClass) {
        super(storage, entityColumnHandle, entityClass);
    }

    @Override
    public String insert(V entity) {
        throw new NotSupportedException("Method is not supported");
    }

    @Override
    public void foreach(Action<String, V> action) {
        RocksIterator iterator = storage.newIterator(entityColumnHandle);
        iterator.seekToFirst();
        while (iterator.isValid()) {
            String id = new String(iterator.key());
            V entity = (V) Utils.getObjectFromBytes(iterator.value(), entityClass);
            boolean needBreak = action.handleAndBreakIfNeeded(new Pair<>(id, entity));
            if (needBreak)
                break;
            iterator.next();
        }
    }
}
