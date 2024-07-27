document.addEventListener('DOMContentLoaded', function() {
    var elemsSidenav = document.querySelectorAll('.sidenav');
    M.Sidenav.init(elemsSidenav, {});

    var elemsCarousel = document.querySelectorAll('.carousel');
    M.Carousel.init(elemsCarousel, {
        fullWidth: true,
        indicators: true
    });

    // Manejo de las flechas de navegación
    var carouselInstance = M.Carousel.getInstance(document.querySelector('.carousel'));
    document.querySelector('.carousel-navigation .left').addEventListener('click', function() {
        carouselInstance.prev();
    });
    document.querySelector('.carousel-navigation .right').addEventListener('click', function() {
        carouselInstance.next();
    });
});