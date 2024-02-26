package com.jcg.s3imageupload.repo;

import com.jcg.s3imageupload.model.UserProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProfileRepository extends MongoRepository<UserProfile,String> {
}
