import {setErrorMessage} from "./setErrorMessage.js";

const users_element = document.getElementById('users');
const selected_users = document.getElementById('selected');

for (let user of users_element.children) {
    user.children[0].addEventListener('click', function(event) {
        const element = event.currentTarget.parentElement;

        if (element.parentNode === users_element) {
            users_element.removeChild(element);
            selected_users.appendChild(element);
        } else {
            selected_users.removeChild(element);
            users_element.appendChild(element);
        }

        if (users_element.children.length === 0) {
            selected_users.style.borderBottom = 'none';
        } else {
            selected_users.style.borderBottom = 'var(--border)';
        }
    });
}

const form = document.getElementById('invoice-form');
form.addEventListener('submit', function(event) {
    event.preventDefault();
    const users = [];
    for (let user of selected_users.children) {
            if (user.localName === "span") {
                continue;
            }
            users.push(user.children[2].innerText);
    }

    const amount = document.getElementById('amount');
    const due = document.getElementById('due');
    const interest = document.getElementById('interest');
    const interestIntervalDays = document.getElementById('interestIntervalDays');

    const formData = new FormData();
    formData.append('amount', amount);
    formData.append('due', due);
    formData.append('interest', interest);
    formData.append('interestIntervalDays', interestIntervalDays);

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/v1/auth/createInvoice', true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            window.location.href = '/confirmInvoice';
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
