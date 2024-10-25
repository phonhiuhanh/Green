function formatCurrency(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

const loadTop10Products = async (containerId, endpoint, requestParams) => {
    $(`#${containerId}`).html('Loading...');
    await axios.get(endpoint, {
        params: requestParams || {
            page: 0,
            size: 10
        }
    })
    .then(res => {
        let products = res.data;
        let content = '';
        console.log(products);
        products.forEach(product => {
            let mainImage = product.images.find(image => image.isMain) || { imageURL: '/assets/img/product/discount/default.jpg' };
            console.log(mainImage.imageURL);
            content += `<div class="">
                            <div class="product__discount__item">
                                <div class="product__discount__item__pic set-bg"
                                     data-setbg="${mainImage.imageURL}">
                                </div>
                                <div class="product__item__text">
                                    <h6>${product.name}</h6>
                                    <div class="price">
                                        <span class="current-price">${formatCurrency(product.price)}đ</span>
                                        ${product.giamgia !== 0 ? `
                                        <span class="old-price">${formatCurrency(product.giacu)}đ</span>
                                        <span class="discount">-${product.giamgia}%</span>                                        
                                        ` : ``}
                                    </div>
                                    <a href="/index/order" class="button-buy">MUA</a>
                                </div>
                            </div>
                        </div>`;
        });
        $(`#${containerId}`).html(content);

        $('.set-bg').each(function() {
            var bg = $(this).data('setbg');
            $(this).css('background-image', 'url(' + bg + ')');
        });

        $('.owl-carousel').owlCarousel('destroy');
        $('.owl-carousel').owlCarousel({
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
        console.log(err);
    });
}

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