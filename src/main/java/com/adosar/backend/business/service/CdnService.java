package com.adosar.backend.business.service;

import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
@AllArgsConstructor
public final class CdnService {
	private static final String CDN_PATH = System.getenv("CDN_PATH");

	public static void saveFile(byte[] data, String path, String filename) throws IOException {
		String fullPath = FilenameUtils.separatorsToSystem(String.format("%s%s\\%s", CDN_PATH, path, filename));
		File file = new File(fullPath);
		try (OutputStream outputStream = new FileOutputStream(file)) {
			outputStream.write(data);
		}
	}
}
