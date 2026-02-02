package com.example.dev.request;

import com.example.dev.util.Constants;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationRequest {

	@NotNull(message = Constants.PAGE_SIZE_REQUIRED)
//	@Pattern(regexp = "^[1-9][0-9]*$", message = "Page number must be a positive integer greater than 0.")
	private Integer pageSize;

	@NotNull(message = Constants.PAGE_INDEX_REQUIRED)
//	@Pattern(regexp = "^[0-9][0-9]*$", message = "Page number must be a positive integer greater than 0.")
	private Integer pageNumber;

//	@Pattern(regexp = ValidationConstants.SORT_ORDER_PATTERN, message = ValidationConstants.SORT_ORDER_PATTERN_MESSAGE)
	private String sortDirection = "DESC";

	private String sortKey;
	private String searchValue;
}
