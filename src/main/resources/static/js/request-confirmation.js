import {setErrorNotification} from "./components/notifications.js";
import {sendDataToServer} from "./components/networking.js";

const newConfirmationForm = document.getElementById('newConfirmation-form');
const formerUrl = document.referrer;

if (performance.navigation.type == performance.navigation.TYPE_RELOAD && formerUrl !== "http://localhost:8080/request-confirmation") {
  	window.location.href = '/home';
}
else if(formerUrl.split("?")[0] === "http://localhost:8080/confirm-redirect") {
	setErrorNotification("Email konnte nicht bestätigt werden. Bitte erneut anfordern!", 0);
	/*if (Notification.permission === "granted") {
        new Notification("Email konnte nicht bestätigt werden. Bitte erneut anfordern!");
    } else if (Notification.permission !== "denied") {
        Notification.requestPermission().then(function(permission) {
            if (permission === "granted") {
                new Notification("Email konnte nicht bestätigt werden. Bitte erneut anfordern!");
            }
        });
    }*/
}
else if(formerUrl.split("?")[0] === "http://localhost:8080/password-reset") {
    setErrorNotification("Benutzer muss vor Passwortänderung bestätigt werden!", 0);
    /*if (Notification.permission === "granted") {
        new Notification("Benutzer muss vor Passwortänderung bestätigt werden!");
    } else if (Notification.permission !== "denied") {
        Notification.requestPermission().then(function(permission) {
            if (permission === "granted") {
                new Notification("Benutzer muss vor Passwortänderung bestätigt werden!");
            }
        });
    }*/
}

newConfirmationForm.addEventListener('submit', function(event) {
	event.preventDefault();
	
	const data = {
        name: document.getElementById('name').value
    };
    
	sendDataToServer(
        JSON.stringify(data),
        '/api/v1/auth/request-confirmation',
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
