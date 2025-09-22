package hdyoon.bbs.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private static final String WRITER = "writer";
    private static final String READER = "reader";

    private final Map<Object, Object> targetDataSources = new HashMap<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? READER : WRITER;
    }

    public void setWriterDataSource(DataSource writerDataSource) {
        targetDataSources.put(WRITER, writerDataSource);
        setTargetDataSources(targetDataSources);
        setDefaultTargetDataSource(writerDataSource);
    }

    public void setReaderDataSource(DataSource readerDataSource) {
        targetDataSources.put(READER, readerDataSource);
        setTargetDataSources(targetDataSources);
        afterPropertiesSet();
    }
}
