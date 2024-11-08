const init = () => {
    const currentUrl = window.location.href;

    // Kiểm tra nếu URL chứa "/details/product?id="
    if (currentUrl.includes("/details?id=")) {
        const urlParams = new URLSearchParams(window.location.search);
        const productId = urlParams.get('id');
        loadProductDetails(productId);
    } else {
        Swal.fire({
            title: 'Lỗi',
            text: 'URL không chứa thông tin sản phẩm.',
            icon: 'error',
            confirmButtonText: 'OK'
        });
    }
}

const idMap = {
    productImages: 'product-images',
    productName: 'product-name',
    productPrice: 'product-price',
    productDescription: 'product-description',
    relevantProducts: 'relevant-products'
}

let currentProduct = {};

const loadProductDetails = async (productId) => {
    const response = await axios.get(`/api/products/${productId}`)
        .catch(err => {
            Swal.fire({
                title: 'Lỗi',
                text: 'Không thể tải thông tin sản phẩm.',
                icon: 'error',
                confirmButtonText: 'OK'
            });
        });
    const product = response.data;
    currentProduct = product;
    let imageContent = ``;
    product.images.forEach(image => {
        imageContent += `<div class="product__details__pic__item">
                            <img class="product__details__pic__item__pic" src="${image.imageURL}" alt="${product.name}">
                        </div>`;
    });
    $(`#${idMap.productImages}`).html(imageContent);

    $(`#${idMap.productImages}`).owlCarousel('destroy');
    $(`#${idMap.productImages}`).owlCarousel({
        loop: true,
        margin: 10,
        nav: true,
        items: 1,
        autoplay: true,
        autoplayTimeout: 2500,
        autoplayHoverPause: true
    });

    $(`#${idMap.productName}`).text(product.name);
    $(`#${idMap.productPrice}`).html(`
        ${formatCurrency(product.price)}₫
        ${product.giamgia !== 0 ? `
        <span class="old-price">${formatCurrency(product.giacu)}đ</span>
        <span class="discount">-${product.giamgia}%</span>                                        
        ` : ``}        
    `);
    $(`.${idMap.productDescription}`).text(product.description);

    await loadTop10Products(idMap.relevantProducts, '/api/products/top10-by-category', {categoryID: product.category.categoryID});
}

const addToCartFromDetailPage = () => {
    let item = {
        productID: currentProduct.productID,
        quantity: parseInt($(`#product-quantity`).val()),
        price: currentProduct.price,
        name: currentProduct.name,
        thumbnail: currentProduct.images.find(image => image.isMain).imageURL
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