import {calculatePasswordStrength} from "./calculatePasswordStrength.js";
import {setErrorMessage} from "./setErrorMessage.js";

document.getElementById('profile-image-selector').addEventListener('change', function() {
    const file = this.files[0];
    const reader = new FileReader();
    reader.onload = function(e) {
        document.getElementById('profile-image').src = e.target.result;
    };
    reader.readAsDataURL(file);
});

document.getElementById('new-password').addEventListener('input', function() {
    let percentage = calculatePasswordStrength(this.value);
    document.getElementById('bar').style.width = (100 * percentage) + '%';
    document.getElementById('bar').style.backgroundColor = 'hsl(' + (percentage * 180) + ', 100%, 50%)';
});

document.getElementById('account-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const password = document.getElementById('password').children[0].value;
    const newPassword = document.getElementById('new-password').value;
    let picture = document.getElementById('profile-image-selector').files[0];
    const name = document.getElementById('name').value;
    const buyIn = document.getElementById('buy-in').children[0].value;

    const formData = new FormData();
    formData.append('name', name);
    formData.append('password', password);

    if (newPassword !== '') {
        formData.append('newPassword', newPassword);
    }
    formData.append('buyIn', buyIn);

    if (typeof picture !== 'undefined') {
        formData.append('picture', picture);
    }

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/v1/auth/change-details', true);
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
});
