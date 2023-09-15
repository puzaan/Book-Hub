package com.teamAlpha.bookHub.communication.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.teamAlpha.bookHub.communication.exception.InvalidAttachmentTypeException;
import com.teamAlpha.bookHub.communication.model.AttachmentStorageProperties;

public class FileUtils {

	private final static String rootPath = new AttachmentStorageProperties().getPATH();

	public static Path pathFinders(MultipartFile file, Integer key) throws InvalidAttachmentTypeException {

		if (fileTypeValidation(file)) {

			String path = rootPath.concat("/" + key + "/" + fileHash(file));

			Path getPath = Paths.get(path);

			return getPath;
		} else {
			throw new InvalidAttachmentTypeException("Please select image type file for attachment.");
		}

	}

	public static UUID fileHash(MultipartFile file) {
		return UUID.nameUUIDFromBytes(file.getOriginalFilename().getBytes());
	}

	private static Boolean fileTypeValidation(MultipartFile file) throws InvalidAttachmentTypeException {

		String[] type = file.getContentType().split("/");

		if (type[1].equalsIgnoreCase("jpg") || type[1].equalsIgnoreCase("png") || type[1].equalsIgnoreCase("jpeg")) {
			return true;
		}
		return false;
	}



}
