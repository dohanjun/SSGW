
function payment(type) {
	var merchant_uid = "order" + new Date().getTime();
	var username = $("#subName").val();
	var userEmail = $("#subEmail").val();
	var userPhone = $("#phoneNo").val();
	let num =
		IMP.init('imp07168373');

	IMP.request_pay(
		{
			pg: "html5_inicis",
			pay_method: type,
			merchant_uid: merchant_uid,
			name: username + "님의 결제",
			amount: 100,
			buyer_email: userEmail,
			buyer_name: username,
			buyer_tel: userPhone,
			escrow: true,
			vbank_due: "YYYYMMDD",
		},
		function(rsp) {
			if (rsp.success) {
				$.ajax({
					type: "POST",
					url: "/savePayment",
					data: JSON.stringify({
						paymentType: type,
						paymentPrice: totalDiff,
						suberNo: suber.suberNo,
						paymentStatus: "완료",
						claim: "Y",
						claimState: "완료"
					}),
					contentType: "application/json",
					success: function(response2) {
					},
					error: function(xhr, status, error) {
						alert("❌ 결제 정보 저장 중 오류가 발생했습니다.");
						console.error(xhr.responseText);
					}
				});

			} else {
				var mesg = '결제를 실패하였습니다.';
				console.log(rsp);
				alert(mesg + rsp.error_msg);
			}
		}
	);
}

