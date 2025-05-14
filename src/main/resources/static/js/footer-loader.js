document.addEventListener('DOMContentLoaded', () => {
    const footerContainer = document.createElement('div');
    footerContainer.id = 'footercContainer';
    document.body.appendChild(footerContainer);

    fetch('/partials/footer.html')
        .then(response => response.text())
        .then(data => {
            footerContainer.innerHTML = data;
        })
        .catch(error => {
            console.error('Error al cargar el footer:', error);
        });
});
