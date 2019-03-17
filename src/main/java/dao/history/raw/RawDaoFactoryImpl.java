package dao.history.raw;

import domain.RawSpan;
import domain.RawTrace;
import domain.RawTracePart;
import domain.RawTraceUnit;
import org.rocksdb.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RawDaoFactoryImpl implements dao.history.raw.interfaces.RawDaoFactory {

    private RocksDB storage;

    private DBOptions options;

    private ColumnFamilyHandle sentColumnFamily;

    private ColumnFamilyHandle sentMessageIdToRecordIdColumnFamily;

    private ColumnFamilyHandle receivedColumnFamily;

    private ColumnFamilyHandle childsCountColumnFamily;

    private ColumnFamilyHandle traceUnitsColumnFamily;

    private ColumnFamilyHandle spansColumnFamily;

    private ColumnFamilyHandle tracePartsColumnFamily;

    private ColumnFamilyHandle tracesColumnFamily;

    private final String SENT_COLUMN_FAMILY_NAME = "SENT_MESSAGES";

    private final String SENT_MESSAGEID_TO_RECORDID_COLUMN_FAMILY_NAME = "SENT_MESSAGEID_TO_RECORDID";

    private final String RECEIVED_COLUMN_FAMILY_NAME = "RECEIVED_MESSAGES";

    private final String CHILDS_COUNT_COLUMN_FAMILY_NAME = "CHILDS_COUNT";

    private final String TRACE_UNITS_COLUMN_FAMILY_NAME = "TRACE_UNITS";

    private final String SPANS_COLUMN_FAMILY_NAME = "SPANS";

    private final String TRACEPARTS_COLUMN_FAMILY_NAME = "TRACE_PARTS";

    private final String TRACES_COLUMN_FAMILY_NAME = "TRACESS";

    private final String DEFAULT_COLUMN_FAMILY_NAME = "default";

    public RawDaoFactoryImpl(@NotNull String storagePath) {
        RocksDB.loadLibrary();
        options = new DBOptions().setCreateIfMissing(true).setCreateMissingColumnFamilies(true);
        try {
            List<ColumnFamilyHandle> columnFamilyHandles = new ArrayList<>();
            List<ColumnFamilyDescriptor> columnFamilyDescriptors = Arrays.asList(
                    new ColumnFamilyDescriptor(SENT_COLUMN_FAMILY_NAME.getBytes()),
                    new ColumnFamilyDescriptor(SENT_MESSAGEID_TO_RECORDID_COLUMN_FAMILY_NAME.getBytes()),
                    new ColumnFamilyDescriptor(RECEIVED_COLUMN_FAMILY_NAME.getBytes()),
                    new ColumnFamilyDescriptor(CHILDS_COUNT_COLUMN_FAMILY_NAME.getBytes()),
                    new ColumnFamilyDescriptor(TRACE_UNITS_COLUMN_FAMILY_NAME.getBytes()),
                    new ColumnFamilyDescriptor(SPANS_COLUMN_FAMILY_NAME.getBytes()),
                    new ColumnFamilyDescriptor(TRACEPARTS_COLUMN_FAMILY_NAME.getBytes()),
                    new ColumnFamilyDescriptor(TRACES_COLUMN_FAMILY_NAME.getBytes()),
                    new ColumnFamilyDescriptor(DEFAULT_COLUMN_FAMILY_NAME.getBytes())
            );

            storage = RocksDB.open(options, storagePath, columnFamilyDescriptors, columnFamilyHandles);

            sentColumnFamily = columnFamilyHandles.get(0);
            sentMessageIdToRecordIdColumnFamily = columnFamilyHandles.get(1);
            receivedColumnFamily = columnFamilyHandles.get(2);
            childsCountColumnFamily = columnFamilyHandles.get(3);
            traceUnitsColumnFamily = columnFamilyHandles.get(4);
            spansColumnFamily = columnFamilyHandles.get(5);
            tracePartsColumnFamily = columnFamilyHandles.get(6);
            tracesColumnFamily = columnFamilyHandles.get(7);

        } catch (RocksDBException err) {
            err.printStackTrace();
        }
    }
    @Override
    public RawHistoryDao getMessageHistoryDao() {
        return new RawHistoryDao(storage, sentColumnFamily,
                sentMessageIdToRecordIdColumnFamily, receivedColumnFamily, childsCountColumnFamily);
    }

    @Override
    public RawTraceUnitDao getTraceUnitsDao() {
        return new RawTraceUnitDao(storage, traceUnitsColumnFamily, RawTraceUnit.class);
    }

    @Override
    public RawSpanDao getSpansDao() {
        return new RawSpanDao(storage, spansColumnFamily, RawSpan.class);
    }

    @Override
    public RawTracePartDao getTracePartsDao() {
        return new RawTracePartDao(storage, tracePartsColumnFamily, RawTracePart.class);
    }

    @Override
    public RawTraceDao getTracesDao() {
        return new RawTraceDao(storage, tracesColumnFamily, RawTrace.class);
    }


    public void close() throws IOException {
        sentColumnFamily.close();
        receivedColumnFamily.close();
        options.close();
        storage.close();
    }
}
