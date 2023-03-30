package ua.servicedesk.services.controllerservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ua.servicedesk.dao.FilesRepository;
import ua.servicedesk.dao.RequestsRepository;
import ua.servicedesk.domain.AttachedFile;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.domain.SupportRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FilesControllerService {

    private FilesRepository filesRepository;

    private RequestsRepository requestsRepository;

    private CurrentRoleHolder roleHolder;

    public void processFiles(String id,
                             Model model){
        List<AttachedFile> files = filesRepository.findByRequestId(id);

        model.addAttribute("files", files);
        model.addAttribute("id", id);
        CommonModelAttrSetter.setCommonModelAttr(model, roleHolder);

    }

    public String processUploadFile(MultipartFile multipartFile,
                                  String id,
                                  Model model){
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
        return "filestable";
    }

    public ResponseEntity<Object> processDownloadFile(String id) throws IOException {
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

    @Autowired
    public void setFilesRepository(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    @Autowired
    public void setRequestsRepository(RequestsRepository requestsRepository) {
        this.requestsRepository = requestsRepository;
    }

    @Autowired
    public void setRoleHolder(CurrentRoleHolder roleHolder) {
        this.roleHolder = roleHolder;
    }
}
