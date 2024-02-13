export function setErrorMessage(message) {
    const errorElement = document.getElementsByClassName("message")[0];
    if (errorElement.classList.contains('red')) {
        errorElement.classList.remove('red');
    }
    errorElement.classList.add('red');
    errorElement.innerHTML = message;
}