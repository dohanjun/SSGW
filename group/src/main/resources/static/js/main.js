document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ JavaScript 로드 완료!");

    let startWorkBtn = document.getElementById("startWorkBtn");
    let endWorkBtn = document.getElementById("endWorkBtn");

    if (!startWorkBtn || !endWorkBtn) {
        console.error("❌ 출근/퇴근 버튼을 찾을 수 없습니다!");
        return;
    }

    // ✅ 출근 버튼 클릭 이벤트
    startWorkBtn.addEventListener("click", function () {
        console.log("✅ 출근 버튼 클릭됨!");

        fetch('/attendance/clock-in', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        })
        .then(response => response.json())
        .then(data => {
            console.log("✅ 서버 응답:", data);
            alert(data.message);
        })
        .catch(error => console.error('❌ 오류 발생:', error));
    });

    // ✅ 퇴근 버튼 클릭 이벤트
    endWorkBtn.addEventListener("click", function () {
        console.log("✅ 퇴근 버튼 클릭됨!");

        fetch('/attendance/clock-out', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        })
        .then(response => response.json())
        .then(data => {
            console.log("✅ 서버 응답:", data);
            alert(data.message);
        })
        .catch(error => console.error('❌ 오류 발생:', error));
    });
});
