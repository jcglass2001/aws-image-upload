package com.jcg.s3imageupload.bucket;

import lombok.Getter;

@Getter
public enum BucketName {
    PROFILE_IMAGE("jcg-image-upload");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
