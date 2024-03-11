import {setDefaultNotification, setErrorNotification} from "./components/notifications.js";
import {sendDataToServer} from "./components/networking.js";

const loginForm = document.getElementById('login-form');
const formerUrl = document.referrer;

if (performance.navigation.type == performance.navigation.TYPE_RELOAD && formerUrl !== "http://localhost:8080/login") {
  	window.location.href = '/login';
}
else if(formerUrl === "http://localhost:8080/request-confirmation" || formerUrl === "http://localhost:8080/register") {
	setDefaultNotification("Bestätigungsmail wurde gesendet!", 0);
}
else if(formerUrl === "http://localhost:8080/request-password-reset") {
	setDefaultNotification("Email zur Passwortzurücksetzung wurde gesendet!", 0);
}
else if(formerUrl.split("?")[0] === "http://localhost:8080/password-reset-form") {
    setDefaultNotification("Passwort wurde erfolgreich geändert.", 0);
}

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
                setErrorNotification(response, 0);
            }
        }
    );
});
