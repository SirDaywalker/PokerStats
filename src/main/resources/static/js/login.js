import {setDefaultNotification, setErrorNotification} from "./components/notifications.js";
import {sendDataToServer} from "./components/networking.js";

const loginForm = document.getElementById('login-form');
const formerUrl = document.referrer;

if (performance.navigation.type == performance.navigation.TYPE_RELOAD && formerUrl !== "http://localhost:8080/login") {
  	window.location.href = '/home';
}
else if(formerUrl === "http://localhost:8080/requestConfirmation") {
	setErrorMessage("Bestätigungsmail wurde gesendet!");
	if (Notification.permission === "granted") {
        new Notification("Bestätigungsmail wurde gesendet!");
    } else if (Notification.permission !== "denied") {
        Notification.requestPermission().then(function(permission) {
            if (permission === "granted") {
                new Notification("Bestätigungsmail wurde gesendet!");
            }
        });
    }
}
else if(formerUrl === "http://localhost:8080/requestPasswordReset") {
	setErrorMessage("Email zur Passwortzurücksetzung wurde gesendet!");
	if (Notification.permission === "granted") {
        new Notification("Email zur Passwortzurücksetzung wurde gesendet!");
    } else if (Notification.permission !== "denied") {
        Notification.requestPermission().then(function(permission) {
            if (permission === "granted") {
                new Notification("Email zur Passwortzurücksetzung wurde gesendet!");
            }
        });
    }
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
