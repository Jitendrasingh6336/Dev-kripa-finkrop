package com.example.dev.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "faqs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String faqId;

    @NotBlank(message = "Question is required")
    @Column(columnDefinition = "TEXT")
    private String question;

    @NotBlank(message = "Answer is required")
    @Column(columnDefinition = "TEXT")
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private FaqCategory category;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private FaqStatus status = FaqStatus.ACTIVE;

    @CreatedDate
   	@Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
   	private LocalDateTime createdDate;

   	@LastModifiedDate
   	@Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
   	private LocalDateTime updatedDate;

   	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
   	private Boolean isDeleted = Boolean.FALSE;
   	
   	@Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
	private Boolean isActive = Boolean.TRUE;
}
