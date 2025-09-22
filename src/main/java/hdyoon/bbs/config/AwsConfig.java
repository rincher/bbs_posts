package hdyoon.bbs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

@Configuration
@Slf4j
public class AwsConfig {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.credentials.access-key:}")
    private String accessKey;

    @Value("${aws.credentials.secret-key:}")
    private String secretKey;

    @Value("${aws.credentials.session-token:}")
    private String sessionToken;

    @Value("${aws.credentials.assume-role-arn:}")
    private String assumeRoleArn;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        if ("local".equals(activeProfile)) {
            return createAssumeRoleCredentials();
        } else {
            return DefaultCredentialsProvider.builder().build();
        }
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient(AwsCredentialsProvider credentialsProvider) {
        return SqsAsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    @Bean
    public SecretsManagerClient secretsManagerClient(AwsCredentialsProvider credentialsProvider) {
        return SecretsManagerClient.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider)
                .build();
    }

    private AwsCredentialsProvider createAssumeRoleCredentials() {
        // 빈 값 체크 추가
        if (accessKey.isEmpty() || secretKey.isEmpty() || sessionToken.isEmpty()) {
            log.warn("AWS credentials are empty, falling back to default provider");
            return DefaultCredentialsProvider.builder().build();
        }

        AwsSessionCredentials sessionCredentials = AwsSessionCredentials.builder()
                .accessKeyId(accessKey)
                .secretAccessKey(secretKey)
                .sessionToken(sessionToken)
                .build();

        StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider.create(sessionCredentials);

        StsClient stsClient = StsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(staticCredentialsProvider)
                .build();

        return StsAssumeRoleCredentialsProvider.builder()
                .stsClient(stsClient)
                .refreshRequest(AssumeRoleRequest.builder()
                        .roleArn(assumeRoleArn)
                        .roleSessionName("local-session")
                        .build())
                .build();
    }
}