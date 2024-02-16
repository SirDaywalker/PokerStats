import {setErrorMessage} from "./setErrorMessage.js";
import {calculatePasswordStrength} from "./calculatePasswordStrength.js";

document.getElementById('profile-image-selector').addEventListener('change', function() {
    let reader = new FileReader();
    reader.onload = function(e) {
        document.getElementById('image-preview').style.display = 'block';
        document.getElementById('image-preview').src = e.target.result;
        document.getElementById('img-example').style.display = 'none';
    };
    reader.readAsDataURL(this.files[0]);
});

const form = document.getElementsByTagName('form')[0];
const password = document.getElementById('password');

password.addEventListener('input', function() {
    let percentage = calculatePasswordStrength(this.value);
    document.getElementById('bar').style.width = (100 * percentage) + '%';
    document.getElementById('bar').style.backgroundColor = 'hsl(' + (percentage * 180) + ', 100%, 50%)';
});

form.addEventListener('submit', function(event) {
    event.preventDefault();

    const name = document.getElementById('name').value;
    const picture = document.getElementById('profile-image-selector').files[0];
    const confirmPassword = document.getElementById('password-confirm').value;
    const email = document.getElementById('email').value;

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
    formData.append('email', email);

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
