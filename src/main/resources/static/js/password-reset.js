import {calculatePasswordStrength} from "./components/security.js";
import {sendDataToServer} from "./components/networking.js";
import {setErrorNotification} from "./components/notifications.js";

const form = document.getElementsByTagName('form')[0];
const password = document.getElementById('password');

password.addEventListener('input', function() {
    let percentage = calculatePasswordStrength(this.value);
    document.getElementById('bar').style.width = (100 * percentage) + '%';
    document.getElementById('bar').style.backgroundColor = 'hsl(' + (percentage * 180) + ', 100%, 50%)';
});

form.addEventListener('submit', function(event) {
    event.preventDefault();

    const confirmPassword = document.getElementById('password-confirm').value;

    if (password.value !== confirmPassword) {
        const errorElement = document.getElementsByClassName('notification')[0];
        errorElement.innerHTML = 'Passwörter stimmen nicht überein.';
        errorElement.classList.add('red');
        return;
    }

    const data = {
        password: confirmPassword
    };
    const url = '/password-reset?confirmation='+document.getElementById('confirmation').getAttribute('data-token');

    sendDataToServer(
        JSON.stringify(data),
        url,
        'POST',
        'application/json',
        function(text, status, isOK) {
            if (isOK) {
                window.location.href = '/login';
                return;
            }
            setErrorNotification(text, 0);
        }
    )
});
