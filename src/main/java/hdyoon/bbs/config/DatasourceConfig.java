package hdyoon.bbs.config;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatasourceConfig {

  @Value("${aws.region:ap-northeast-2}")
  private String awsRegion;

  @Value("${aws.secretsmanager.secret-name}")
  private String secretName;

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Bean
  @Primary
  public DataSource dataSource() {
    try {
      AWSSecretsManager client = AWSSecretsManagerClientBuilder
        .standard()
        .withRegion(awsRegion)
        .build();
      GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
        .withSecretId(secretName);
      GetSecretValueResult getSecretValueResult = client.getSecretValue(
        getSecretValueRequest
      );
      String secret = getSecretValueResult.getSecretString();

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode secretJson = objectMapper.readTree(secret);

      String username = secretJson.get("username").asText();
      String password = secretJson.get("password").asText();

      return DataSourceBuilder
        .create()
        .url(dbUrl)
        .username(username)
        .password(password)
        .driverClassName("org.postgresql.Driver")
        .build();
    } catch (Exception e) {
      throw new RuntimeException(
        "failed to retrieve database credentials from SecretsManager",
        e
      );
    }
  }
}
