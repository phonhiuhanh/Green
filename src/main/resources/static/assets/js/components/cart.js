// Helper function to get cart items from localStorage
const getCartItems = () => {
    return JSON.parse(localStorage.getItem('cartItems')) || [];
}

// Helper function to save cart items to localStorage
const saveCartItems = (items) => {
    localStorage.setItem('cartItems', JSON.stringify(items));
    window.dispatchEvent(new Event('cartItemsUpdated'));
}

// Add item to cart
const addItemToCart = (product) => {
    let cartItems = getCartItems();
    const existingItem = cartItems.find(item => item.productID === product.productID);

    if (existingItem) {
        existingItem.quantity += product.quantity;
    } else {
        cartItems.push(product);
    }

    saveCartItems(cartItems);
}

// Remove item from cart
const removeItemFromCart = (productID) => {
    confirm('Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng?') && (() => {
        let cartItems = getCartItems();
        cartItems = cartItems.filter(item => item.productID !== productID);
        saveCartItems(cartItems);
    })();
}

const setQuantityAdjustmentButtons = () => {
    $('.pro-qty').prepend('<span class="dec qtybtn">-</span>');
    $('.pro-qty').append('<span class="inc qtybtn">+</span>');
    $('.pro-qty').on('click', '.qtybtn', function () {
        var $button = $(this);
        var oldValue = $button.parent().find('input').val();
        if ($button.hasClass('inc')) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            // Don't allow decrementing below 1
            if (oldValue > 1) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 1;
            }
        }
        $button.parent().find('input').val(newVal);

        // Check if the parent has the class `product-${productID}`
        $button.parent()[0].classList.forEach((className) => {
            if (className.startsWith('product-')) {
                const productID = className.split('-')[1];
                // console.log(productID, newVal);
                updateItemQuantity(productID, newVal);
            }
        });
    });
}

// Update item quantity in cart
const updateItemQuantity = (productID, quantity) => {
    let cartItems = getCartItems();
    const item = cartItems.find(item => item.productID === parseInt(productID));

    // console.log(cartItems, item);
    if (item) {
        item.quantity = quantity;
        if (item.quantity <= 0) {
            removeItemFromCart(productID);
        } else {
            saveCartItems(cartItems);
        }
    }
}

// Load cart items from localStorage
const loadCartItems = () => {
    return getCartItems();
}

// Function to load cart items and append them to the cart body
const loadCartItemsIntoTable = () => {
    const cartItems = getCartItems();
    const cartBody = document.getElementById('cart-body');
    cartBody.innerHTML = ''; // Clear existing content
    let cartContent = ``;

    cartItems.forEach(item => {
        const total = formatCurrency(item.price * item.quantity);
        const row = `
            <tr>
                <td class="shoping__cart__item d-flex flex-row align-items-center">
                    <a href="/index/details?id=${item.productID}"><img src="${item.thumbnail}" alt="${item.name}" class=""></a>
                    <h5 class="too-long-text">${item.name}</h5>
                </td>
                <td class="shoping__cart__price">
                    ${formatCurrency(item.price)}₫
                </td>
                <td class="shoping__cart__quantity">
                    <div class="quantity">
                        <div class="pro-qty product-${item.productID}">
                            <input type="text" class="quantity-input" disabled value="${item.quantity}">
                        </div>
                    </div>
                </td>
                <td class="shoping__cart__total">
                    ${total}₫
                </td>
                <td class="shoping__cart__item__close">
                    <span class="icon_close" onclick="removeItemFromCart(${item.productID})"></span>
                </td>
            </tr>
        `;
        cartContent += row;
        // cartBody.insertAdjacentHTML('beforeend', row);
    });
    $('#cart-body').html(cartContent);

}

const calc = (cartItems) => {
    let subtotal = 0;
    let shippingFee = cartItems.length === 0 ? 0 : 30000; // Example shipping fee
    let total = 0;

    cartItems.forEach(item => {
        subtotal += item.price * item.quantity;
    });

    shippingFee = subtotal / 10 < shippingFee ? subtotal / 10 : shippingFee;
    total = subtotal + shippingFee;
    return {subtotal, shippingFee, total};
}

const updateCartSummary = () => {
    const cartItems = getCartItems();
    const {subtotal, shippingFee, total} = calc(cartItems);
    // let subtotal = 0;
    // let shippingFee = cartItems.length === 0 ? 0 : 30000; // Example shipping fee
    // let total = 0;
    //
    // cartItems.forEach(item => {
    //     subtotal += item.price * item.quantity;
    // });
    //
    // shippingFee = subtotal / 10 < shippingFee ? subtotal / 10 : shippingFee;
    // total = subtotal + shippingFee;

    // Update the DOM elements
    document.querySelector('.checkout__order__subtotal span').textContent = formatCurrency(subtotal) + '₫';
    document.querySelector('.checkout__order__total span').textContent = formatCurrency(total) + '₫';
    $('.shipping-fee').text(formatCurrency(shippingFee) + '₫');
}

const checkOut = async () => {
    const cartItems = getCartItems();
    if (cartItems.length === 0) {
        alert('Giỏ hàng của bạn đang trống!');
        return;
    }
    confirm('Bạn có chắc chắn muốn tiến hành đặt hàng?') &&
    (() => {
        createOrder(cartItems);
    })();
}

const createOrder = async (cartItems) => {
    const {subtotal, shippingFee, total} = calc(cartItems);

    const order = {
        totalAmount: total,
        orderDetails: cartItems.map(item => ({
            productID: item.productID,
            quantity: item.quantity,
            price: item.price
        }))
    };

    try {
        const response = await fetch('/api/orders', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(order)
        });
        if (response.status === 200) {
            alert('Đặt hàng thành công!');
            saveCartItems([]);
            loadCartItemsIntoTable();
            updateCartSummary();
        } else {
            alert('Đặt hàng thất bại. Vui lòng thử lại!');
        }
    } catch (error) {
        console.error('Error creating order:', error);
        alert('Đặt hàng thất bại. Vui lòng thử lại!');
    }
}

const updateCartItemCount = () => {
    const cartItems = getCartItems();
    $('.header__cart span').text(cartItems.length);
}

const loadUserInfoForCheckOut = async () => {
    await axios.get('/api/customers/get-current-customer')
        .then(response => {
            const customer = response.data;
            $('#address').text(customer.address);
            $('#username').text(customer.username);
            $('#phone').text(customer.phone);
        })
        .catch(error => {
            console.error('Error fetching customer data:', error);
        });
}

document.addEventListener('DOMContentLoaded', () => {
    updateCartItemCount();
});
// Listen for changes in localStorage
window.addEventListener('cartItemsUpdated', (event) => {
    updateCartSummary();
    loadCartItemsIntoTable();
    setQuantityAdjustmentButtons();
    updateCartItemCount();
});