import React, { useEffect, useState } from "react";
import userProfileAPI from "../config/axiosConfig";
import MyDropzone from "./MyDropzone";

const UserProfiles = () => {
  const [userProfiles, setUserProfiles] = useState([]);
  const [imageUrls, setImageUrls] = useState({});
  
  useEffect(() => {
    fetchUserProfiles();
    userProfiles.forEach((profile) => { fetchProfileImage(profile.id)})
  }, []);

  const fetchUserProfiles = () => {
    userProfileAPI.get("get/profiles").then((res) => {
      setUserProfiles(res.data);
    });
  };

  const fetchProfileImage = (profileId) => {
    userProfileAPI.get(`${profileId}/image/download`, { responseType: 'arraybuffer' }).then((res) => {
      const blob = new Blob([res.data], { type: 'image/jpeg' });
      const imageUrl = URL.createObjectURL(blob);
      setImageUrls((prevUrls) => ({
        ...prevUrls,
        [profileId]: imageUrl,
      }));
    });
  };

  return (
    <div>
      {userProfiles.map((profile, index) => (
        <div className="profile-container" key={index}>
          <div className="profile-image">
            {imageUrls[profile.id] ? (
              <img src={imageUrls[profile.id]} alt="profile_image" />
            ) : (
              <img src="https://picsum.photos/200/200" alt="placeholder image" />
            )}
          </div>
          <div className="profile-content">
            <h1>Username: {profile.username}</h1>
            <p>Id: {profile.id}</p>
            <MyDropzone {...profile} />
          </div>
        </div>
      ))}
    </div>
  );
};

export default UserProfiles;
