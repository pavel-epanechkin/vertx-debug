package dao.history.raw.interfaces;

import domain.RawMessageInfo;
import javafx.util.Pair;

public interface Action<K, V> {

    boolean handleAndBreakIfNeeded(Pair<K, V> pair);

}
