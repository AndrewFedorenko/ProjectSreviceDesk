package ua.servisedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.servisedesk.dao.FilesRepository;
import ua.servisedesk.dao.RequestsRepository;
import ua.servisedesk.domain.AttachedFile;
import ua.servisedesk.domain.SupportRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/")
public class FilesController {
    @Autowired
    FilesRepository filesRepository;
    @Autowired
    RequestsRepository requestsRepository;

    @GetMapping(path = "/files")
    public String files(@RequestParam(name = "id") String id,
                        Model model) {

        List<AttachedFile> files = filesRepository.findByRequestId(id);

        model.addAttribute("files", files);
        model.addAttribute("id", id);
        return "files";
    }
    @PostMapping(path = "/upload_file")
    @ResponseBody
    public String uploadFile(@RequestParam(name = "file") MultipartFile multipartFile,
                             @RequestParam(name = "id") String id,
                             Model model) {
        SupportRequest supportRequest = requestsRepository.findRequestById(id);
        String fileName = multipartFile.getOriginalFilename();
        if (!multipartFile.isEmpty()) {
            try {

                byte[] bytes = multipartFile.getBytes();
                AttachedFile attachedFile = new AttachedFile();
                attachedFile.setName(fileName);
                attachedFile.setFile(bytes);
                attachedFile.setUploadDateTime(LocalDateTime.now());
                attachedFile.setSupportRequest(supportRequest);
                filesRepository.updateFile(attachedFile);
            } catch (Exception e) {
                return "Cannot upload file " + fileName + ": " + e.getMessage();
            }
        }
        List<AttachedFile> files = filesRepository.findByRequestId(id);

        model.addAttribute("files", files);
//        model.addAttribute("id", id);
        return "filestable";
    }

        @RequestMapping(value = "/download_file", method = RequestMethod.GET)
        public ResponseEntity<Object> downloadFile(@RequestParam(name = "id") String id) throws IOException  {
            AttachedFile attachedFile = filesRepository.findFileById(id);
            byte[] data = attachedFile.getFile();
            String fileName = attachedFile.getName();
            String extension = fileName.substring(fileName.length()-3);
            File tmpFile = File.createTempFile(fileName, extension);
            FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
            fileOutputStream.write(data);
            fileOutputStream.close();


            InputStreamResource resource = new InputStreamResource(new FileInputStream(tmpFile));
            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"",
                    URLEncoder.encode(fileName)));

            return ResponseEntity.ok().headers(headers).contentLength(
                    tmpFile.length()).contentType(MediaType.parseMediaType("application/" + extension)
            ).body(resource);

        }
}
