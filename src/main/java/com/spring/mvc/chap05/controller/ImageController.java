package com.spring.mvc.chap05.controller;

import com.spring.mvc.chap05.dto.response.LoginUserResponseDTO;
import com.spring.mvc.chap05.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@Slf4j
@RequestMapping("/display")
public class ImageController {

    @Value("${file.upload.root-path}")
    private String rootPath;
    private MemberService memberService;

    /*
    # img 태그의 src 속성을 통해서 들어오는 요청을 처리하는 방법.
    페이지가 렌더링되고, img 태그가 생성될 때 src 에 작성된 요청 url 을 통해
    자동으로 요청이 들어오게 됩니다.
     */

    @GetMapping("/{y}/{m}/{d}/{fileName}")
    public ResponseEntity<?> getImage(@PathVariable String y,
                                      @PathVariable String m,
                                      @PathVariable String d,
                                      @PathVariable String fileName) {
        log.info("/display/{}/{}/{}/{}: GET!", y, m, d, fileName);

        String fullPath = String.format("%s/%s/%s/%s/%s", rootPath, y, m, d, fileName);
        log.info("fullPath: {}", fullPath);
        File file = new File(fullPath);

        ResponseEntity<byte[]> result = null;
        HttpHeaders headers = new HttpHeaders(); // 응답 헤더 객체 생성.

        try {
            // probeContentType: 매개값으로 전달받은 파일의 타입이 무엇인지를 문자열로 반환.
            headers.add("Content-Type", Files.probeContentType(file.toPath()));

            // ResponseEntity 객체에 전달하고자 하는 파일을 byte[]로 변환해서 전달. (파일의 손상을 막기 위해)
            // header 내용도 같이 포함, 응답 상태 코드도 원하는 형태로 전달 가능.
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return result;
    }

    // 다운로드 요청
    @GetMapping("/download/{y}/{m}/{d}/{fileName}")
    public ResponseEntity<?> download(@PathVariable String y,
                                      @PathVariable String m,
                                      @PathVariable String d,
                                      @PathVariable String fileName) {
        log.info("/display/download/{}/{}/{}/{}: GET!", y, m, d, fileName);

        String fullPath = String.format("%s/%s/%s/%s/%s", rootPath, y, m, d, fileName);
        log.info("fullPath: {}", fullPath);
        File file = new File(fullPath);

        ResponseEntity<byte[]> result = null;
        HttpHeaders headers = new HttpHeaders(); // 응답 헤더 객체 생성.

        //응답하는 본문을 브라우저가 어떻게 표시해야 할 지 알려주는 헤더 정보를 추가합니다.
        //inline 인 경우 웹 페이지 화면에 표시되고, attachment 인 경우 다운로드를 제공합니다.

        //request 객체의 getHeader("User-Agent") -> 단어를 뽑아서 확인
        //ie: Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko
        //chrome: Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36

        //파일명 한글처리(Chrome browser) 크롬
        //header.add("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1") );
        //파일명 한글처리(Edge) 엣지
        //header.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        //파일명 한글처리(Trident) IE
        //Header.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", " "));

        headers.add("Content-Disposition", "attachment; filename=" + fileName);

        try {
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }
}















