package com.codehat.charusat.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "en";

    public static final String YOUTUBE_API_KEY = "AIzaSyAT_xty2Pny6w75z5IDDo0J25fANx1ZjKw";

    public static final String AWS_REGION;
    public static final String AWS_ACCESS_KEY;
    public static final String AWS_SECRET_KEY;
    public static final AWSCredentials credentials;
    public static final AmazonS3 s3client;
    public static final String S3_BUCKET_LINK;
    public static final String S3_BUCKET_NAME;
    public static final String OBJECT_PATH;
    public static final String userDir;
    public static final String DRAFTING;
    public static final String APPROVAL_PENDING;
    public static final String REVIEWER_ASSIGNED;
    public static final String APPROVED;
    public static final String REJECTED;

    static {
        AWS_REGION = "ap-south-1";
        AWS_ACCESS_KEY = "";
        AWS_SECRET_KEY = "";
        S3_BUCKET_NAME = "charuvidya-charusat";
        userDir = System.getProperty("user.dir");
        OBJECT_PATH = userDir + "/src/main/resources/objects/";
        S3_BUCKET_LINK = "https://" + S3_BUCKET_NAME + ".s3.amazonaws.com/";
        credentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);
        s3client =
            AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_SOUTH_1)
                .build();
        DRAFTING = "drafting";
        APPROVAL_PENDING = " approval pending";
        REVIEWER_ASSIGNED = " reviewer assigned";
        APPROVED = " course approved";
        REJECTED = " course rejected";
    }

    private Constants() {}
}
