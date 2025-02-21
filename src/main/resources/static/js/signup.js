$(document).ready(function() {
    $("#btn-submit").on("click", function() {

        let userId = $("#id").val().trim(); // 입력값 가져오기
        let userPw = $("#password").val().trim();
        let userPwConfirm = $("#confirm-password").val().trim()

        if (userId === "") {
            $("#id-warning").show();  // 경고 메시지 표시
            $("#id").focus();         // 입력창에 포커스
            return false;             // 이벤트 중단
        } else {
            $("#id-warning").hide();  // 입력이 있으면 메시지 숨김
        }

        if(userPw === "") {
            $("#pw-warning").show();
            $("#password").focus();
            return false;
        } else {
            $("#pw-warning").hide();
        }

        if(userPwConfirm === "") {
            $("#pw-warning").show();
            $("#confirm-password").focus();
            return false;
        } else {
            $("#pw-warning").hide();
        }

        if(userPw !== userPwConfirm) {
            $("#confirm-warning").show();
            $("#confirm-password").focus();
            return false;
        } else {
            $("#confirm-warning").hide();
        }

    });

    // 입력을 시작하면 메시지 자동으로 사라지게 하기
    $("#id").on("input", function() {
        $("#id-warning").hide();
    });

    $("#password").on("input", function() {
        $("#pw-warning").hide();
    })

    $("#confirm-password").on("input", function() {
        $("#pw-warning").hide();
    })

    // 아이디 중복 확인 버튼 클릭 이벤트
    $("#btn-checkId").on("click", function() {
        let userId = $("#id").val().trim();

        if (userId === "") {
            $("#id-warning").text("아이디를 입력하세요.").show();
            return;
        }

        // AJAX로 서버에 아이디 중복 체크 요청
        $.ajax({
            type: "GET",
            url: "/member/checkId",
            data: { id: userId },
            success: function(response) {
                if (response.exists) {  // ✅ 서버 응답을 올바르게 확인
                    $("#id-check-msg").text("이미 존재하는 아이디입니다.").css("color", "red").show();
                } else {
                    $("#id-check-msg").text("사용 가능한 아이디입니다.").css("color", "green").show();
                }
            },
            error: function() {
                alert("아이디 중복 확인 중 오류 발생!");
            }
        });
    });
});
