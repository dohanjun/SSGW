function formattedDate(d){
	return d.getFullYear() + "년 " + ("0" + (d.getMonth() + 1)).slice(-2) + "월 " + ("0" + d.getDate()).slice(-2) + "일";
}