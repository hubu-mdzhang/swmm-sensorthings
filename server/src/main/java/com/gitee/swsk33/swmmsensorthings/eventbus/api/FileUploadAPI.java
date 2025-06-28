package com.gitee.swsk33.swmmsensorthings.eventbus.api;

import com.gitee.swsk33.swmmsensorthings.eventbus.model.Result;
import com.gitee.swsk33.swmmsensorthings.eventbus.property.CoreProperties;
import io.github.swsk33.fileliftcore.model.BinaryContent;
import io.github.swsk33.fileliftcore.model.file.LocalFile;
import io.github.swsk33.fileliftcore.model.file.UploadFile;
import io.github.swsk33.fileliftcore.model.result.FileResult;
import io.github.swsk33.fileliftcore.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 */
@RestController
@RequestMapping("/api/file")
public class FileUploadAPI {

	@Autowired
	private UploadFileService uploadFileService;

	@Autowired
	private CoreProperties coreProperties;

	@PutMapping("/upload")
	public Result<String> upload(@RequestParam("file") MultipartFile file) {
		FileResult<UploadFile> result = uploadFileService.upload(file);
		if (!result.isSuccess()) {
			return Result.resultFailed("上传文件失败！" + result.getMessage());
		}
		LocalFile uploadFile = (LocalFile) result.getData();
		return Result.resultSuccess("上传文件成功！", String.format("http://%s:%d/api/file/get/%s.%s", coreProperties.getAdvertiseHost(), coreProperties.getAdvertisePort(), uploadFile.getName(), uploadFile.getFormat()));
	}

	@GetMapping("/get/{filename}")
	public ResponseEntity<byte[]> downloadByFullName(@PathVariable("filename") String fullName) {
		FileResult<BinaryContent> result = uploadFileService.downloadFileByFullName(fullName);
		if (!result.isSuccess()) {
			return ResponseEntity.notFound().build();
		}
		// 获取二进制结果中的文件流信息
		BinaryContent content = result.getData();
		// 准备响应头
		HttpHeaders headers = new HttpHeaders();
		// 设定MediaType类型（从文件结果中获取并设定）至响应头
		headers.setContentType(MediaType.parseMediaType(content.getContentType()));
		// 设定Content-Disposition头，告诉浏览器下载的文件名称
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(fullName).build());
		// 最终返回响应对象
		return ResponseEntity.ok().headers(headers).body(content.getByteAndClose());
	}

}