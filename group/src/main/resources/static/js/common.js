function formattedDate(d){
	return d.getFullYear() + "년 " + ("0" + (d.getMonth() + 1)).slice(-2) + "월 " + ("0" + d.getDate()).slice(-2) + "일";
}

function formattedTime(d) {
    const hours = ("0" + d.getHours()).slice(-2);  // 시간을 2자리로 포맷
    const minutes = ("0" + d.getMinutes()).slice(-2);  // 분을 2자리로 포맷
    return `${hours}:${minutes}`;
}