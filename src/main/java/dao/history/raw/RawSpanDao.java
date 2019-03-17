package dao.history.raw;

import dao.history.raw.base.BaseNumberIdentifiableRawEntityDao;
import domain.RawSpan;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;

public class RawSpanDao extends BaseNumberIdentifiableRawEntityDao<RawSpan> {

    public RawSpanDao(RocksDB storage, ColumnFamilyHandle entityColumnHandle, Class entityClass) {
        super(storage, entityColumnHandle, entityClass);
    }
}