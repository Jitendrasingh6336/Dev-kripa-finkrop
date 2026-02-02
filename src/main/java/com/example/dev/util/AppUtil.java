package com.example.dev.util;

import java.util.Arrays;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.example.dev.model.User;
import com.example.dev.request.PaginationRequest;

@Component
public class AppUtil {

	
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
	
}
