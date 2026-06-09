package br.com.espaco_verde.control;

import br.com.espaco_verde.DTO.ReportFilterDTO;
import br.com.espaco_verde.entity.Report;
import br.com.espaco_verde.service.ServiceReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/admin/reports")
public class ControllerReport {

    @Autowired
    private ServiceReport serviceReport;

    @PostMapping("/generate")
    public ResponseEntity<Report> generateOrderReport(@RequestBody ReportFilterDTO filterDTO){
        return ResponseEntity.ok(serviceReport.generateOrdersReport(filterDTO));
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports(){
        return ResponseEntity.ok(serviceReport.getAllReports());
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<Resource> downloadPdf(@PathVariable int id){
        Report report = serviceReport.getReportById(id);
        File file = new File(report.getPdfPath());
        if (!file.exists()){
            throw new RuntimeException("Arquivo PDF não encontrado");
        }

        try{
            Resource resource = new UrlResource(file.toURI());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getName()+"\"")
                    .body(resource);
        }catch (MalformedURLException e){
            throw new RuntimeException("Erro ao ler o caminho do arquivo", e);
        }
    }

}
