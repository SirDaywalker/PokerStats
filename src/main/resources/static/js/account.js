import {calculatePasswordStrength} from "./components/security.js";
import {setErrorNotification} from "./components/notifications.js";
import {sendDataToServer} from "./components/networking.js";
import {setProfilePicture} from "./components/utils.js";

const imageInputElement = document.getElementById('image-input');
const newPasswordElement = document.getElementById('new-password');
const form = document.getElementById('account-form');
const nameElement = document.getElementById('name');

imageInputElement.addEventListener('change', function() {
    setProfilePicture('image-preview', this);
});

newPasswordElement.addEventListener('input', function() {
    let percentage = calculatePasswordStrength(this.value);
    document.getElementById('bar').style.width = (100 * percentage) + '%';
    document.getElementById('bar').style.backgroundColor = 'hsl(' + (percentage * 180) + ', 100%, 50%)';
});

form.addEventListener('submit', function(event) {
    event.preventDefault();

    const formData = new FormData();
    formData.append('name', nameElement.value);
    formData.append('password', document.getElementById('password').children[0].value);

    const newPassword = newPasswordElement.value;
    if (newPassword !== '') {
        formData.append('newPassword', newPassword);
    }

    const picture = imageInputElement.files[0];
    if (typeof picture !== 'undefined') {
        formData.append('picture', picture);
    }

    formData.append('buyIn', document.getElementById('buy-in').children[0].value);
    formData.append('email', document.getElementById('email').value);
    formData.append('targetId', nameElement.getAttribute('data-target-id'));

    let role = null;
    if (document.getElementById('role') != null) {
        const e = document.getElementById('role');
        role = e.options[e.selectedIndex].value;
    }
    if (role !== null) {
        formData.append('role', role);
    }

    sendDataToServer(
        formData,
        '/api/v1/auth/change-details',
        'POST',
        null,
        function(response, status, isOK) {
            if (isOK) {
                window.location.href = '/home';
            } else {
                setErrorNotification(response, 0);
            }
        }
    );
});