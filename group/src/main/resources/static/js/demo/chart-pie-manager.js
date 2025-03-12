// Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = 'Nunito', '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#858796';

// Pie Chart Example
var ctx = document.getElementById("myPieChart");
var myPieChart = new Chart(ctx, {
  type: 'pie',
  data: {
    labels: ["정상", "지각", "연차","반차","병가","결근"],
    datasets: [{
      data: [10, 10, 10, 10, 10, 10 ],
      backgroundColor: ['#4e73df', '#1cc88a', '#36b9cc', '#37b9cc', '#46b9cc', '#47b9cc'],
      hoverBackgroundColor: ['#2e59d9', '#17a673', '#2c9faf', '#2e48d9', '#17a623', '#2c7caf'],
      hoverBorderColor: "rgba(234, 236, 244, 1)",
    }],
  },
  options: {
    maintainAspectRatio: false,	
	animation: {
	   duration: 0, // 애니메이션을 없애면 바로 나타남
	 },
    tooltips: {
      backgroundColor: "rgb(255,255,255)",
      bodyFontColor: "#858796",
      borderColor: '#dddfeb',
      borderWidth: 1,
      xPadding: 15,
      yPadding: 15,
      displayColors: false,
      caretPadding: 10,
    },
    legend: {
      display: false
    },
    cutoutPercentage: 50,
  },
});
