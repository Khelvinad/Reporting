package basdat.report;

import basdat.model.Instructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class reportGenerator {

    public void exportToExcel(List<Instructor> instructors, String jrxmlPath, String outputPath) throws JRException {
        InputStream jrxmlStream = reportGenerator.class.getClassLoader().getResourceAsStream(jrxmlPath);
        if (jrxmlStream == null) {
            throw new JRException("Resource not found: " + jrxmlPath + ". Pastikan file .jrxml ada di classpath (misal: src/main/resources/" + jrxmlPath + " jika pakai Maven/Gradle, atau tercopy ke folder build).");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(instructors);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "Laporan Data Dosen");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputPath));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        exporter.setConfiguration(configuration);

        exporter.exportReport();
    }
}