package com.example.dev.model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Employee1 {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	
	private String profile;
	
	private String name;
	
	private String firstName;

    private String lastName;

    private String email;

    private String phone;
    
    private String username;
    
    private String password;

    private String address;

    private String designation;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate  dateOfBirth;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate  joiningDate;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus employeeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Leave> leaves;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Attendance> attendances;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
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
