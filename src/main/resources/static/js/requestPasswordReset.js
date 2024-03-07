import {setErrorMessage} from "./setErrorMessage.js";
import {sendDataToServer} from "./components/networking.js";

const resetPasswordForm = document.getElementById('resetPassword-form');

document.getElementById('back').addEventListener('click', function(event) {
	event.preventDefault();
	window.location.href = '/login';
});

resetPasswordForm.addEventListener('submit', function(event) {
	event.preventDefault();
	
	const data = {
        name: document.getElementById('name').value
    };
    
	sendDataToServer(
        JSON.stringify(data),
        '/api/v1/auth/requestPasswordReset',
        'PUT',
        'application/json',
        function(response, status, isOK) {
            if (isOK) {
                window.location.href = '/login';
            } else {
                setErrorMessage(response);
            }
        }
    );
	
    
});