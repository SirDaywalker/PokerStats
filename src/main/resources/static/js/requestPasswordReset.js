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
				if (Notification.permission === "granted") {
			        alert("Email zur Passwortzurücksetzung wurde gesendet!");
			    } else if (Notification.permission !== "denied") {
			        Notification.requestPermission().then(function(permission) {
			            if (permission === "granted") {
			                alert("Email zur Passwortzurücksetzung wurde gesendet!");
			            }
			        });
			    }
                window.location.href = '/login';
            } else {
                setErrorMessage(response);
            }
        }
    );
	
    
});
