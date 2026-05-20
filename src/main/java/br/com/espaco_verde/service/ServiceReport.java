package br.com.espaco_verde.service;

import br.com.espaco_verde.DTO.OrderResponseDTO;
import br.com.espaco_verde.DTO.ReportFilterDTO;
import br.com.espaco_verde.DTO.ReportTopProductProjection;
import br.com.espaco_verde.entity.Order;
import br.com.espaco_verde.entity.Report;
import br.com.espaco_verde.entity.ReportFilter;
import br.com.espaco_verde.entity.ReportTopProduct;
import br.com.espaco_verde.repository.RepositoryOrder;
import br.com.espaco_verde.repository.RepositoryReport;
import br.com.espaco_verde.repository.specification.OrderSpecification;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ServiceReport {

    @Autowired
    private RepositoryOrder repositoryOrder;

    @Autowired
    private RepositoryReport repositoryReport;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${dir.reports}")
    private String pdfStoragePath;

    @Transactional
    public Report generateOrdersReport (ReportFilterDTO reportFilterDTO){

        List<Order> reportOrders = repositoryOrder.findAll(OrderSpecification.withFilter(reportFilterDTO));

        List<ReportTopProductProjection> reportTopProducts = repositoryOrder.getMostSoldProducts(
                reportFilterDTO.initalDate(),
                reportFilterDTO.finalDate(),
                reportFilterDTO.status(),
                PageRequest.of(0,5)
        );

        BigDecimal totalOrdersValue = reportOrders.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer ordersQuantity = reportOrders.size();

        if(ordersQuantity <= 0){
            throw new RuntimeException("Nenhum pedido encontrado com os filtros aplicados");
        }

        Report report = new Report();
        report.setOrdersQuantity(ordersQuantity);
        report.setTotalOrdersValue(totalOrdersValue);
        ReportFilter filter = reportFilterDTO.toEntity();
        report.setFilters(filter);

        for(ReportTopProductProjection projection: reportTopProducts){
            ReportTopProduct topProduct = new ReportTopProduct(
                    projection.getProductid(),
                    projection.getProductName(),
                    projection.getSoldQuantity(),
                    projection.getTotalValue()
            );
            report.addTopProduct(topProduct);
        }

        Context context = new Context();
        context.setVariable("reportOrders", reportOrders);
        context.setVariable("topProducts", reportTopProducts);
        context.setVariable("totalOrdersValue", totalOrdersValue);
        context.setVariable("ordersQuantity", ordersQuantity);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        context.setVariable(
                "filterPeriod", reportFilterDTO.initalDate().format(dtf)
                        + " até " + reportFilterDTO.finalDate().format(dtf));

        String pdfHtml = templateEngine.process("reports/ordersReport", context);
        String pdfName = "report_" + UUID.randomUUID().toString().substring(0,8) + ".pdf";
        String pdfPath = pdfStoragePath +"/"+pdfName;

        try {
            createPdf(pdfHtml, pdfPath);
            report.setPdfPath(pdfPath);
        }catch (Exception e){
            throw new RuntimeException("Falha ao gerar o arquivo PDF. Relatório não foi salvo", e);
        }

        return repositoryReport.save(report);
    }

    private void createPdf(String pdfHtml, String pdfPath) throws Exception{

        File dir = new File(pdfStoragePath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        try(OutputStream outputStream = new FileOutputStream(pdfPath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(pdfHtml, null);
            builder.toStream(outputStream);
            builder.run();
        }

    }

    public  List<Report> getAllReports() {

        List<Report> reports = repositoryReport.findAll();
        return reports;

    }

    public Report getReportById(int id) {
        return repositoryReport.findById(id).orElseThrow(()-> new RuntimeException("Relatório não encontrado"));
    }
}
