let formattedTPrice = 0;
let dataList = []; 
$(document).ready(updatePrice);
function updatePrice() {
	let totalModulePrice = 0;
	dataList.forEach(e=>{
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
	let tPrice = (countValue / 10 * 100000) + (uploadValue / 5 * 10000) + (storageValue * 100000)  + totalModulePrice;
	
	formattedTPrice = tPrice.toLocaleString('ko-KR');
	
	$('#moculePrice')[0].innerHTML = mPrice;
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
		$('#modalPrice')[0].innerHTML = formattedTPrice
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
	console.log(num)
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
				    url: "/save",
					data: JSON.stringify({
					        subscriber: {
								domain : "@"+$("#subId").val(),
								firstIp:first_ip,
					            subName: $("#subName").val(),
					            subId: $("#subId").val(),
								subUid:"UID" + Date.now().toString() + Math.floor(Math.random() * 1000).toString().padStart(3, "0"),
					            subPw: $("#subPw").val(),
					            companyName: $("#companyName").val(),
					            subEmail: $("#subEmail").val(),
					            subBnisNo: $("#subBnisNo").val(),
					            maxCount: $(".countInput").val(),
					            maxSize: $(".storageInput").val(), 
					            maxUpSize: $(".uploadInput").val()
					        },
					       /* payment: {
					            amount: $("#amount").val(),
					            method: $("#paymentMethod").val()
					        },
					        modules: [
					            { moduleId: 1, moduleName: "인사관리" },
					            { moduleId: 2, moduleName: "근태관리" }
					        ]*/
					    }),
				    contentType: "application/json",
				    success: function(response) {
				        alert("결제 정보가 정상적으로 저장되었습니다.");
				        console.log(response);
				    },
				    error: function(xhr, status, error) {
				        alert("결제 정보 저장 중 오류가 발생했습니다.");
				        console.error(xhr.responseText);
				    }
				});
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