import {setDefaultNotification, setErrorNotification} from "./components/notifications.js";
import {sendDataToServer} from "./components/networking.js";

const resetPasswordForm = document.getElementById('resetPassword-form');
const formerUrl = document.referrer;

if (performance.navigation.type == performance.navigation.TYPE_RELOAD && formerUrl !== "http://localhost:8080/login") {
    window.location.href = '/home';
}
else if(formerUrl === "http://localhost:8080/password-reset") {
    setDefaultNotification("Anfrage zur Passwort Zurücksetzung invalide oder abgelaufen. Bitte erneut anfordern!", 0);
    /*if (Notification.permission === "granted") {
        new Notification("Anfrage zur Passwort Zurücksetzung invalide oder abgelaufen. Bitte erneut anfordern!");
    } else if (Notification.permission !== "denied") {
        Notification.requestPermission().then(function(permission) {
            if (permission === "granted") {
                new Notification("Anfrage zur Passwort Zurücksetzung invalide oder abgelaufen. Bitte erneut anfordern!");
            }
        });
    }*/
}

resetPasswordForm.addEventListener('submit', function(event) {
	event.preventDefault();
	
	const data = {
        name: document.getElementById('name').value
    };
    
	sendDataToServer(
        JSON.stringify(data),
        '/api/v1/auth/request-password-reset',
        'PUT',
        'application/json',
        function(response, status, isOK) {
            if (isOK) {
                window.location.href = '/login';
            } else {
                setErrorNotification(response, 0);
            }
        }
    );
	
    
});
