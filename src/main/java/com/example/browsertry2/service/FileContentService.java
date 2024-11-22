package com.example.browsertry2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.io.RandomAccessFile;

@Slf4j
@Service
public class FileContentService {
    private final static String FILE_NAME="log.txt";
    private final static String READ_MODE="r";
    private final static String DESTINATION="/topic/log";

    private long offset;

    private final RandomAccessFile randomAccessFile;

    @Autowired
    private SimpMessagingTemplate simpleMessagingTemplate;

    public FileContentService() throws IOException {
       randomAccessFile=new RandomAccessFile(FILE_NAME,READ_MODE);
       offset=initialOffset();
    }

    @Scheduled(fixedDelay = 100, initialDelay = 5000)
    public void sendUpdates() throws IOException {
        long fileLength=randomAccessFile.length();
        randomAccessFile.seek(offset);

        while(randomAccessFile.getFilePointer()<fileLength){
            String latestLog=randomAccessFile.readLine();
            //make the payload
            String payload="{\"content\":\""+latestLog+"\"}";
            log.info("I have reached here");
            System.out.println("Payload: "+ latestLog);
            log.info("I have reached line after payload print");
            simpleMessagingTemplate.convertAndSend(DESTINATION,payload);
        }
        offset=fileLength;
    }

    private long initialOffset() throws IOException {
        long fileLength = randomAccessFile.length();
        long lines = 0;
        long offset = fileLength;

        // Read the file from the end to find the last 10 lines
        for (long pointer = fileLength - 1; pointer >= 0; pointer--) {
            randomAccessFile.seek(pointer);
            int readByte = randomAccessFile.readByte();
            if (readByte == '\n') {
                lines++;
                if (lines == 11) {
                    offset = pointer + 1;
                    break;
                }
            }
        }

        // If the file has less than 10 lines, set offset to the beginning
        if (lines < 10) {
            offset = 0;
        }

        return offset;
    }
}
