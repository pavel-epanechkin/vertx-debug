package dao.history.raw.base;

import dao.history.raw.interfaces.Action;
import dao.history.raw.interfaces.RawEntityDao;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import utils.Utils;

public abstract class BaseRawEntityDao<K, V> implements RawEntityDao<K,V> {

    protected RocksDB storage;

    protected ColumnFamilyHandle entityColumnHandle;

    protected Class entityClass;

    public BaseRawEntityDao(RocksDB storage, ColumnFamilyHandle entityColumnHandle, Class entityClass) {
        this.storage = storage;
        this.entityColumnHandle = entityColumnHandle;
        this.entityClass = entityClass;
    }

    @Override
    public abstract K insert(V entity);

    @Override
    public K insert(K id, V entity) {
        try {
            storage.put(entityColumnHandle, id.toString().getBytes(), Utils.getBytesFromObject(entity));
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void update(K id, V entity) {
        try {
            byte[] idBytes = id.toString().getBytes();
            storage.delete(entityColumnHandle, idBytes);
            storage.put(entityColumnHandle, idBytes, Utils.getBytesFromObject(entity));
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public V get(K id) {
        try {
            byte[] bytes = storage.get(entityColumnHandle, id.toString().getBytes());
            return (V) Utils.getObjectFromBytes(bytes, entityClass);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public abstract void foreach(Action<K, V> action);
}
