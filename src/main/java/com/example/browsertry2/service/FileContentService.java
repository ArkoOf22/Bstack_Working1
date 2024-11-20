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
        long lines=0;

        while(randomAccessFile.readLine()!=null)
            lines++;

        if(lines>10)
            offset=lines-10;

        return offset;
    }
}
