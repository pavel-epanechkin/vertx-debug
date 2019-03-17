package dao.history.raw.interfaces;

import dao.history.raw.*;

public interface RawDaoFactory {

    RawHistoryDao getMessageHistoryDao();

    RawTraceUnitDao getTraceUnitsDao();

    RawSpanDao getSpansDao();

    RawTracePartDao getTracePartsDao();

    RawTraceDao getTracesDao();

}
