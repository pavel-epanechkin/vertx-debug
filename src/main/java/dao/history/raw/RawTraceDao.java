package dao.history.raw;

import dao.history.raw.base.BaseNumberIdentifiableRawEntityDao;
import domain.RawTrace;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;

public class RawTraceDao extends BaseNumberIdentifiableRawEntityDao<RawTrace> {

    public RawTraceDao(RocksDB storage, ColumnFamilyHandle entityColumnHandle, Class entityClass) {
        super(storage, entityColumnHandle, entityClass);
    }
}