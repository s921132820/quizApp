$(document).ready(function() {
    $("#btn-login").on("click", function() {
        let userId = $("#id").val().trim(); // 입력값 가져오기
        let userPw = $("#password").val().trim();

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
    });

    // 입력을 시작하면 메시지 자동으로 사라지게 하기
    $("#id").on("input", function() {
        $("#id-warning").hide();
    });

    $("#password").on("input", function() {
        $("#pw-warning").hide();
    })
});
