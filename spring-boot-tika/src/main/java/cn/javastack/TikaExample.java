package cn.javastack;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TikaExample {

    public static void main(String[] args) throws IOException, TikaException, SAXException {

        // 创建一个内容处理器和一个元数据实例
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(new File("/Users/maclezhou/JavaWorld/spring-boot-best-practice-3x/spring-boot-tika/src/main/resources/example.docx"));
        ParseContext pcontext = new ParseContext();

        // 自动检测文档类型（探测器的工作）
        Parser parser = new AutoDetectParser();

        // 解析文档并提取内容和元数据
        parser.parse(inputstream, handler, metadata, pcontext);

        // 打印文档内容
        System.out.println("Contents of the document:" + handler.toString());

        // 打印元数据信息
        String[] metadataNames = metadata.names();

        for (String name : metadataNames) {
            System.out.println(name + ": " + metadata.get(name));
        }

        // 关闭输入流
        inputstream.close();
    }
}