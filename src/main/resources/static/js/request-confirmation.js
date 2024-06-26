import {setErrorNotification} from "./components/notifications.js";
import {sendDataToServer} from "./components/networking.js";

const newConfirmationForm = document.getElementById('newConfirmation-form');
const formerUrl = document.referrer;
const curBaseUrl = window.location.href.split("/").slice(0, -1).join("/");

if (performance.navigation.type == performance.navigation.TYPE_RELOAD && formerUrl !== curBaseUrl+"/request-confirmation") {
  	window.location.href = '/request-confirmation';
}
else if(formerUrl.split("?")[0] === curBaseUrl+"/password-reset-form") {
    setErrorNotification("Benutzer muss vor Passwortänderung bestätigt werden!", 0);
}
else if(formerUrl.split("?")[0] === curBaseUrl+"/confirm-redirect") {
    setErrorNotification("Token ist invalide oder wurde bereits benutzt!", 0);
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
