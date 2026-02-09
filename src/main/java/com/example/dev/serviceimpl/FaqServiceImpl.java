package com.example.dev.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.dev.exception.ResourceAlreadyExistException;
import com.example.dev.exception.ResourceNotFoundException;
import com.example.dev.model.Faq;
import com.example.dev.repository.FaqRepository;
import com.example.dev.request.FaqRequest;
import com.example.dev.request.PaginationRequest;
import com.example.dev.request.UpdateFaqRequest;
import com.example.dev.response.ApiResponse;
import com.example.dev.response.FaqResponse;
import com.example.dev.response.PaginatedResponse;
import com.example.dev.service.IFaqService;
import com.example.dev.util.AppUtil;
import com.example.dev.util.Constants;

@Service
public class FaqServiceImpl implements IFaqService{

	@Autowired
	private FaqRepository faqRepository;

	@Override
	public ApiResponse addFaq(FaqRequest faqRequest) {
		// TODO Auto-generated method stub
		 faqRepository.findByQuestionAndIsDeleted(
		            faqRequest.getQuestion(),
		            Boolean.FALSE
		    ).ifPresent(faq -> {
		        throw new ResourceAlreadyExistException(
		                "Question already exists"
		        );
		    });

		    faqRepository.findByAnswerAndIsDeleted(
		            faqRequest.getAnswer(),
		            Boolean.FALSE
		    ).ifPresent(faq -> {
		        throw new ResourceAlreadyExistException(
		                "Answer already exists"
		        );
		    });

		 Faq faq = Faq.builder()
				    .question(faqRequest.getQuestion()).answer(faqRequest.getAnswer()).category(faqRequest.getCategory()).isActive(Boolean.TRUE)
		            .createdDate(LocalDateTime.now()).updatedDate(LocalDateTime.now()).isDeleted(Boolean.FALSE).status(faqRequest.getStatus())
		            .build();
		    Faq savedFaq = faqRepository.save(faq);

		    return ApiResponse.builder().statusCode(HttpStatus.CREATED.value())
					.message(Constants.FAQ_CREATED_SUCCESSFULLY).response(savedFaq).build();
	}

	@Override
	public ApiResponse getAllFaq(Integer pageNumber, Integer pageSize, String search) {
		// TODO Auto-generated method stub
		PaginationRequest pagePaginationRequest = new PaginationRequest();
		pagePaginationRequest.setPageNumber(pageNumber);
		pagePaginationRequest.setPageSize(pageSize);
		Pageable pageableRequest = AppUtil.buildPageableRequest(pagePaginationRequest);
		Page<Faq> leads = this.faqRepository.findFaqs(search, pageableRequest);

		return ApiResponse.builder().message(Constants.FAQ_FETCHED).statusCode(HttpStatus.OK.value())
				.response(new PaginatedResponse<>(leads.map(this::faqToFaqResponse))).build();
	}
	
	public FaqResponse faqToFaqResponse(Faq faq) {
	    return FaqResponse.builder()
	            .faqId(faq.getFaqId())
	            .question(faq.getQuestion())
	            .answer(faq.getAnswer())
	            .category(faq.getCategory())
	            .status(faq.getStatus())
	            .build();
	}

	@Override
	public ApiResponse updateFaq(UpdateFaqRequest updateFaqRequest) {
		// TODO Auto-generated method stub
		Faq faq = faqRepository.findByFaqIdAndIsDeleted(updateFaqRequest.getFaqId(),Boolean.FALSE)
	            .orElseThrow(() -> new ResourceNotFoundException("Faq not found with id: " + updateFaqRequest.getFaqId()));
		faqRepository.findByQuestionAndIsDeletedAndFaqIdNot(
	            updateFaqRequest.getQuestion(),
	            Boolean.FALSE,
	            updateFaqRequest.getFaqId()
	    ).ifPresent(existing -> {
	        throw new ResourceAlreadyExistException(
	                "Question already exists"
	        );
	    });

	    faqRepository.findByAnswerAndIsDeletedAndFaqIdNot(
	            updateFaqRequest.getAnswer(),
	            Boolean.FALSE,
	            updateFaqRequest.getFaqId()
	    ).ifPresent(existing -> {
	        throw new ResourceAlreadyExistException(
	                "Answer already exists"
	        );
	    });

	    // Update Fields
	    faq.setQuestion(updateFaqRequest.getQuestion());
	    faq.setAnswer(updateFaqRequest.getAnswer());
	    faq.setCategory(updateFaqRequest.getCategory());
	    faq.setStatus(updateFaqRequest.getFaqStatus());

	    Faq updateFaq = faqRepository.save(faq);

	    return ApiResponse.builder()
	            .statusCode(HttpStatus.OK.value())
	            .message("Faq updated successfully")
	            .response(updateFaq)
	            .build();
	}

	@Override
	public ApiResponse deleteFaq(String id) {
		// TODO Auto-generated method stub
		Faq faq = this.faqRepository.findByFaqIdAndIsDeleted(id, Boolean.FALSE)
				.orElseThrow(() -> new ResourceNotFoundException(Constants.FAQ_NOT_FOUND));
		faq.setIsActive(Boolean.FALSE);
		faq.setIsDeleted(Boolean.TRUE);
		this.faqRepository.save(faq);
		return ApiResponse.builder().message(Constants.FAQ_DELETE_SUCCESS).statusCode(HttpStatus.OK.value())
				.build();
		
	}
	
	
}
