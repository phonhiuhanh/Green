const setCategoryName = async (targetId, categoryId) => {
    const response = await axios.get(`/api/categories/${categoryId}`);
    $(`#${targetId}`).text(response.data.name.toUpperCase());
}