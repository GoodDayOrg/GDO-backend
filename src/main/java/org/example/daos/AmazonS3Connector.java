package org.example.daos;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public final class AmazonS3Connector {

    private static final String REGION = "eu-west-1";
    private static AmazonS3 amazonS3Client;
    private static final String ENDPOINT_URL = System.getenv("ENDPOINT_URL");

    private AmazonS3Connector() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static AmazonS3 getAmazonS3Client() {
        if (amazonS3Client != null) {
            return amazonS3Client;
        }

        amazonS3Client = getAwsClient();
        return amazonS3Client;
    }

    private static AmazonS3 getAwsClient() {
        String accessKeyId = System.getenv("AWS_SECRET_KEY_ID");
        String secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        String endpointUrl = System.getenv("ENDPOINT_URL");

        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(new ClientConfiguration().withProtocol(Protocol.HTTP));

        if (endpointUrl != null && !endpointUrl.isEmpty()) {
            builder =
                    builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpointUrl, REGION));
        } else {
            builder = builder.withRegion(REGION);
        }

        if (accessKeyId != null && !accessKeyId.isEmpty() && secretAccessKey != null && !secretAccessKey.isEmpty()) {
            AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
            builder = builder.withCredentials(new AWSStaticCredentialsProvider(credentials));
        } else {
            builder = builder.withCredentials(DefaultAWSCredentialsProviderChain.getInstance());
        }

        return builder.build();
    }
}
