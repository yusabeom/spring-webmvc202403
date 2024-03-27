package com.spring.mvc.chap05.repository;

import com.spring.mvc.chap04.entity.Grade;
import com.spring.mvc.chap04.entity.Score;
import com.spring.mvc.chap05.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository // 빈등록 -> 서비스가 꼭 필요로 하기 때문
@RequiredArgsConstructor // final 변수가 있기 때문에 필요한 생성자 생성
public class BoardRepositoryImpl implements BoardRepository{

    public class BoardMapper implements RowMapper<Board> {
        @Override
        public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
            Board board = new Board(
                    rs.getInt("board_no"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getInt("view_count"),
                    rs.getTimestamp("reg_date").toLocalDateTime(),
                    rs.getString("writer")
            );

            return board;
        }
    }

    private final JdbcTemplate template;

    @Override
    public List<Board> findAll() {
        String sql = "SELECT * FROM tbl_board ORDER BY board_no DESC";

        List<Board> boardList = template.query(sql, new BoardMapper());
        return boardList;
    }

    @Override
    public Board findOne(int boardNo) {
        String sql = "SELECT * FROM tbl_board WHERE board_no=?";
        try {
            return template.queryForObject(sql, new BoardMapper(), boardNo);
        } catch (DataAccessException e) {
            return null; // 없는 글 번호라면 null을 리턴하겠다.
        }
    }

    @Override
    public void save(Board board) {
        String sql = "INSERT INTO tbl_board (title, content, writer) " +
                "VALUES(?,?,?)";

        template.update(sql,board.getTitle(), board.getContent(), board.getWriter());
    }

    @Override
    public void delete(int boardNo) {
        String sql = "DELETE FROM tbl_board WHERE board_no=?";

        template.update(sql, boardNo);
    }
}
