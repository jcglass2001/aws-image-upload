package com.jcg.s3imageupload.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Optional;

@Document("profiles")
@Data
@AllArgsConstructor
@Builder
public class UserProfile {
    @Id
    private final String id;
    private final String username;
    private String profileImageLink; //S3 key

    public Optional<String> getProfileImageLink() { return Optional.ofNullable(profileImageLink); }
}
