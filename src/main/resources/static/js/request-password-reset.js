import {setDefaultNotification, setErrorNotification} from "./components/notifications.js";
import {sendDataToServer} from "./components/networking.js";

const resetPasswordForm = document.getElementById('resetPassword-form');
const formerUrl = document.referrer;
const curBaseUrl = window.location.href.split("/").slice(0, -1).join("/");

if (performance.navigation.type == performance.navigation.TYPE_RELOAD && formerUrl !== curBaseUrl+"/request-password-reset") {
    window.location.href = '/request-password-reset';
}
else if(formerUrl.split("?")[0] === curBaseUrl+"/password-reset-form") {
    setErrorNotification("Anfrage zur Passwort Zurücksetzung invalide oder abgelaufen. Bitte erneut anfordern!", 0);
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
                return;
            } else {
                setErrorNotification(response, 0);
            }
        }
    );
	
    
});
