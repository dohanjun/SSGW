let formattedTPrice = 0;
$(document).ready(updatePrice);
function updatePrice() {
	let countValue = $('.countInput')[0].value;
	let uploadValue = $('.uploadInput')[0].value;
	let storageValue = $('.storageInput')[0].value;
	let modulePrice = Number($('#moculePrice')[0].innerHTML);
	let periodValue = $('.periodInput')[0].value;

	let pPrice = (countValue / 10 * 100000).toLocaleString('ko-KR');
	let fuPrice = (uploadValue / 5 * 10000).toLocaleString('ko-KR');
	let fRPrice = (storageValue * 100000).toLocaleString('ko-KR');

	let tPrice = (countValue / 10 * 100000) + (uploadValue / 5 * 10000) + (storageValue * 100000) + modulePrice;
	formattedTPrice = tPrice.toLocaleString('ko-KR');

	$('#peoplePrice')[0].innerHTML = pPrice;
	$('#fileUploadPrice')[0].innerHTML = fuPrice;
	$('#fileRepositoryPrice')[0].innerHTML = fRPrice;
	$('#tPrice')[0].innerHTML = formattedTPrice;

	if (periodValue == 12) {
		let discountedPrice = tPrice * 0.8;
		$('#ysale')[0].innerHTML = discountedPrice.toLocaleString('ko-KR');
		$('#endPrice')[0].innerHTML = $('#ysale')[0].innerHTML;
	} else {
		$('#ysale')[0].innerHTML = formattedTPrice;
		$('#endPrice')[0].innerHTML = formattedTPrice;
	}
}


let count = document.querySelector('.countInput').value;

document.querySelectorAll('.selectModule .selectmd').forEach(e => {
	e.addEventListener('click', f => {
		f.target.classList.toggle('check')
	})
})

document.querySelector('.plusButton').addEventListener('click', event => {
	event.preventDefault();
	if (count < 100) {
		count = Number(count) + 10
		document.querySelector('.countInput').value = count
		updatePrice()
	}
})

document.querySelector('.minusButton').addEventListener('click', event => {
	event.preventDefault();
	if (count > 10) {
		count = Number(count) - 10
		document.querySelector('.countInput').value = count
		updatePrice()
	}
})

function setupDropdown(inputClass, dropdownClass, optionClass) {
	let input = document.querySelector(inputClass);
	let dropdown = document.querySelector(dropdownClass);

	input.addEventListener('click', function() {
		dropdown.style.display = dropdown.style.display === 'none' ? 'block' : 'none';
	});

	document.querySelectorAll(optionClass).forEach(option => {
		option.addEventListener('click', function() {
			input.value = this.dataset.value;
			dropdown.style.display = 'none';
		});
	});

	document.addEventListener('click', function(event) {
		if (!input.contains(event.target) && !dropdown.contains(event.target)) {
			dropdown.style.display = 'none';
			updatePrice()
		}
	});
}

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
	let num = Number(formattedTPrice.replace(/,/g, ''));
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
				console.log(rsp)
			}
			else {
				var mesg = '결제를 실패하였습니다.';
				console.log(rsp)
				alert(mesg + rsp.error_msg);
			}
		}
	);
}

setupDropdown('.uploadInput', '.uploadDropdown', '.uploadOption');
setupDropdown('.storageInput', '.storageDropdown', '.storageOption');
setupDropdown('.periodInput', '.periodDropdown', '.periodOption');
setupDropdown('.paymentInput', '.paymentDropdown', '.paymentOption');