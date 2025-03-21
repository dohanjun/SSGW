let formattedTPrice = 0;
let dataList = [];
$(document).ready(updatePrice);
function updatePrice() {
	let totalModulePrice = 0;
	dataList.forEach(e => {
		totalModulePrice += Number(e.modulePrice);
	})
	let countValue = $('.countInput')[0].value;
	let uploadValue = $('.uploadInput')[0].value;
	let storageValue = $('.storageInput')[0].value;
	let periodValue = $('.periodInput')[0].value;

	let mPrice = totalModulePrice.toLocaleString('ko-KR');

	let pPrice = (countValue / 10 * 100000).toLocaleString('ko-KR');
	let fuPrice = (uploadValue / 5 * 10000).toLocaleString('ko-KR');
	let fRPrice = (storageValue * 100000).toLocaleString('ko-KR');
	let tPrice = (countValue / 10 * 100000) + (uploadValue / 5 * 10000) + (storageValue * 100000) + totalModulePrice;

	formattedTPrice = tPrice.toLocaleString('ko-KR');

	$('#moculePrice')[0].innerHTML = mPrice;
	$('#peoplePrice')[0].innerHTML = pPrice;
	$('#fileUploadPrice')[0].innerHTML = fuPrice;
	$('#fileRepositoryPrice')[0].innerHTML = fRPrice;
	$('#tPrice')[0].innerHTML = formattedTPrice;

	if (periodValue == 12) {
		let discountedPrice = tPrice * 12 * 0.8;
		$('#ysale')[0].innerHTML = discountedPrice.toLocaleString('ko-KR');
		$('#endPrice')[0].innerHTML = $('#ysale')[0].innerHTML;
	} else {
		$('#ysale')[0].innerHTML = formattedTPrice;
		$('#endPrice')[0].innerHTML = formattedTPrice;
	}
}


document.querySelectorAll('.selectModule .selectItem *').forEach(e => {

	e.addEventListener('click', f => {

		f.currentTarget.classList.toggle('check');

		let attrValue = f.target.getAttribute('for');
		if (attrValue) {
			let data = {};
			let cleanedAttrValue = attrValue.replace(/^[^\(]*\(|\)$/g, '');

			cleanedAttrValue.split(', ').forEach(pair => {
				let [key, value] = pair.split('=');
				if (value) {
					data[key] = value;
				}
			});

			if (f.target.classList.contains('check')) {
				dataList.push(data);
			} else {
				dataList = dataList.filter(item => item.moduleNo !== data.moduleNo);
			}
			updatePrice();
		}
	});
});


document.querySelector('.plusButton').addEventListener('click', event => {
	event.preventDefault();
	let countInput = document.querySelector('.countInput');
	let count = Number(countInput.value);

	if (count < 100) {
		count += 10;
		countInput.value = count;
		updatePrice();
	}
});

document.querySelector('.minusButton').addEventListener('click', event => {
	event.preventDefault();
	let countInput = document.querySelector('.countInput');
	let count = Number(countInput.value);

	if (count > 10) {
		count -= 10;
		countInput.value = count;
		updatePrice();
	}
});


function setupDropdown(inputClass, dropdownClass, optionClass) {
	let input = document.querySelector(inputClass);
	let dropdown = document.querySelector(dropdownClass);

	input.addEventListener('click', function(event) {
		dropdown.style.display = dropdown.style.display === 'none' ? 'block' : 'none';
		event.stopPropagation();
	});

	document.querySelectorAll(optionClass).forEach(option => {
		option.addEventListener('click', function(event) {
			input.value = this.dataset.value;
			dropdown.style.display = 'none';
			updatePrice();
			event.stopPropagation();
		});
	});
}

document.addEventListener('click', function(event) {
	document.querySelectorAll('.uploadDropdown, .storageDropdown, .periodDropdown, .paymentDropdown')
		.forEach(dropdown => dropdown.style.display = 'none');
});
document.addEventListener("DOMContentLoaded", function() {
	let form = document.querySelector("form");
	let paymentModal = document.getElementById("paymentModal");

	form.addEventListener("submit", function(event) {
		event.preventDefault();


		for (let e of document.querySelectorAll('.form-control')) {
			if (!e.value.trim()) {
				let label = document.querySelector(`label[for="${e.id}"]`);
				alert(label.innerHTML + "값을(를) 입력하세요.");
				e.focus();
				return;
			}
		}
		console.log(dataList)
		$('#modalPrice')[0].innerHTML = $('#endPrice')[0].innerHTML
		paymentModal.style.display = "flex";
	});

	document.querySelectorAll(".payMent button").forEach(e => {
		e.addEventListener("click", btn => {
			if (!document.querySelector(".clauseCheck input[type='checkbox']").checked) {
				alert("이용약관에 동의해야 결제가 진행됩니다.");
				return;
			}
			if (btn.target.id == "card") {
				payment("card")
			}
			else if (btn.target.id == "trans") {
				payment("trans")
			}
		});
	});
});
document.querySelector('.close').addEventListener('click', function() {
	document.getElementById('paymentModal').style.display = 'none';
});

function payment(type) {
	var merchant_uid = "order" + new Date().getTime();
	var username = $("#subName").val();
	var userEmail = $("#subEmail").val();
	var userPhone = $("#phoneNo").val();
	let num = parseInt($('.periodInput')[0].value) === 12 ? Number($('#endPrice')[0].innerHTML.replace(/,/g, '')) * 12 * 0.8 : Number($('#endPrice')[0].innerHTML.replace(/,/g, ''));
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
					url: "/saveSuber",
					data: JSON.stringify({
						domain: "@" + $("#subId").val() + ".com",
						firstIp: first_ip,
						subName: $("#subName").val(),
						subId: $("#subId").val(),
						subUid: "UID" + Date.now().toString() + Math.floor(Math.random() * 1000).toString().padStart(3, "0"),
						subPw: $("#subPw").val(),
						companyName: $("#companyName").val(),
						subEmail: $("#subEmail").val(),
						subBnisNo: $("#subBnisNo").val(),
						maxCount: $(".countInput").val(),
						maxSize: $(".storageInput").val(),
						maxUpSize: $(".uploadInput").val()
					}),
					contentType: "application/json",
					success: function(response) {
						var suberNo = response;

						$.ajax({
							type: "POST",
							url: "/saveSubDetail",
							data: JSON.stringify(
								dataList.map(module => ({
									suberNo: suberNo,
									moduleNo: module.moduleNo,
									subPeriod: $('.periodInput')[0].value,
									discount: parseInt($('.periodInput')[0].value) === 12 ? 'Y' : 'N',
									discountRate: parseInt($('.periodInput')[0].value) === 12 ? 0.200 : 0.000,
									discountPrice: parseInt($('.periodInput')[0].value) === 12 ? Number(module.modulePrice) * 12 * 0.8 : Number(module.modulePrice)
								}))
							),
							contentType: "application/json",
							success: function(response) {
								subDetailsNoList = response.map(detail => ({
									subDetailsNo: detail.subDetailsNo,
									discountPrice: detail.discountPrice
								}));

								$.ajax({
									type: "POST",
									url: "/savePayment",
									data: JSON.stringify({
										paymentType: type,
										paymentPrice: num,
										suberNo: suberNo,
										subPeriod: $('.periodInput')[0].value,
										paymentStatus: "완료",
										claim: "Y",
										claimState: "완료"
									}),
									contentType: "application/json",
									success: function(response2) {
										let paymentNo = response2.paymentNo;

										let paymentDetailsList = subDetailsNoList.map(detail => ({
											paymentNo: paymentNo,
											subDetailsNo: detail.subDetailsNo,
											paymentPrice: detail.discountPrice,
											paymentState: "완료"
										}));

										$.ajax({
											type: "POST",
											url: "/savePaymentDetails",
											data: JSON.stringify(paymentDetailsList),
											contentType: "application/json",
											success: function(response3) {
												$.ajax({
													type: "POST",
													url: "/saveUser",
													data: JSON.stringify({
														  employeeId: $("#subId").val(),         // EMPLOYEE_ID
														  employeePw: $("#subPw").val(),         // EMPLOYEE_PW
														  employeeName: $("#subName").val(),     // EMPLOYEE_NAME
														  resignationStatus: "N",                // RESIGNATION_STATUS: "N"이면 재직 중
														  tempIp: null,                            // TEMP_IP: 필요시 서버나 클라이언트에서 IP를 설정
														  passwordChanged: "N",                // PASSWORD_CHANGED: 초기에는 false 처리
														  hireDate: new Date().toISOString().split('T')[0],  // HIRE_DATE: 오늘 날짜 (YYYY-MM-DD 형식)
														  exitDate: null,                        // EXIT_DATE: 퇴사시 기록, 초기에는 null
														  phoneNumber: $("#phoneNo").val(),      // PHONE_NUMBER
														  address: null,    // ADDRESS: 주소 입력값 (폼에 없으면 빈 문자열)
														  profileImage: null,                    // PROFILE_IMAGE: 이미지 경로 또는 URL (파일 업로드 시 처리)
														  suberNo: suberNo,                      // SUBER_NO: 이미 변수로 선언되어 있는 값
														  rankId: 7,                             // RANK_ID: 초기값 7
														  rightsId: 1,                           // RIGHTS_ID: 초기값 1
														  manager: null,                         // MANAGER: 초기에는 매니저 없음
														  departmentNo: null,                    // DEPARTMENT_NO: 부서 미지정 상태
														  profileImageBlob: null                 // PROFILE_IMAGE_BLOB: 이미지 파일의 blob 데이터 (필요시 할당)
													}),
													contentType: "application/json",
													success: function(response3) {
														location.href = "login";
													},
													error: function(xhr, status, error) {
														alert("❌ 결제 상세 정보 저장 중 오류 발생.");
														console.error(xhr.responseText);
													}
												});
											},
											error: function(xhr, status, error) {
												alert("❌ 결제 상세 정보 저장 중 오류 발생.");
												console.error(xhr.responseText);
											}
										});

									},
									error: function(xhr, status, error) {
										alert("❌ 결제 정보 저장 중 오류가 발생했습니다.");
										console.error(xhr.responseText);
									}
								});

							},
							error: function(xhr, status, error) {
								alert("❌ 구독 상세 저장 중 오류 발생.");
								console.error(xhr.responseText);
							}
						});

					},
					error: function(xhr, status, error) {
						alert("❌ 구독자 정보 저장 중 오류 발생.");
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




setupDropdown('.uploadInput', '.uploadDropdown', '.uploadOption');
setupDropdown('.storageInput', '.storageDropdown', '.storageOption');
setupDropdown('.periodInput', '.periodDropdown', '.periodOption');
setupDropdown('.paymentInput', '.paymentDropdown', '.paymentOption');