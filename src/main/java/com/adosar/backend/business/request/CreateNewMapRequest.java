package com.adosar.backend.business.request;

import com.adosar.backend.domain.Removed;
import com.adosar.backend.domain.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNewMapRequest {

	@Setter
	@NotNull
	private String title;

	@Setter
	@NotNull
	private String artist;

	@Setter
	@NotNull
	private Boolean published;
	
}
