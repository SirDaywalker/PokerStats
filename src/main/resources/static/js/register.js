import {setErrorMessage} from "./setErrorMessage.js";

document.getElementById('profile-image-selector').addEventListener('change', function() {
    let reader = new FileReader();
    reader.onload = function(e) {
        document.getElementById('image-preview').style.display = 'block';
        document.getElementById('image-preview').src = e.target.result;
    };
    reader.readAsDataURL(this.files[0]);
});

const form = document.getElementsByTagName('form')[0];
const password = document.getElementById('password');

password.addEventListener('input', function() {
    let input = this.value;
    let percentage = 0;

    percentage += Number(new RegExp(/[A-Z]/).test(input)) * 25;
    percentage += Number(new RegExp(/[a-z]/).test(input)) * 25;
    percentage += Number(new RegExp(/[0-9]/).test(input)) * 25;
    percentage += Number(new RegExp(/[^A-Za-z0-9]/).test(input)) * 25;
    percentage *= Math.min(input.length / 8, 1)

    document.getElementById('bar').style.width = percentage + '%';
});

form.addEventListener('submit', function(event) {
    event.preventDefault();

    const name = document.getElementById('name').value;
    const picture = document.getElementById('profile-image-selector').files[0];
    const confirmPassword = document.getElementById('password-confirm').value;

    if (password.value !== confirmPassword) {
        const errorElement = document.getElementsByClassName('message')[0];
        errorElement.innerHTML = 'Passwörter stimmen nicht überein.';
        errorElement.classList.add('red');
        return;
    }

    const formData = new FormData();
    formData.append('name', name);
    formData.append('password', password.value);
    formData.append('picture', picture);

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/v1/auth/register', true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            window.location.href = '/home';
            return;
        }
        if (xhr.status === 400) {
            setErrorMessage('Ein Fehler ist aufgetreten. Existiert ein Account für Sie bereits?');
        }
        else if (xhr.status === 413) {
            setErrorMessage('Das Bild ist zu groß.');
        } else {
            setErrorMessage('Status ' + xhr.status.toString());
        }
    };
    xhr.send(formData);
});
