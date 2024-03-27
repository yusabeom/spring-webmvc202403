package com.spring.mvc.chap05.controller;

import com.spring.mvc.chap05.dto.request.BoardWriteRequestDTO;
import com.spring.mvc.chap05.dto.response.BoardListResponseDTO;
import com.spring.mvc.chap05.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService service;

    // 1. 목록 조회 요청 (/board/list : GET)
    // chap05/list.jsp
    @GetMapping("/list")
    public String list(Model model) {
        List<BoardListResponseDTO> dtoList = service.getList();

        model.addAttribute("bList", dtoList);
        return "chap05/list";
    }

    // 2. 글쓰기 화면요청 (/board/write : GET)
    // chap05/write.jsp 로 이동
    @GetMapping("/write")
    public String write() {
        System.out.println("/board/write: GET!!!");
        return "chap05/write";
    }
    // 3. 글쓰기 등록요청 (/board/write : POST)
    // BoardWriteRequestDTO를 활용하여 파라미터 처리 -> dto.request 패키지에 생성
    // 등록 완료 후에는 목록 조회 요청이 다시 들어오게끔 처리.
    @PostMapping("/write")
    public String write(BoardWriteRequestDTO dto) {
        System.out.println("/board/write: POST!!!");


        service.save(dto);

        return "redirect:/board/list";
    }

    // 4. 글 삭제 요청 (/board/delete : GET)
    // 글번호 전달되면 삭제 진행

    // 5. 글 상세보기 요청 (/board/detail : GET)
    // 글 번호 전달되면 해당 내용 상세보기 처리
    // chap05/detail.jsp


}
