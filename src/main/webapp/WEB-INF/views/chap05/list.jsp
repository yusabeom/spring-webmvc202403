<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <%@ include file="../include/static-head.jsp" %>
    <link rel="stylesheet" href="/assets/css/list.css">
</head>

<body>

<%@ include file="../include/header.jsp" %>

<div id="wrap">

    <div class="main-title-wrapper">
        <h1 class="main-title">꾸러기 게시판</h1>
        <c:if test="${login != null}">
            <button class="add-btn">새 글 쓰기</button>
        </c:if>
    </div>

    <div class="top-section">
        <!-- 검색창 영역 -->
        <div class="search">
            <form action="/board/list" method="get">

                <select class="form-select" name="type" id="search-type">
                    <option value="title">제목</option>
                    <option value="content">내용</option>
                    <option value="writer">작성자</option>
                    <option value="tc">제목+내용</option>
                </select>

                <input type="text" class="form-control" name="keyword" value="${s.keyword}">

                <button class="btn btn-primary" type="submit">
                    <i class="fas fa-search"></i>
                </button>

            </form>
        </div>

        <div class="amount">
            <div><a href="/board/list?pageNo=1&amount=6&type=${s.type}&keyword=${s.keyword}">6</a></div>
            <div><a href="/board/list?pageNo=1&amount=18&type=${s.type}&keyword=${s.keyword}">18</a></div>
            <div><a href="/board/list?pageNo=1&amount=30&type=${s.type}&keyword=${s.keyword}">30</a></div>
        </div>

    </div>

    <!-- 메인 게시판 영역 -->
    <div class="card-container">
        <c:forEach var="b" items="${bList}">
            <div class="card-wrapper">
                <section class="card" data-bno="${b.boardNo}">
                    <div class="card-title-wrapper">
                        <h2 class="card-title">${b.shortTitle}</h2>
                        <div class="time-view-wrapper">
                            <div class="time">
                                <i class="far fa-clock"></i>
                                    ${b.regDate} </div>
                            <div class="view">
                                <i class="fas fa-eye"></i>
                                <span class="view-count">${b.viewCount}</span>
                            </div>
                        </div>
                    </div>
                    <div class="card-content">

                      ${b.shortContent}

                    </div>
                </section>
                
            <c:if test="${login.auth == '관리자회원' || login.account == b.writer}">
                <div class="card-btn-group">
                    <button class="del-btn" data-href="/board/delete?bno=${b.boardNo}">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
            </c:if>

            </div>
        </c:forEach>
    </div>

    <!-- 게시글 목록 하단 영역 -->
    <div class="bottom-section">

        <!-- 페이지 버튼 영역 -->
        <nav aria-label="Page navigation example">
            <ul class="pagination pagination-lg pagination-custom">
                <c:if test="${maker.page.pageNo != 1}">
                    <li class="page-item"><a class="page-link"
                                             href="/board/list?pageNo=1&amount=${s.amount}&type=${s.type}&keyword=${s.keyword}">&lt;&lt;</a>
                    </li>
                </c:if>

                <c:if test="${maker.prev}">
                    <li class="page-item"><a class="page-link"
                                             href="/board/list?pageNo=${maker.begin - 1}&amount=${s.amount}&type=${s.type}&keyword=${s.keyword}">prev</a>
                    </li>
                </c:if>

                <c:forEach var="i" begin="${maker.begin}" end="${maker.end}">
                    <li data-page-num="${i}" class="page-item">
                        <a class="page-link"
                           href="/board/list?pageNo=${i}&amount=${s.amount}&type=${s.type}&keyword=${s.keyword}">${i}</a>
                    </li>
                </c:forEach>

                <c:if test="${maker.next}">
                    <li class="page-item"><a class="page-link"
                                             href="/board/list?pageNo=${maker.end + 1}&amount=${s.amount}&type=${s.type}&keyword=${s.keyword}">next</a>
                    </li>
                </c:if>

                <c:if test="${maker.page.pageNo != maker.finalPage}">
                    <li class="page-item"><a class="page-link"
                                             href="/board/list?pageNo=${maker.finalPage}&amount=${s.amount}&type=${s.type}&keyword=${s.keyword}">&gt;&gt;</a>
                    </li>
                </c:if>
            </ul>
        </nav>

    </div>
</div>


</div>

<!-- 모달 창 -->
<div class="modal" id="modal">
    <div class="modal-content">
        <p>정말로 삭제할까요?</p>
        <div class="modal-buttons">
            <button class="confirm" id="confirmDelete"><i class="fas fa-check"></i> 예</button>
            <button class="cancel" id="cancelDelete"><i class="fas fa-times"></i> 아니오</button>
        </div>
    </div>
</div>


<script>

    // 카드 형태의 게시물들을 감싸고 있는 부모 요소 취득
    const $cardContainer = document.querySelector('.card-container');

    // 삭제에 필요한 요소드을 먼저 얻겠습니다.
    const $modal = document.getElementById('modal'); // 모달창 얻기
    const $confirmDelete = document.getElementById('confirmDelete');
    const $cancelDelete = document.getElementById('cancelDelete');

    $cardContainer.addEventListener('click', e => {
        console.log('이벤트 타겟: ', e.target);
        if(e.target.matches('.card-container')) return;

        // 삭제 버튼을 눌렀다면~
        if (e.target.matches('.card-btn-group *')) {
            console.log('삭제 버튼 클릭됨!');
            $modal.style.display = 'flex'; // 숨겨진 모달창을 드러내기.

             // 이벤트가 발생한 타겟에서 가장 가까운 .del.-btn이 가지고 있는 data-href를 얻는다.
             const deleteLocation = e.target.closest('.del-btn').dataset.href;

            // 확인 버튼 이벤트
            $confirmDelete.onclick = e => {
                // 삭제 요청을 서버에 보내야 한다.  
                location.href = deleteLocation;
                // 모달창을 닫아야 한다.
                $modal.style.display = 'none';
            }

            $cancelDelete.onclick = () => {
                $modal.style.display = 'none';
            }

        } else { // 삭제 버튼을 제외한 부분은 글 상세조회 요청이다.

             console.log('card-wapper에 이벤트 발생!');
            // section 태그에 붙은 글 번호를 읽어오자
            // 이벤트가 발생한 타겟에서 가장 가까운 section.card를 지목해서 data-bno를 얻어오기.
            const bno = e.target.closest('section.card').dataset.bno;
            console.log('bno: ' + bno);

            // 서버에 요청 보내기
            location.href='/board/detail/' + bno +
             '?pageNo=${s.pageNo}&amount=${s.amount}&type=${s.type}&keyword=${s.keyword}'; // ?bno=를 생략하고 앞에 /를 작성. 
        }

        

    });
  //========== 게시물 목록 스크립트 ============//

  function removeDown(e) {
    if (!e.target.matches('.card-container *')) return;
    const $targetCard = e.target.closest('.card-wrapper');
    $targetCard?.removeAttribute('id', 'card-down');
  }

  function removeHover(e) {
    if (!e.target.matches('.card-container *')) return;
    const $targetCard = e.target.closest('.card');
    $targetCard?.classList.remove('card-hover');

    const $delBtn = e.target.closest('.card-wrapper')?.querySelector('.del-btn');
    $delBtn.style.opacity = '0';
  }


  $cardContainer.onmouseover = e => {

    if (!e.target.matches('.card-container *')) return;

    const $targetCard = e.target.closest('.card');
    $targetCard?.classList.add('card-hover');

    const $delBtn = e.target.closest('.card-wrapper')?.querySelector('.del-btn');
    $delBtn.style.opacity = '1';
  }

  $cardContainer.onmousedown = e => {

    if (e.target.matches('.card-container .card-btn-group *')) return;

    const $targetCard = e.target.closest('.card-wrapper');
    $targetCard?.setAttribute('id', 'card-down');
  };

  $cardContainer.onmouseup = removeDown;

  $cardContainer.addEventListener('mouseout', removeDown);
  $cardContainer.addEventListener('mouseout', removeHover);

  // write button event
  document.querySelector('.add-btn').onclick = e => {
    window.location.href = '/board/write';
  };

  // 사용자가 현재 머물고 있는 페이지 버튼에 active 스타일 부여
  function appendPageActive() {
    
    // 현재 서버에서 넘겨준 페이지 번호
    const currPage = '${maker.page.pageNo}';

    // li 태그들을 전부 확인해서
    // 현재 페이지 번호와 일치하는 li를 찾은 후 active 클래스 이름 붙이기
    const $ul = document.querySelector('.pagination');
    const $liList = [...$ul.children];

    $liList.forEach($li => {
        if (currPage === $li.dataset.pageNum) {
            $li.classList.add('active');
        }
    });

  }

  // 검색 조건 셀렉트 박스 옵션타입 고정하기
  function fixSearchOption() {
    const $select = document.getElementById('search-type');
    // 셀렉트 박스 내에 있는 option 태그들 전부 가져오기
    const $options = [...$select.children];

    $options.forEach($opt => {
        if ($opt.value === '${s.type}') {
            // option 태그에 selected를 주면 그 option이 고정됨.
            $opt.setAttribute('selected', 'selected');
        }
    });

  }



  appendPageActive();
  fixSearchOption();
  




</script>

</body>

</html>