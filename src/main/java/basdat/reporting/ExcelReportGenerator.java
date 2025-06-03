package basdat.reporting;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import basdat.entity.InstructorRecord;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

public class ExcelReportGenerator {

    public void exportToExcel(List<InstructorRecord> instructors, String jrxmlPath, String outputPath) throws JRException {
        InputStream jrxmlStream = ExcelReportGenerator.class.getClassLoader().getResourceAsStream(jrxmlPath);
        if (jrxmlStream == null) {
            throw new JRException("Resource not found: " + jrxmlPath + ". Ensure the .jrxml file is in the classpath (e.g., src/main/resources/" + jrxmlPath + " if using Maven/Gradle, or copied to the build folder).");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(instructors);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "Instructor Data Report");

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