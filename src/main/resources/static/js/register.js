import {setErrorNotification} from "./components/notifications.js";
import {calculatePasswordStrength} from "./components/security.js";
import {sendDataToServer} from "./components/networking.js";

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
        const errorElement = document.getElementsByClassName('notification')[0];
        errorElement.innerHTML = 'Passwörter stimmen nicht überein.';
        errorElement.classList.add('red');
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
