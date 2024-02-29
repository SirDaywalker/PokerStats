import {setErrorMessage} from "./setErrorMessage.js";

form.addEventListener('submit', function(event) {
    event.preventDefault();

    const amount = document.getElementByID('amount');
    const due = document.getElementByID('due');
    const interest = document.getElementByID('interest');
    const interestIntervalDays = document.getElementByID('interestIntervalDays');

    const formDate = new FormData();
    formData.append('amount', amount);
    formData.append('due', due);
    formData.append('interest', interest);
    formData.append('interestIntervalDays', interestIntervalDays);

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/v1/auth/createInvoice', true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            window.location.href = '/home';
            return;
        }
        if (xhr.status === 400) {
            setErrorMessage('Ein Fehler ist aufgetreten. Haben Sie alle Felder ausgefüllt?');
        } else {
            setErrorMessage('Status ' + xhr.status.toString());
        }
    };
    xhr.send(formData);
});