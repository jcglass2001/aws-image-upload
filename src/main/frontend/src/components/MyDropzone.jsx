import React, { useCallback } from "react";
import { useDropzone } from "react-dropzone";
import userProfileAPI from "../config/axiosConfig";

const MyDropzone = ({ id }) => {
  const uploadFile = async (file) => {
    try {
      const formData = new FormData();
      formData.append("file",file);
      
      await userProfileAPI.post(`/${id}/image/upload`, formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      });
      console.log("File uploaded successfully");
    } catch (error) {
      console.error("Error uploading file", err);
    }
  }
  const onDrop = useCallback((acceptedFiles) => {
    if(acceptedFiles.length > 0){
      const file = acceptedFiles[0];
      uploadFile(file);
    }
  }, [id]);
  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {isDragActive ? (
        <p>Drop the profile image here ...</p>
      ) : (
        <p>Drag 'n' drop profile image, or click to select profile image</p>
      )}
    </div>
  );
};

export default MyDropzone;
