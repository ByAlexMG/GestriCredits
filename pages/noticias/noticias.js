document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('.sidenav');
    M.Sidenav.init(elems, {});
    var modals = document.querySelectorAll('.modal');
    M.Modal.init(modals, {});

    // Lógica de búsqueda
    const searchInput = document.getElementById('search');
    const searchBtn = document.getElementById('search-btn');
    const resetBtn = document.getElementById('reset-btn');
    const cards = document.querySelectorAll('.card');

    searchBtn.addEventListener('click', function() {
        const query = searchInput.value.toLowerCase();
        cards.forEach(card => {
            const title = card.querySelector('.card-content h5').textContent.toLowerCase();
            if (title.includes(query)) {
                card.parentElement.style.display = 'block';
            } else {
                card.parentElement.style.display = 'none';
            }
        });
    });

    resetBtn.addEventListener('click', function() {
        searchInput.value = '';
        cards.forEach(card => {
            card.parentElement.style.display = 'block';
        });
    });
});
