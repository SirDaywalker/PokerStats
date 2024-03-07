import {setErrorMessage} from "./setErrorMessage.js";
import {sendDataToServer} from "./components/networking.js";

const loginForm = document.getElementById('login-form');

document.getElementById('confirmation').addEventListener('click', function(event) {
    window.location.href = '/newConfirmation';
})

document.getElementById('resetPassword').addEventListener('click', function(event) {
    window.location.href = '/resetPassword';
})

loginForm.addEventListener('submit', function(event) {
    event.preventDefault();
    const data = {
        name: document.getElementById('name').value,
        password: document.getElementById('password').value
    };
    sendDataToServer(
        JSON.stringify(data),
        '/api/v1/auth/authenticate',
        'POST',
        'application/json',
        function(response, status, isOK) {
            if (isOK) {
                window.location.href = '/home';
            } else {
                setErrorMessage(response);
            }
        }
    );
});
