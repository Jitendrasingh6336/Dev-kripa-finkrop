package com.example.dev.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.dev.exception.BadRequestException;
import com.example.dev.model.User;
import com.example.dev.request.PaginationRequest;

@Component
public class AppUtil {

	public static final long MAX_PROFILE_PIC_UPLOAD_SIZE = 10 * 1024 * 1024;
	
	private final Cloudinary cloudinary;

    public AppUtil(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
	
	public static <T> boolean validateSortKey(Class<T> modelClass, String sortKey) {
		try {
			Class<?> currentClass = modelClass;
			while (currentClass != null) {
				boolean isValidKey = Arrays.stream(currentClass.getDeclaredFields())
						.anyMatch(f -> f.getName().equals(sortKey));
				if (isValidKey) {
					return true; // Found the sortKey
				}
				currentClass = currentClass.getSuperclass();
			}
			throw new IllegalArgumentException("Invalid sort key: " + sortKey);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid sort key: " + sortKey, e);
		}
	}

	public static Pageable buildPageableRequest(PaginationRequest pageRequest) {
		String sortKey = pageRequest.getSortKey();
		if (sortKey != null && pageRequest.getSortKey().equals("roleName"))
			sortKey = "role.roleName";
//		else if (sortKey != null && AppUtil.validateSortKey(User.class, sortKey))
			;
		Sort sort = Sort.by((pageRequest.getSortDirection() != null && !pageRequest.getSortDirection().isEmpty()
				? Direction.valueOf(pageRequest.getSortDirection())
				: Direction.ASC), sortKey != null ? sortKey : "createdDate");
		return PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
	}
	
	public String uploadPhoto(MultipartFile file, String destinationPath) {
		String fileName = null;
		// Check file size before uploading
		if (Objects.nonNull(file)) {
			long fileSize = file.getSize();
			if (fileSize > Constants.MAX_PROFILE_PIC_UPLOAD_SIZE) {
				throw new BadRequestException("File size too large. Maximum allowed size is 10 MB.");
			}
			fileName = this.uploadFile(file, destinationPath);
		}
		return fileName == null ? "" : fileName;
	}
	
	public String uploadFile(MultipartFile myFile, String destinationPath) {
		if (Objects.nonNull(myFile) && !myFile.isEmpty()) {
			String uuid = UUID.randomUUID().toString();
			String randomName = uuid.concat(myFile.getOriginalFilename());
			String fileName = StringUtils.cleanPath(randomName);
			Map uploadResponse;
			try {
				uploadResponse = cloudinary.uploader().upload(myFile.getBytes(),
						ObjectUtils.asMap("public_id", destinationPath + "/" + fileName));
				return (String) uploadResponse.get("secure_url");
			} catch (IOException e) {
				throw new BadRequestException(e.getMessage());
			}
		}
		return null;
	}
}
