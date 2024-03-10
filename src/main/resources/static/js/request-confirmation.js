import {setErrorNotification} from "./components/notifications.js";
import {sendDataToServer} from "./components/networking.js";

const newConfirmationForm = document.getElementById('newConfirmation-form');

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
