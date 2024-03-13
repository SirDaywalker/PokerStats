import {sendDataToServer} from "./components/networking.js";
import {setErrorNotification} from "./components/notifications.js";


window.deleteInvoice = function(id) {
	
	const data = {
		id: id
	}
	
	sendDataToServer(
		JSON.stringify(data),
		"/api/v1/invoice/delete",
		'DELETE',
		'application/json',
		function(response, status, isOK) {
               if (isOK) {
                    window.location.href = '/my-invoices';
               } else {
                    setErrorNotification(response, 0);
               }
        })
};

document.getElementById('home').addEventListener('click', (event) => {
	event.preventDefault();
	window.location.href = '/home';
});
