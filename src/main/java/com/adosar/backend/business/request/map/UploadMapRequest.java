package com.adosar.backend.business.request.map;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
public class UploadMapRequest {

	@Setter
	@NotNull
	private MultipartFile file;

	@Setter
	@NotNull
	private Integer id;

	@Setter
	@NotNull
	private String jwt;

}
