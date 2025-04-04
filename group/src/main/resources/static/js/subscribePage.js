let formattedTPrice = 0;
let dupId = true;
let dataList = [];

$(document).ready(updatePrice);
function updatePrice() {
	let totalModulePrice = 0;
	dataList.forEach(e => {
		console.log(e)
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


document.querySelectorAll('.selectItem').forEach(item => {
	item.addEventListener('click', function () {
		if (this.classList.contains('basic-locked')) return;
		
		this.classList.toggle('check');

		const moduleNo = this.getAttribute('data-module-no');
		const modulePrice = this.getAttribute('data-module-Price');
		const data = {
			moduleNo: parseInt(moduleNo),
			modulePrice: parseInt(modulePrice)
		};
		if (this.classList.contains('check')) {
			// 중복 방지
			if (!dataList.some(item => item.moduleNo === data.moduleNo)) {
				dataList.push(data);
			}
		} else {
			// 체크 해제 시 제거
			dataList = dataList.filter(item => item.moduleNo !== data.moduleNo);
		}

		updatePrice();
	});
});

$(document).ready(function () {
	updatePrice();

	// 체크된 모듈만 초기 dataList에 담기 (기본 모듈 체크 상태)
	$('.selectItem.check').each(function () {
		const moduleNo = $(this).data('module-no');
		const modulePrice = $(this).data('module-price');
		dataList.push({
			moduleNo: parseInt(moduleNo),
			modulePrice: parseInt(modulePrice)
		});
	});

	updatePrice(); // 가격 갱신
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
	const form = document.querySelector("#subscribeForm");
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
		if (dupId) {
			alert("아이디 중복확인을 하세요");
			return;
		}

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
		async function(rsp) {
			if (rsp.success) {
				let suberNo = await $.ajax({
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
					contentType: "application/json"
				})

				//2 구독중인 모듈 정보 저장
				let response = await $.ajax({
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
					contentType: "application/json"
				})

				
				let subDetailsNoList = response.map(detail => ({
					subDetailsNo: detail.subDetailsNo,
					discountPrice: detail.discountPrice
				}));

				//3 결제 정보 저장				
				let paymentDetailsList = subDetailsNoList.map(detail => ({
					subDetailsNo: detail.subDetailsNo,
					paymentPrice: detail.discountPrice,
					paymentState: "완료"
				}));


				let paymentdata = {
					paymentType: type,
					paymentPrice: num,
					suberNo: suberNo,
					subPeriod: $('.periodInput')[0].value,
					paymentStatus: "완료",
					claim: "Y",
					claimState: "완료",
					paymentDetailsList
				}

				await $.ajax({
					type: "POST",
					url: "/savePayment",
					data: JSON.stringify(paymentdata),
					contentType: "application/json"
				})

				// 5 부서 생성
				let data = JSON.stringify({
					departmentName: "대표",
					departmentLevel: 1,
					manager: 1,
					suberNo: suberNo
				});

				let response3 = await $.ajax({
					type: "POST",
					url: "/api/saveDepartMent",
					data: data,
					contentType: "application/json"
				});

				//6 대표 정보 저장
				let empnum = await $.ajax({
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
						departmentNo: response3,                    // DEPARTMENT_NO: 부서 미지정 상태
						profileImageBlob: null                 // PROFILE_IMAGE_BLOB: 이미지 파일의 blob 데이터 (필요시 할당)
					}),
					contentType: "application/json"
				});
				
				await $.ajax({
					type: "POST",
					url: "/api/updateDepartMent",
					data: data = JSON.stringify({
						manager: empnum,
						suberNo: suberNo
					}),
					contentType: "application/json",
					success: function() {
						alert("구독성공")
						location.href = "login";
					},
				})


			} else {
				var mesg = '결제를 실패하였습니다.';
				console.log(rsp);
				alert(mesg + rsp.error_msg);
			}
		}
	);
}


$('#checkDuplicateBtn').on('click', function() {
	var subId = $('#subId').val();
	if ($('#subId').val() == '') {
		$('#idCheckMessage').text('아이디를 입력해주세요.');
		return;
	}

	$.ajax({
		url: '/checkDuplicateId',
		type: 'GET',
		data: { subId: subId },
		success: function(response) {
			if (response === true) {
				$('#idCheckMessage').css('color', 'red').text('이미 사용 중인 아이디입니다.');
				dupId = true;
			} else {
				$('#idCheckMessage').css('color', 'blue').text('사용 가능한 아이디입니다.');
				dupId = false;
			}
		},
		error: function() {
			$('#idCheckMessage').css('color', 'red').text('오류가 발생했습니다.');
		}
	});
});


setupDropdown('.uploadInput', '.uploadDropdown', '.uploadOption');
setupDropdown('.storageInput', '.storageDropdown', '.storageOption');
setupDropdown('.periodInput', '.periodDropdown', '.periodOption');
setupDropdown('.paymentInput', '.paymentDropdown', '.paymentOption');


