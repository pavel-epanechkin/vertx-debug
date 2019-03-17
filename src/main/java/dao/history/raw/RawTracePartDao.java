package dao.history.raw;

import dao.history.raw.base.BaseNumberIdentifiableRawEntityDao;
import domain.RawTracePart;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;

public class RawTracePartDao extends BaseNumberIdentifiableRawEntityDao<RawTracePart> {

    public RawTracePartDao(RocksDB storage, ColumnFamilyHandle entityColumnHandle, Class entityClass) {
        super(storage, entityColumnHandle, entityClass);
    }
}