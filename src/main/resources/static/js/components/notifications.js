export function setErrorNotification(message, index) {
    let elements = document.getElementsByClassName("notification");

    if ( index !== -1) {
        elements = [elements[index]];
    }

    for (const element of elements) {
        let styleModified = false;

        if (element.classList.length > 1) {
            element.style.display = 'block';
            styleModified = true;

            element.classList.remove(...element.classList);
            element.classList.add('notification');
        }
        setTimeout(() => {
            element.classList.add('red');
            if (styleModified) {
                element.style.display = '';
            }
        }, 10);
        element.innerHTML = message;
    }
}

export function setDefaultNotification(message, index) {
    let elements = document.getElementsByClassName("notification");

    if ( index !== -1) {
        elements = [elements[index]];
    }

    for (const element of elements) {
        let styleModified = false;

        if (element.classList.length > 1) {
            element.style.display = 'block';
            styleModified = true;

            element.classList.remove(...element.classList);
            element.classList.add('notification');
        }
        setTimeout(() => {
            element.classList.add('blue');
            if (styleModified) {
                element.style.display = '';
            }
        }, 10);
        element.innerHTML = message;
    }
}