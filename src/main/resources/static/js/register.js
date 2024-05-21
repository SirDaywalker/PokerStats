import {setErrorNotification} from "./components/notifications.js";
import {calculatePasswordStrength} from "./components/security.js";
import {sendDataToServer} from "./components/networking.js";
import {setProfilePicture} from "./components/utils.js";

document.getElementById('image-input').addEventListener('change', function() {
    setProfilePicture('image-preview', this);
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
    const picture = document.getElementById('image-input').files[0];
    const confirmPassword = document.getElementById('password-confirm').value;
    const email = document.getElementById('email').value;

    if (password.value !== confirmPassword) {
        const errorElement = document.getElementsByClassName('notification')[0];
        errorElement.innerHTML = 'Passwörter stimmen nicht überein.';
        errorElement.classList.add('red');
        return;
    }

    if (picture === undefined) {
        setErrorNotification('Bitte wählen Sie ein Bild aus.', 0);
        return;
    }

    const formData = new FormData();
    formData.append('name', name);
    formData.append('password', password.value);
    formData.append('picture', picture);
    formData.append('email', email);

    sendDataToServer(
        formData,
        '/api/v1/auth/register',
        'POST',
        null,
        function(text, status, isOK) {
            if (isOK) {
                window.location.href = '/home';
                return;
            }
            setErrorNotification(text, 0);
        }
    )
});
