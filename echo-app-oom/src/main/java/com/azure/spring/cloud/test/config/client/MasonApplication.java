package com.azure.spring.cloud.test.config.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.stream.Stream;

@SpringBootApplication
@RestController
public class MasonApplication {
    private static final Logger logger = LogManager.getLogger(MasonApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MasonApplication.class, args);
    }

    @GetMapping("/{message}")
    public String echo(@PathVariable("message") String message) {
        String retMsg = message + " from echo sample\n";
        System.out.println(retMsg);
        System.out.flush();
        logger.info(retMsg);
        return message;
    }

    @GetMapping("/byos/write/{filename}")
    public String byosWriteFile(@PathVariable("filename") String filename) throws IOException {
        String mountPath = "/springit/byos";
        File file = new File(mountPath + "/" + filename);

        Writer output = new BufferedWriter(new FileWriter(file));

        String msg = "Hello World!!!";
        output.write(msg);
        logger.info(msg);

        output.close();
        return "File Created";
    }

    @GetMapping("/byos/read/{filename}")
    public String byosFileExist(@PathVariable("filename") String filename) throws IOException {
        String mountPath = "/springit/byos";
        File file = new File(mountPath + "/" + filename);

        return String.format("%b", file.exists());
    }

    @GetMapping("/health")
    public String health() {
        return "GREEN";
    }

    @GetMapping("/javaversion")
    public String version() {
        return System.getProperty("java.version");
    }

    @GetMapping("/dump")
    public String heapDumpExist() throws IOException {
        String filePath = "/springit/byos";
        boolean exist = Stream.of(new File(filePath).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .anyMatch(fileName -> fileName.contains("dump"));
        return exist ? "Exist" : "Not Exist";
    }
}
