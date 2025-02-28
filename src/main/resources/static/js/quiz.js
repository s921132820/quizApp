let answers = {};
let currentIndex = 0; // 현재 퀴즈 인덱스

// 다음 퀴즈 요청
function nextQuiz() {
    fetch('/quizGame/next', { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            console.log("서버 응답:", data); // 디버깅용

            if (data.submit) {
                console.log("퀴즈 종료! 답안 제출 시작");
                submitAnswers();
                return;
            }

            if (data.quiz) {
                let questionElement = document.getElementById("question");
                let trueBtn = document.getElementById("true-btn");
                let falseBtn = document.getElementById("false-btn");
                let indexElement = document.getElementById("currentIndex"); // 추가된 부분

                if (!questionElement || !trueBtn || !falseBtn || !indexElement) {
                    console.error("HTML 요소를 찾을 수 없습니다.");
                    return;
                }

                // 문제 업데이트
                questionElement.innerText = data.quiz.content || data.quiz.question;

                // 현재 퀴즈 번호 업데이트
                currentIndex++;
                indexElement.innerText = currentIndex; // ✅ 화면에 반영

                // 기존 버튼을 제거하고 새로 이벤트 추가
                trueBtn.replaceWith(trueBtn.cloneNode(true));
                falseBtn.replaceWith(falseBtn.cloneNode(true));

                // 새 버튼 요소 가져오기
                trueBtn = document.getElementById("true-btn");
                falseBtn = document.getElementById("false-btn");

                // 새로운 클릭 이벤트 추가
                trueBtn.addEventListener("click", () => selectAnswer(data.quiz.no, true));
                falseBtn.addEventListener("click", () => selectAnswer(data.quiz.no, false));
            }
        })
        .catch(error => console.error("퀴즈 로딩 실패", error));
}


function selectAnswer(quizNo, answer) {
    answers[`answer_${quizNo}`] = answer;
    nextQuiz(); // 다음 문제 요청
}

function submitAnswers() {
    console.log("제출할 답안:", answers); // 추가된 디버깅 코드

    fetch('/member/submitAnswers', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams(answers)
    })
    .then(response => {
        if (response.ok) {
            console.log("답안 제출 완료! 결과 페이지로 이동");
            window.location.href = "/result";
        } else {
            console.error("답안 제출 실패");
        }
    })
    .catch(error => console.error("답안 제출 중 오류 발생", error));
}

// 페이지 로드 시 첫 번째 퀴즈 시작
document.addEventListener("DOMContentLoaded", nextQuiz);
