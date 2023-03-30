package ua.servicedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.servicedesk.services.controllerservices.FilesControllerService;

import java.io.IOException;

@Controller
@RequestMapping("/")
public class FilesController {

    private FilesControllerService controllerService;

    @GetMapping(path = "/files")
    public String files(@RequestParam(name = "id") String id,
                        Model model) {

        controllerService.processFiles(id, model);
        return "files";
    }
    @PostMapping(path = "/upload_file")
    @ResponseBody
    public String uploadFile(@RequestParam(name = "file") MultipartFile multipartFile,
                             @RequestParam(name = "id") String id,
                             Model model) {
        return controllerService.processUploadFile(multipartFile, id, model);
    }

        @RequestMapping(value = "/download_file", method = RequestMethod.GET)
        public ResponseEntity<Object> downloadFile(@RequestParam(name = "id") String id) throws IOException  {
            return controllerService.processDownloadFile(id);

        }
    @Autowired
    public void setControllerService(FilesControllerService controllerService) {
        this.controllerService = controllerService;
    }
}
