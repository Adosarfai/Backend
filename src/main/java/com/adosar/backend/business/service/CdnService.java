package com.adosar.backend.business.service;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
