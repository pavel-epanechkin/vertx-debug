package dao.history.raw;

import domain.RawMessageInfo;

public interface RawMessageAction {

    boolean handleAndBreakIfNeeded(RawMessageInfo rawMessageInfo);

}
