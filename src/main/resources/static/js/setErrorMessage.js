export function setErrorMessage(message) {
    const errorElement = document.getElementsByClassName("message")[0];
    let styleModified = false;

    if (errorElement.classList.contains('red')) {
        errorElement.style.display = 'block';
        styleModified = true;
        errorElement.classList.remove('red');
    }
    setTimeout(function() {
        errorElement.classList.add('red');
        if (styleModified) {
            errorElement.style.display = '';
        }
    }, 10);
    errorElement.innerHTML = message;
}