/**
 * This function sets the error notification message to a notification element.
 * @param message - The message to be displayed
 * @param index - The index of the notification element. If -1, all elements will be affected.
 * @returns {void}
 */
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

/**
 * This function sets the default notification message to a notification element.
 * @param message - The message to be displayed
 * @param index - The index of the notification element. If -1, all elements will be affected.
 * @returns {void}
 */
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