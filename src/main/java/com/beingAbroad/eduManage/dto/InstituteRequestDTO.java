package com.beingAbroad.eduManage.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstituteRequestDTO {

    @NotBlank(message = "Name is mandatory")
    @Size(max = 255, message = "Name should not exceed 255 characters")
    private String name;

    @NotBlank(message = "Location is mandatory")
    @Size(max = 255, message = "Location should not exceed 255 characters")
    private String location;

//    @NotBlank(message = "Contact information is mandatory")
//    @Size(max = 255, message = "Contact information should not exceed 255 characters")
//    private String contactInfo;
      @NotBlank(message = "Email is mandatory")
      @Email(message = "Invalid email format")
      @Size(max = 255, message = "Email should not exceed 255 characters")
      private String email;

      @NotBlank(message = "Phone number is mandatory")
      @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
      @Pattern(regexp = "[0-9]+", message = "Phone number must contain only digits")
      private String phoneNumber;

    @NotBlank(message = "Institute code is mandatory")
    @Size(max = 50, message = "Institute code should not exceed 50 characters")
    private String instituteCode;



}
