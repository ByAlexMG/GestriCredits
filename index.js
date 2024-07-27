document.addEventListener('DOMContentLoaded', function() {
    var elemsSidenav = document.querySelectorAll('.sidenav');
    M.Sidenav.init(elemsSidenav, {});
    
    var elemsCarousel = document.querySelectorAll('.carousel');
    var carousels = M.Carousel.init(elemsCarousel, {
        duration: 200,
        dist: -80,
        shift: 5,
        padding: 5,
        numVisible: 6,
        indicators: false
    });
    
    setInterval(function() {
        carousels.forEach(function(instance) {
            instance.next();
        });
    }, 3500);
});

function scrollDown() {
    window.scrollTo({
        top: window.innerHeight,
        behavior: 'smooth'
    });
}