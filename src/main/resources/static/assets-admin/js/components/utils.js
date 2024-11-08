function formatCurrency(number) {
    number = Math.round(number);
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}