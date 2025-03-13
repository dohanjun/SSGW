	let count = document.querySelector('.countInput').value;
	
	document.querySelectorAll('.selectModule .selectmd').forEach(e=>{
		e.addEventListener('click',f=>{
			f.target.classList.toggle('check')
		})
	})
	
	document.querySelector('.plusButton').addEventListener('click',f=>{
		event.preventDefault();
		if(count < 100){
			count = Number(count) + 10
			document.querySelector('.countInput').value = count
		}
	})
	
	document.querySelector('.minusButton').addEventListener('click',f=>{
		event.preventDefault();
		if(count > 10){
			count = Number(count) - 10
			document.querySelector('.countInput').value = count
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
	        }
	    });
	}


setupDropdown('.uploadInput', '.uploadDropdown', '.uploadOption');
setupDropdown('.storageInput', '.storageDropdown', '.storageOption');
setupDropdown('.periodInput', '.periodDropdown', '.periodOption');
setupDropdown('.paymentInput', '.paymentDropdown', '.paymentOption');