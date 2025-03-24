// Ajax를 사용하여 부서/직급/권한 데이터를 동적으로 로드
function loadData(url, selectId, empv) {
    fetch(url)
        .then(response => response.json())
        .then(data => {
            let select = document.getElementById(selectId);
            data.forEach(item => {
                let option = document.createElement("option");
                option.value = item.departmentNo || item.rankId || item.rightsId;
                option.textContent = item.departmentName || item.jobTitleName || item.rightsName;
				if(option.value == empv){
					option.selected=true;
				}
                select.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading data:", error));
}