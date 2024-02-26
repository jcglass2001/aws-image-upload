package com.jcg.s3imageupload.controller;

import com.jcg.s3imageupload.dto.UserRequestDTO;
import com.jcg.s3imageupload.model.UserProfile;
import com.jcg.s3imageupload.service.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/user-profile")
@CrossOrigin("*")
@Slf4j
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService){
        this.userProfileService = userProfileService;
    }
    @GetMapping("/get/profiles")
    public ResponseEntity<List<UserProfile>> getUserProfiles() {
        log.info("UserProfileController | getUserProfiles() endpoint called");
        return ResponseEntity.ok(userProfileService.getUserProfiles());
    }
    @PostMapping("/create/profile")
    public ResponseEntity<UserProfile> createUserPofile(@RequestBody UserRequestDTO user){
        log.info("UserProfileController | createUserProfile() endpoint called");
        return ResponseEntity.status(HttpStatus.CREATED).body(userProfileService.createUserProfile(user));
    }
    @PostMapping(
            value = "/{userProfileId}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserProfileImage(@PathVariable("userProfileId") String userProfileId,
                                       @RequestParam("file") MultipartFile file) {
        log.info("UserProfileController | uploadUserProfileImage() endpoint called with parameter: {}, {}", userProfileId, file);
        userProfileService.uploadUserProfileImage(userProfileId,file);
    }
    @DeleteMapping("/delete/profiles")
    public ResponseEntity<String> deleteUserProfiles() {
        log.info("UserProfileController | deleteUserProfiles() endpoint called");
        return ResponseEntity.ok(userProfileService.deleteUserProfiles());
    }
    @GetMapping("{userProfileId}/image/download")
    public ResponseEntity<byte[]> downloadUerProfileImage(@PathVariable("userProfileId") String userProfileId){
        log.info("UserProfileController | downloadUserProfileImage() endpoint called with parameter: {}", userProfileId);
        return ResponseEntity.ok(userProfileService.downloadUserProfileImage(userProfileId));
    }
}
