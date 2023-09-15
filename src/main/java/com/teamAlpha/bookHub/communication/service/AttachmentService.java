package com.teamAlpha.bookHub.communication.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.*;
import java.io.File;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.teamAlpha.bookHub.communication.model.AttachmentStorageProperties;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.teamAlpha.bookHub.communication.controller.AttachmentController;
import com.teamAlpha.bookHub.communication.entity.Attachment;
import com.teamAlpha.bookHub.communication.exception.AttachmentDetailNotFoundException;
import com.teamAlpha.bookHub.communication.exception.InvalidAttachmentTypeException;
import com.teamAlpha.bookHub.communication.model.AttachmentDto;
import com.teamAlpha.bookHub.communication.repository.AttachmentRepository;
import com.teamAlpha.bookHub.communication.utils.FileUtils;

import javax.servlet.http.HttpServletResponse;

@Service
public class AttachmentService {
	private final static String rootPath = new AttachmentStorageProperties().getPATH();

	private final static Logger LOGGER = LoggerFactory.getLogger(AttachmentService.class);

	private Path path = null;

	@Autowired
	AttachmentRepository attachmentRepository;

	public AttachmentDto saveAttachment(MultipartFile file, Attachment attachment)
			throws InvalidAttachmentTypeException {
		try {
			LOGGER.info("Create new attachment with details");
			path = FileUtils.pathFinders(file, attachment.getAttachmentTypeId());
			Files.createDirectories(path);
			attachment.setFileHash(FileUtils.fileHash(file).toString());
			Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));

			AttachmentDto attachmentDto = new AttachmentDto();
			Attachment savedAttachment = attachmentRepository.save(attachment);
			LOGGER.info("Attachment with details saved success");
			BeanUtils.copyProperties(savedAttachment, attachmentDto);

			attachmentDto.add(linkTo(methodOn(AttachmentController.class).getAllAttachmentDetails()).withRel("list"));
			attachmentDto.add(linkTo(
					methodOn(AttachmentController.class).singleAttchmentDetail(savedAttachment.getAttachmentId()))
							.withSelfRel());

			return attachmentDto;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.info("Invalid attachment type uploaded.");
			throw new InvalidAttachmentTypeException("Select proper image file type. Try again!");
		}
	}

	public List<AttachmentDto> getAllAttachmentDetails() throws AttachmentDetailNotFoundException {
		List<AttachmentDto> attachmentDtoList = new ArrayList<>();
		List<Attachment> attachments = attachmentRepository.findAll();
		BeanUtils.copyProperties(attachments, attachmentDtoList);

		return attachmentDtoList;
	}

	public AttachmentDto singleAttachmentDetail(Integer attachmentId) throws AttachmentDetailNotFoundException {
		try {

			AttachmentDto attachmentDto = new AttachmentDto();
			Attachment attachment = attachmentRepository.findById(attachmentId).get();
			BeanUtils.copyProperties(attachment, attachmentDto);
			attachmentDto.add(linkTo(methodOn(AttachmentController.class).getAllAttachmentDetails()).withRel("list"));

			return attachmentDto;

		} catch (Exception e) {
			throw new AttachmentDetailNotFoundException(attachmentId);
		}
	}

	public void downloadAttachment(HttpServletResponse response, Integer attachmentId) throws AttachmentDetailNotFoundException {
		LOGGER.info("Download attachment");
		try {
			AttachmentDto attachmentDto = new AttachmentDto();
			Attachment attachment = attachmentRepository.findById(attachmentId).get();
			BeanUtils.copyProperties(attachment, attachmentDto);
			String path = rootPath.concat("/" + attachment.getAttachmentTypeId() + "/" + attachment.getFileHash());

			File file = new File(path);
			String[] fileList = file.list();
			File imagePath = new File(path.concat("/" + fileList[0]));

			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + imagePath.getName() + "\""));
			response.setContentLength((int) imagePath.length());
			InputStream inputStream = new BufferedInputStream(new FileInputStream(imagePath));
			FileCopyUtils.copy(inputStream, response.getOutputStream());
			LOGGER.info("Successfully downloaded attachment");

		} catch (Exception e) {
			LOGGER.error("Cannot find attachment Id: {} in database", attachmentId);
			throw new AttachmentDetailNotFoundException(attachmentId);
		}
	}



	public void deleteAttachment(Integer attachmentId) throws AttachmentDetailNotFoundException {
		LOGGER.info("Delete File and if folder is empty delete folder also");
		try {

			AttachmentDto attachmentDto = new AttachmentDto();
			Attachment attachment = attachmentRepository.findById(attachmentId).get();
			BeanUtils.copyProperties(attachment, attachmentDto);

			File keyPath = new File(rootPath.concat("/" + attachment.getAttachmentTypeId()));
			System.out.println(keyPath);
			File hashMapPath = new File(rootPath.concat("/" + attachment.getAttachmentTypeId() + "/" + attachment.getFileHash()));

			if (keyPath.list().length == 0) {
				FileSystemUtils.deleteRecursively(keyPath);
			} else {
				FileSystemUtils.deleteRecursively(hashMapPath);
				LOGGER.info("Successfully delete local file");
				if (keyPath.list().length == 0) {
					FileSystemUtils.deleteRecursively(keyPath);
					LOGGER.info("Successfully delete empty local folder");
				}
				attachmentRepository.deleteById(attachmentId);
				LOGGER.info("Successfully delete attachment of Id: {} from database", attachmentId );

			}
		} catch (Exception e) {
			LOGGER.error("Cannot find attachment id {} in database or file not found in locally", attachmentId);
			throw new AttachmentDetailNotFoundException(attachmentId);
		}
	}

	public byte[] getImageWithmediaType(Integer attachmentId) throws AttachmentDetailNotFoundException{
		try{
			AttachmentDto attachmentDto = new AttachmentDto();
			Attachment attachment = attachmentRepository.findById(attachmentId).get();
			BeanUtils.copyProperties(attachment, attachmentDto);
			String path = rootPath.concat("/" + attachment.getAttachmentTypeId() + "/" + attachment.getFileHash());

			File file = new File(path);
			String[] fileList = file.list();
			File imagePath = new File(path.concat("/" + fileList[0]));

			Path imageUrl = Paths.get(String.valueOf(imagePath));
			System.out.println(imageUrl);
			return IOUtils.toByteArray(imageUrl.toUri());

		}catch (Exception e) {
			LOGGER.error("Cannot find attachment id {} in database or file not found in locally", attachmentId);
			throw new AttachmentDetailNotFoundException(attachmentId);
		}

	}
}

