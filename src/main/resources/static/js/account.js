import {calculatePasswordStrength} from "./calculatePasswordStrength.js";
import {setErrorMessage} from "./setErrorMessage.js";

document.getElementById('new-password').addEventListener('input', function() {
    let percentage = calculatePasswordStrength(this.value);
    document.getElementById('bar').style.width = (100 * percentage) + '%';
    document.getElementById('bar').style.backgroundColor = 'hsl(' + (percentage * 180) + ', 100%, 50%)';
});

document.getElementById('account-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const password = document.getElementById('password').value;
    const newPassword = document.getElementById('new-password').value;
    const picture = document.getElementById('profile-image-selector').files[0];
    const name = document.getElementById('name').value;
    const buyIn = document.getElementById('buy-in').value;

    const formData = new FormData();
    formData.append('password', password);
    formData.append('newPassword', newPassword);

    if (typeof picture !== 'undefined' && picture !== null) {
        formData.append('picture', picture);
    }
    if (typeof picture !== 'undefined') {
        formData.append('picture', null);
    }
    formData.append('name', name);
    formData.append('buyIn', buyIn);

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/v1/auth/change-details', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
        if (xhr.status === 200) {
            window.location.href = '/home';
            return;
        }
        if (xhr.status === 400) {
            setErrorMessage('Ein Fehler ist aufgetreten. Sind die Daten gültig?');
        }
        else if (xhr.status === 413) {
            setErrorMessage('Das Bild ist zu groß.');
        }
        else if (xhr.status === 401) {
            setErrorMessage('Das Passwort ist falsch.');
        }
        else {
            setErrorMessage('Status ' + xhr.status.toString());
        }
    };
    xhr.send(formData);
    // Give out all entries in the console
    for (var pair of formData.entries()) {
        console.log(pair[0]+ ', ' + pair[1]);
    }
});
