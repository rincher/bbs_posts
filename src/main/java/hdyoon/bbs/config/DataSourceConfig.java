package hdyoon.bbs.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @Profile("local")
    @Qualifier("writerDataSource")
    public DataSource localWriterDataSource(
            @Value("${spring.datasource.writer.url}") String url,
            @Value("${spring.datasource.writer.username}") String username,
            @Value("${spring.datasource.writer.password}") String password) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000);
        return new HikariDataSource(config);
    }

    @Bean
    @Profile("local")
    @Qualifier("readerDataSource")
    public DataSource localReaderDataSource(
            @Value("${spring.datasource.reader.url}") String url,
            @Value("${spring.datasource.reader.username}") String username,
            @Value("${spring.datasource.reader.password}") String password) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(15);
        config.setConnectionTimeout(30000);
        return new HikariDataSource(config);
    }

    @Bean
    @Profile("stage")
    @Qualifier("writerDataSource")
    public DataSource stageWriterDataSource(
            @Value("${spring.datasource.writer.url}") String url,
            @Value("${spring.datasource.writer.username}") String username,
            @Value("${spring.datasource.writer.password}") String password) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(20);
        config.setConnectionTimeout(30000);
        return new HikariDataSource(config);
    }

    @Bean
    @Profile("stage")
    @Qualifier("readerDataSource")
    public DataSource stageReaderDataSource(
            @Value("${spring.datasource.reader.url}") String url,
            @Value("${spring.datasource.reader.username}") String username,
            @Value("${spring.datasource.reader.password}") String password) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(30);
        config.setConnectionTimeout(30000);
        return new HikariDataSource(config);
    }

    @Bean
    @Profile("prod")
    @Qualifier("writerDataSource")
    public DataSource productionWriterDataSource(
            @Value("${DB_WRITER_URL}") String url,
            @Value("${DB_WRITER_USERNAME}") String username,
            @Value("${DB_WRITER_PASSWORD}") String password) {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(20000);
        config.setIdleTimeout(300000);package hdyoon.bbs.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;

        @Configuration
        public class DataSourceConfig {

            @Bean
            @Profile("local")
            @Qualifier("writerDataSource")
            public DataSource localWriterDataSource(
                    @Value("${spring.datasource.writer.url}") String url,
                    @Value("${spring.datasource.writer.username}") String username,
                    @Value("${spring.datasource.writer.password}") String password) {

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(url);
                config.setUsername(username);
                config.setPassword(password);
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                config.setMaximumPoolSize(10);
                config.setConnectionTimeout(30000);
                return new HikariDataSource(config);
            }

            @Bean
            @Profile("local")
            @Qualifier("readerDataSource")
            public DataSource localReaderDataSource(
                    @Value("${spring.datasource.reader.url}") String url,
                    @Value("${spring.datasource.reader.username}") String username,
                    @Value("${spring.datasource.reader.password}") String password) {

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(url);
                config.setUsername(username);
                config.setPassword(password);
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                config.setMaximumPoolSize(15);
                config.setConnectionTimeout(30000);
                return new HikariDataSource(config);
            }

            @Bean
            @Profile("stage")
            @Qualifier("writerDataSource")
            public DataSource stageWriterDataSource(
                    @Value("${spring.datasource.writer.url}") String url,
                    @Value("${spring.datasource.writer.username}") String username,
                    @Value("${spring.datasource.writer.password}") String password) {

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(url);
                config.setUsername(username);
                config.setPassword(password);
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                config.setMaximumPoolSize(20);
                config.setConnectionTimeout(30000);
                return new HikariDataSource(config);
            }

            @Bean
            @Profile("stage")
            @Qualifier("readerDataSource")
            public DataSource stageReaderDataSource(
                    @Value("${spring.datasource.reader.url}") String url,
                    @Value("${spring.datasource.reader.username}") String username,
                    @Value("${spring.datasource.reader.password}") String password) {

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(url);
                config.setUsername(username);
                config.setPassword(password);
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                config.setMaximumPoolSize(30);
                config.setConnectionTimeout(30000);
                return new HikariDataSource(config);
            }

            @Bean
            @Profile("prod")
            @Qualifier("writerDataSource")
            public DataSource productionWriterDataSource(
                    @Value("${DB_WRITER_URL}") String url,
                    @Value("${DB_WRITER_USERNAME}") String username,
                    @Value("${DB_WRITER_PASSWORD}") String password) {

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(url);
                config.setUsername(username);
                config.setPassword(password);
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                config.setMaximumPoolSize(10);
                config.setMinimumIdle(2);
                config.setConnectionTimeout(20000);
                config.setIdleTimeout(300000);
                config.setMaxLifetime(1200000);
                return new HikariDataSource(config);
            }

            @Bean
            @Profile("prod")
            @Qualifier("readerDataSource")
            public DataSource productionReaderDataSource(
                    @Value("${DB_READER_URL}") String url,
                    @Value("${DB_READER_USERNAME}") String username,
                    @Value("${DB_READER_PASSWORD}") String password) {

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(url);
                config.setUsername(username);
                config.setPassword(password);
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                config.setMaximumPoolSize(15);
                config.setMinimumIdle(3);
                config.setConnectionTimeout(20000);
                config.setIdleTimeout(300000);
                config.setMaxLifetime(1200000);
                return new HikariDataSource(config);
            }

            @Bean
            @Primary
            public DataSource routingDataSource(
                    @Qualifier("writerDataSource") DataSource writerDataSource,
                    @Qualifier("readerDataSource") DataSource readerDataSource) {

                RoutingDataSource routingDataSource = new RoutingDataSource();
                routingDataSource.setWriterDataSource(writerDataSource);
                routingDataSource.setReaderDataSource(readerDataSource);
                return new LazyConnectionDataSourceProxy(routingDataSource);
            }
        }
        config.setMaxLifetime(1200000);
        return new HikariDataSource(config);
    }
}