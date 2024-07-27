document.addEventListener('DOMContentLoaded', function() {
    // Inicializar los modales
    var elemsModal = document.querySelectorAll('.modal');
    M.Modal.init(elemsModal);

    // Inicializar el carrusel
    var elemsCarousel = document.querySelectorAll('.carousel');
    M.Carousel.init(elemsCarousel, { fullWidth: true });

    // Inicializar el menú lateral
    var elemsSidenav = document.querySelectorAll('.sidenav');
    M.Sidenav.init(elemsSidenav);

    // Inicializar el formulario de búsqueda
    document.getElementById('search-form').addEventListener('submit', function(event) {
        event.preventDefault();
        filterProperties();
    });

    // Inicializar el botón de restablecer
    document.getElementById('reset-button').addEventListener('click', function() {
        document.getElementById('search-form').reset();
        filterProperties();
    });
});

// Función para abrir el modal correspondiente
function openModal(modalId) {
    var modalElem = document.getElementById('modal' + modalId);
    if (modalElem) {
        var instance = M.Modal.getInstance(modalElem);
        if (instance) {
            instance.open();
            // Re-inicializar el carrusel cuando se abre el modal
            M.Carousel.init(modalElem.querySelectorAll('.carousel'), { fullWidth: true });
        } else {
            M.Modal.init(modalElem).open();
            M.Carousel.init(modalElem.querySelectorAll('.carousel'), { fullWidth: true });
        }
    } else {
        console.error('Modal with ID ' + modalId + ' does not exist.');
    }
}

// Función para filtrar propiedades
function filterProperties() {
    var ubicacion = document.getElementById('ubicacion').value.toLowerCase();
    var precioMin = parseInt(document.getElementById('precio-min').value) || 0;
    var precioMax = parseInt(document.getElementById('precio-max').value) || Infinity;

    var cards = document.querySelectorAll('.card-item');
    cards.forEach(function(card) {
        var cardUbicacion = card.getAttribute('data-ubicacion').toLowerCase();
        var cardPrecio = parseInt(card.getAttribute('data-precio'));

        if (cardUbicacion.includes(ubicacion) && cardPrecio >= precioMin && cardPrecio <= precioMax) {
            card.style.display = '';
        } else {
            card.style.display = 'none';
        }
    });
}
