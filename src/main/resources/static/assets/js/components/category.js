const setCategoryName = async (targetId, categoryId) => {
    const response = await axios.get(`/api/categories/${categoryId}`);
    $(`#${targetId}`).text(response.data.name.toUpperCase());
}

// Hàm kiểm tra và cập nhật danh sách danh mục
async function loadCategories() {
    // Lấy phần tử ul với class category-menu
    const categoryMenu = document.querySelector('ul.category-menu');

    // Kiểm tra nếu ul.category-menu có nhiều hơn 1 phần tử con thì trả về
    if (categoryMenu.children.length > 1) {
        return;
    }

    // Gọi API để lấy danh sách danh mục
    await fetch('/api/categories')
        .then(response => {
            if (!response.ok) {
                throw new Error('Lỗi khi gọi API');
            }
            return response.json();
        })
        .then(data => {
            // Duyệt qua danh sách danh mục và thêm vào ul.category-menu
            data.forEach(category => {
                const li = document.createElement('li');
                const a = document.createElement('a');
                a.href = `/index/search?categoryID=${category.categoryID}`;
                a.textContent = category.name.toUpperCase();
                li.appendChild(a);
                categoryMenu.appendChild(li);
            });
        })
        .catch(error => {
            // Hiển thị thông báo lỗi nếu gọi API thất bại
            Swal.fire({
                title: 'Lỗi',
                text: `Lỗi: ${error.message}`,
                icon: 'error',
                confirmButtonText: 'OK'
            });
        });
}

$(document).ready(async () => {
    await loadCategories();
});