function formatCurrency(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

// Load top 10 products
const loadTop10Products = async (containerId, endpoint, requestParams) => {
    $(`#${containerId}`).html('Đang tải...');
    await axios.get(endpoint, {
        params: requestParams || {
            page: 0,
            size: 10
        }
    })
    .then(res => {
        let products = res.data;
        let content = '';
        products.forEach(product => {
            let mainImage = product.images.find(image => image.isMain) || {imageURL: '/assets/img/product/discount/default.jpg'};
            content += `<div class="">
                        <div class="product__discount__item">
                            <div class="product__discount__item__pic set-bg" onclick="navigateToProductDetails(${product.productID})"
                                 data-setbg="${mainImage.imageURL}">
                            </div>
                            <div class="product__item__text">
                                <h6 onclick="navigateToProductDetails(${product.productID})">${product.name}</h6>
                                <div class="price">
                                    <span class="current-price">${formatCurrency(product.price)}đ</span>
                                    ${product.giamgia !== 0 ? `
                                    <span class="old-price">${formatCurrency(product.giacu)}đ</span>
                                    <span class="discount">-${product.giamgia}%</span>                                        
                                    ` : ``}
                                </div>
                                <button 
                                onclick="addToCart(${product.productID}, 1, ${product.price}, '${product.name.trim()}', '${mainImage.imageURL}')" 
                                class="button-buy">MUA</button>
                            </div>
                        </div>
                    </div>`;
        });
        $(`#${containerId}`).html(content);

        $('.set-bg').each(function () {
            var bg = $(this).data('setbg');
            $(this).css('background-image', 'url(' + bg + ')');
        });

        $(`#${containerId}`).owlCarousel('destroy');
        $(`#${containerId}`).owlCarousel({
            loop: true,
            margin: 10,
            nav: true,
            items: 4,
            autoplay: true,
            autoplayTimeout: 3000,
            autoplayHoverPause: true
        });
    })
    .catch(err => {
        Swal.fire({
            title: 'Lỗi',
            text: 'Không thể tải sản phẩm. Vui lòng th�� lại sau.',
            icon: 'error',
            confirmButtonText: 'OK'
        });
    });
}

// Load search results
const loadSearchResults = async (containerId, endpoint, requestParams) => {
    $(`#${containerId}`).html('Đang tải...');
    await axios.get(endpoint, {
        params: requestParams
    })
    .then(res => {
        let products = res.data;
        let content = '';
        products.forEach(product => {
            let mainImage = product.images.find(image => image.isMain) || {imageURL: '/assets/img/product/discount/default.jpg'};
            content += `<div class="col">
                            <div class="product__discount__item">
                                <div class="product__discount__item__pic d-flex" onclick="navigateToProductDetails(${product.productID})">
                                     <img src="${mainImage.imageURL}" alt="" class="">
                                </div>
                                <div class="product__item__text">
                                    <h6 onclick="navigateToProductDetails(${product.productID})">${product.name}</h6>
                                    <div class="price">
                                        <span class="current-price">${formatCurrency(product.price)}đ</span>
                                        ${product.giamgia !== 0 ? `
                                        <span class="old-price">${formatCurrency(product.giacu)}đ</span>
                                        <span class="discount">-${product.giamgia}%</span>                                        
                                        ` : ``}
                                    </div>
                                    <button 
                                    onclick="addToCart(${product.productID}, 1, ${product.price}, '${product.name.trim()}', '${mainImage.imageURL}')" 
                                    class="button-buy">MUA</button>
                                </div>
                            </div>
                        </div>`;
        });
        $(`#${containerId}`).html(content);
    })
    .catch(err => {
        Swal.fire({
            title: 'Lỗi',
            text: 'Không thể tải kết quả tìm kiếm. Vui lòng thử lại sau.',
            icon: 'error',
            confirmButtonText: 'OK'
        });
    });
}

// Initialize search input
function initializeSearchInput() {
    const searchInput = document.getElementById('search-input');
    searchInput.addEventListener('keypress', function(event) {
        event.preventDefault();
        if (event.key === 'Enter') {
            searchProducts();
        }
    });
}

// Search products
const searchProducts = () => {
    const searchInput = document.getElementById('search-input');
    const searchValue = searchInput.value;
    if (searchValue.trim() === '') {
        Swal.fire({
            title: 'Lỗi',
            text: 'Vui lòng nhập từ khóa tìm kiếm.',
            icon: 'error',
            confirmButtonText: 'OK'
        });
        return;
    }
    const urlParams = new URLSearchParams(window.location.search);
    const categoryID = urlParams.get('categoryID') ?? ``;
    window.location.href = `/index/search?keyword=${searchValue}&categoryID=${categoryID}`;
}

// Add item to cart
const addToCart = (productID, quantity, price, name, thumbnail) => {
    let item = {
        productID: productID,
        quantity: quantity,
        price: price,
        name: name,
        thumbnail: thumbnail
    };
    addItemToCart(item);
    Swal.fire({
        title: 'Thành công',
        text: 'Sản phẩm đã được thêm vào giỏ hàng.',
        icon: 'success',
        confirmButtonText: 'OK'
    }).then(() => {
        window.location.href = '/index/order';
    });
}

// Navigate to product details
const navigateToProductDetails = (productID) => {
    window.location.href = `/index/details?id=${productID}`;
}

// Countdown timer for flash sale
let countdownTime = 2 * 60 * 60;

function startCountdown() {
    const timerElement = document.getElementById('flash-sale-timer');

    const interval = setInterval(() => {
        let hours = Math.floor(countdownTime / 3600);
        let minutes = Math.floor((countdownTime % 3600) / 60);
        let seconds = countdownTime % 60;

        hours = hours < 10 ? '0' + hours : hours;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        seconds = seconds < 10 ? '0' + seconds : seconds;

        timerElement.innerHTML = `${hours}:${minutes}:${seconds}`;

        if (countdownTime <= 0) {
            clearInterval(interval);
            timerElement.innerHTML = '00:00:00';
        } else {
            countdownTime--;
        }
    }, 1000);
}

// Toggle category menu
document.querySelectorAll('.category-toggle').forEach(item => {
    item.addEventListener('click', event => {
        event.preventDefault();
        const parentLi = item.parentElement;

        document.querySelectorAll('.category-menu > li').forEach(li => {
            if (li !== parentLi) {
                li.classList.remove('active');
            }
        });

        parentLi.classList.toggle('active');
    });
});

// Initialize search input on window load
window.onload = () => {
    initializeSearchInput();
}