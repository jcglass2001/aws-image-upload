package com.jcg.s3imageupload.service;

import com.jcg.s3imageupload.dto.UserRequestDTO;
import com.jcg.s3imageupload.filestore.FileStore;
import com.jcg.s3imageupload.model.UserProfile;
import com.jcg.s3imageupload.repo.UserProfileRepository;
import com.jcg.s3imageupload.bucket.BucketName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;

@Service
public class UserProfileService {
    // private final DataAccessService dataAccessService;
    private final UserProfileRepository userProfileRepository;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileRepository repository, FileStore fileStore){

        this.userProfileRepository = repository;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getUserProfiles(){
        return userProfileRepository.findAll();
    }

    public void uploadUserProfileImage(String userProfileId, MultipartFile file) {
        validateImage(file);
        Map<String, String> metadata = extractMetadata(file);
        UserProfile userProfile = getUserProfileOrThrow(userProfileId);

        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), userProfile.getId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        try{
            fileStore.save(path, filename, Optional.of(metadata),file.getInputStream());
            userProfile.setProfileImageLink(filename);
            userProfileRepository.save(userProfile);
        } catch(IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] downloadUserProfileImage(String userProfileId) {
        UserProfile userProfile = getUserProfileOrThrow(userProfileId);
        String imagePath = String.format("%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                userProfile.getId()
        );
        return userProfile.getProfileImageLink()
                .map(key -> fileStore.download(imagePath,key))
                .orElse(new byte[0]);
    }
    public UserProfile createUserProfile(UserRequestDTO user) {
        UserProfile newUser = UserProfile.builder()
                .username(user.username())
                .build();
        return userProfileRepository.save(newUser);
    }
    public String deleteUserProfiles() {
        userProfileRepository.deleteAll();
        return "Profiles deleted successfully.";
    }

    /**  HELPER METHODS  **/
    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String,String> metadata = new HashMap<>();
        metadata.put("File-Name", file.getOriginalFilename());
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()) );
        return metadata;
    }

    private void validateImage(MultipartFile file) {
        if(file.isEmpty()){
            throw new IllegalArgumentException("File is empty");
        }
        if( !Arrays.asList(
                IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType() ).contains(file.getContentType()) ) {
            throw new IllegalArgumentException("Invalid file type [" + file.getContentType() + "]");
        }
    }
    private UserProfile getUserProfileOrThrow(String userProfileId) {
        return userProfileRepository.findById(userProfileId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }


}
