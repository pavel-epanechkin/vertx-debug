package dao.history.raw.interfaces;

import dao.history.raw.interfaces.Action;

public interface RawEntityDao<K, V> {

    K insert(V entity);

    K insert(K id, V entity);

    void update(K id, V entity);

    V get(K id);

    void foreach(Action<K, V> action);
}