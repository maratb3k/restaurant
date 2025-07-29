//package com.epam.edp.demo.util;
//
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicSessionCredentials;
//import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
//import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
//import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
//import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
//import com.amazonaws.services.securitytoken.model.Credentials;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.Instant;
//
//@Slf4j
//@Component
//public class AutoRefreshingSessionCredentialsProvider implements AWSCredentialsProvider {
//
//    private final AWSSecurityTokenService stsClient;
//    private final String roleArn;
//    private final String sessionName;
//    private BasicSessionCredentials currentCredentials;
//    private Instant credentialsExpiry;
//
//    public AutoRefreshingSessionCredentialsProvider(
//            @Value("${aws_access_key_id}") String accessKey,
//            @Value("${aws_secret_access_key}") String secretKey,
//            @Value("${aws_session_token:}") String sessionToken,
//            @Value("${region}") String region,
//            @Value("${aws.role_arn}") String roleArn,
//            @Value("${aws.session_name}") String sessionName
//    ) {
//        BasicSessionCredentials baseCreds = new BasicSessionCredentials(accessKey, secretKey, sessionToken);
//        this.stsClient = AWSSecurityTokenServiceClientBuilder.standard()
//                .withRegion(region)
//                .withCredentials(new AWSStaticCredentialsProvider(baseCreds))
//                .build();
//        this.roleArn = roleArn;
//        this.sessionName = sessionName;
//        refresh();
//    }
//
//    @Override
//    public AWSCredentials getCredentials() {
//        if (credentialsExpiry == null || Instant.now().isAfter(credentialsExpiry.minusSeconds(60))) {
//            refresh();
//        }
//        return currentCredentials;
//    }
//
//    @Override
//    public void refresh() {
//        AssumeRoleRequest request = new AssumeRoleRequest()
//                .withRoleArn(roleArn)
//                .withRoleSessionName(sessionName)
//                .withDurationSeconds(3600);
//
//        AssumeRoleResult result = stsClient.assumeRole(request);
//        Credentials tempCreds = result.getCredentials();
//
//        this.currentCredentials = new BasicSessionCredentials(
//                tempCreds.getAccessKeyId(),
//                tempCreds.getSecretAccessKey(),
//                tempCreds.getSessionToken());
//
//        this.credentialsExpiry = tempCreds.getExpiration().toInstant();
//
//        log.info("AWS STS credentials refreshed. New expiry: {}", credentialsExpiry);
//    }
//
//    @Scheduled(fixedRate = 3000000) // Every 50 minutes
//    public void refreshCredentialsPeriodically() {
//        log.info("Scheduled refresh of AWS credentials.");
//        refresh();
//    }
//}
