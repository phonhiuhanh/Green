function formatCurrency(amount) {
    return amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".") + "₫";
}
function updateEntityCounts() {
    $.ajax({
        url: '/api/statistics/entity-counts',
        method: 'GET',
        success: function (data) {
            if (data) {
                // Update Total Products
                $(".counter.text-danger").text(data.ProductCount);

                // Update Total Customers
                $(".counter.text-megna").text(data.CustomerCount);

                // Update Total Employees
                $(".counter.text-primary").text(data.UserCount);
            }
        },
        error: function (error) {
            console.error('Error fetching entity counts:', error);
        }
    });
}

function updateFinancialStatisticsChart() {
    $.ajax({
        url: '/api/statistics/financial-stats',
        method: 'GET',
        success: function (data) {
            const chartData = data.map(item => ({
                year: item.Year.toString(),
                TotalOrderAmount: Math.round(item.TotalOrderAmount) || 0,
                TotalDailyWage: Math.round(item.TotalDailyWage) || 0,
                TotalImportAmount: Math.round(item.TotalImportAmount) || 0
            }));

            Morris.Area({
                element: 'morris-area-chart2',
                data: chartData,
                xkey: 'year',
                ykeys: ['TotalOrderAmount', 'TotalDailyWage', 'TotalImportAmount'],
                labels: ['Tổng thu đơn hàng', 'Phí nhân sự', 'Phí nhập hàng'],
                pointSize: 0,
                fillOpacity: 0.7,
                pointStrokeColors: ['rgba(203,174,174,0.95)', 'rgba(180,203,174,0.95)', 'rgba(174,191,203,0.95)'],
                behaveLikeLine: true,
                gridLineColor: '#e0e0e0',
                lineWidth: 0,
                smooth: false,
                hideHover: 'auto',
                lineColors: ['rgba(203,174,174,0.95)', 'rgba(180,203,174,0.95)', 'rgba(174,191,203,0.95)'],
                resize: true
            });
        },
        error: function (error) {
            console.error('Error fetching financial statistics:', error);
        }
    });
}

$(document).ready(function() {
    updateEntityCounts();
    updateFinancialStatisticsChart();

    // setInterval(updateFinancialStatisticsChart, 60000);
});

$(".counter").counterUp({
    delay: 100,
    time: 1200
});
