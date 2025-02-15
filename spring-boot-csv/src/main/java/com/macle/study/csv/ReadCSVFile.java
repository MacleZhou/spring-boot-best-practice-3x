package com.macle.study.csv;

import com.macle.study.csv.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.StrRegEx;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.*;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
public class ReadCSVFile {
    public static void main(String[] args) throws IOException {
        ClassPathResource resource = new ClassPathResource("csv/data.csv");
        Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        csvBeanReader(reader);
        reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        csvMapReader(reader);
        //读取不规整列的CSV文件
        ClassPathResource resource2 = new ClassPathResource("csv/data2.csv");
        readNonRegularFile(resource2);
    }

    private static void csvBeanReader(Reader reader) throws IOException {
        try (ICsvBeanReader beanReader = new CsvBeanReader(reader,
                CsvPreference.STANDARD_PREFERENCE)) {
            // 表的header映射到bean中
            final String[] headers = beanReader.getHeader(true) ;
            // 我们也可以自定义header映射；尤其对那种没有定义header的csv文件就非常有用
            //final String[] headers = new String[]{"id", "name", "age", "address", "email"} ;
            //final String[] headers = new String[]{"id", "name", "age", null,"email"} ;
            final CellProcessor[] processors = getCellProcessors();

            User user = null;
            while ((user = beanReader.read(User.class, headers, processors)) != null) {
                System.err.println(user);
            }
        }
    }

    private static void csvMapReader(Reader reader) throws IOException {
        try (ICsvMapReader beanReader = new CsvMapReader(reader, CsvPreference.STANDARD_PREFERENCE)) {
            final String[] headers = beanReader.getHeader(true) ;
            final CellProcessor[] processors = getCellProcessors();

            Map<String, Object> data = null;
            while ((data = beanReader.read(headers, processors)) != null) {
                System.err.println(data) ;
            }
        }
    }

    private static void readNonRegularFile(ClassPathResource resource2) throws IOException {
        Reader reader = new InputStreamReader(resource2.getInputStream(), StandardCharsets.UTF_8) ;
        try (ICsvListReader listReader = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE)) {
            listReader.getHeader(false) ;
            CellProcessor[] processors = null ;
            List<String> currentRow = null ;
            while ((currentRow = listReader.read()) != null) {
                processors = switch (currentRow.size()) {
                    case 5 -> getFiveCellProcessors() ;
                    case 4 -> getFourCellProcessors() ;
                    case 3 -> getThreeCellProcessors() ;
                    // other
                    default -> throw new IllegalStateException("Unexpected row size: " + currentRow.size()) ;
                } ;
                final List<Object> data = listReader.executeProcessors(processors) ;
                System.err.printf("rowNumber: %s, data: %s%n", listReader.getRowNumber(), data) ;
            }
        }
    }

    private static CellProcessor[] getCellProcessors() {
        // 需要验证email字段
        final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+";
        StrRegEx.registerMessage(emailRegex, "must be a valid email address");

        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(new ParseLong()), // id
                new NotNull(),                // name
                new ParseInt(),               // age
                new Trim(),                   // address
                new StrRegEx(emailRegex)      // email
        } ;
        return processors ;
    }

    private static CellProcessor[] getFiveCellProcessors() {
        final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+";
        StrRegEx.registerMessage(emailRegex, "must be a valid email address");
        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(new ParseLong()), // id
                new NotNull(),                // name
                new ParseInt(),               // age
                new Trim(),                   // address
                new StrRegEx(emailRegex)      // email
        } ;
        return processors ;
    }
    // 没有address列
    private static CellProcessor[] getFourCellProcessors() {
        final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+";
        StrRegEx.registerMessage(emailRegex, "must be a valid email address");
        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(new ParseLong()), // id
                new NotNull(),                // name
                new ParseInt(),               // age
                new StrRegEx(emailRegex)      // email
        } ;
        return processors ;
    }
    // 没有address,email列
    private static CellProcessor[] getThreeCellProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {
                new NotNull(new ParseLong()), // id
                new NotNull(),                // name
                new ParseInt()                // age
        } ;
        return processors ;
    }
}
