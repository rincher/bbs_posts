package hdyoon.bbs.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Configuration
public class DatasourceConfig {

  @Value("${aws.region:us-east-1}")
  private String awsRegion;

  @Value("${aws.secretsmanager.database-config-secret-name:}")
  private String databaseConfigSecretName;

  @Value("${aws.secretsmanager.credentials-secret-name:}")
  private String credentialsSecretName;

  @Bean
  @Primary
  @ConditionalOnProperty(name = "aws.secretsmanager.enabled", havingValue = "true")
  public DataSource dataSource() {
    try {
      // 빈 값 체크
      if (databaseConfigSecretName.isEmpty() || credentialsSecretName.isEmpty()) {
        throw new IllegalArgumentException("AWS Secrets Manager secret names must be provided when enabled");
      }

      // AWS SDK v2 클라이언트 생성
      SecretsManagerClient client = SecretsManagerClient.builder()
              .region(Region.of(awsRegion))
              .credentialsProvider(DefaultCredentialsProvider.create())
              .build();

      // 1. 데이터베이스 설정 정보 가져오기 (host, port, dbname)
      GetSecretValueRequest configRequest = GetSecretValueRequest.builder()
              .secretId(databaseConfigSecretName)
              .build();

      GetSecretValueResponse configResponse = client.getSecretValue(configRequest);
      String configSecretString = configResponse.secretString();

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode configJson = objectMapper.readTree(configSecretString);

      String host = configJson.get("host").asText();
      int port = configJson.get("port").asInt();
      String dbname = configJson.get("dbname").asText();

      // 2. 자격 증명 정보 가져오기 (username, password) - 7일마다 rotate
      GetSecretValueRequest credentialsRequest = GetSecretValueRequest.builder()
              .secretId(credentialsSecretName)
              .build();

      GetSecretValueResponse credentialsResponse = client.getSecretValue(credentialsRequest);
      String credentialsSecretString = credentialsResponse.secretString();

      JsonNode credentialsJson = objectMapper.readTree(credentialsSecretString);

      String username = credentialsJson.get("username").asText();
      String password = credentialsJson.get("password").asText();

      // 3. JDBC URL 생성
      String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, dbname);

      // 4. DataSource 생성
      return DataSourceBuilder
              .create()
              .url(jdbcUrl)
              .username(username)
              .password(password)
              .driverClassName("org.postgresql.Driver")
              .build();

    } catch (Exception e) {
      throw new RuntimeException(
              "Failed to retrieve database configuration from SecretsManager: " + e.getMessage(),
              e
      );
    }
  }

  @Bean
  @ConditionalOnProperty(name = "aws.secretsmanager.enabled", havingValue = "false", matchIfMissing = true)
  public DataSource localDataSource(
          @Value("${spring.datasource.url}") String url,
          @Value("${spring.datasource.username}") String username,
          @Value("${spring.datasource.password}") String password) {

    return DataSourceBuilder
            .create()
            .url(url)
            .username(username)
            .password(password)
            .driverClassName("org.postgresql.Driver")
            .build();
  }
}