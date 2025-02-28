$(document).ready(function() {
    $("#btn-login").on("click", function(event) {
        event.preventDefault(); // 기본 폼 제출 방지

        let userId = $("#id").val().trim();
        let userPw = $("#password").val().trim();

        if (userId === "") {
            $("#id-warning").show();
            $("#id").focus();
            return false;
        } else {
            $("#id-warning").hide();
        }

        if (userPw === "") {
            $("#pw-warning").show();
            $("#password").focus();
            return false;
        } else {
            $("#pw-warning").hide();
        }

        // 🔥 AJAX 요청으로 로그인 확인
        $.ajax({
            type: "POST",
            url: "/home",
            contentType: "application/json",
            data: JSON.stringify({ id: userId, password: userPw }), // JSON 형식으로 변환
            dataType: "json",
            success: function(response) {
                if (response.status === "success") {
                    window.location.href = response.redirectUrl; // 성공 시 이동
                } else {
                    alert(response.message); // 로그인 실패 메시지 출력
                }
            },
            error: function() {
                alert("서버 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
    });

    // 입력을 시작하면 경고 메시지 숨기기
    $("#id").on("input", function() { $("#id-warning").hide(); });
    $("#password").on("input", function() { $("#pw-warning").hide(); });
});
