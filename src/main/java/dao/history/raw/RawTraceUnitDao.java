package dao.history.raw;

import dao.history.raw.base.BaseStringIdentifiableRawEntityDao;
import domain.RawTraceUnit;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;

public class RawTraceUnitDao extends BaseStringIdentifiableRawEntityDao<RawTraceUnit> {

    public RawTraceUnitDao(RocksDB storage, ColumnFamilyHandle entityColumnHandle, Class entityClass) {
        super(storage, entityColumnHandle, entityClass);
    }
}