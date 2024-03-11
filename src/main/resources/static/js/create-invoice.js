import {setErrorNotification} from "./components/notifications.js";

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

    const titel = document.getElementById('titel');
    const amount = document.getElementById('amount');
    const due = document.getElementById('due');
    const interest = document.getElementById('interest');
    const interestIntervalDays = document.getElementById('interestIntervalDays');

    const formData = new FormData();
    formData.append('titel', titel);
    formData.append('amount', amount);
    formData.append('due', due);
    formData.append('interest', interest);
    formData.append('interestIntervalDays', interestIntervalDays);

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/v1/auth/create-invoice', true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            window.location.href = '/confirm-invoice';
            return;
        }
        if (xhr.status === 400) {
            setErrorNotification('Ein Fehler ist aufgetreten. Haben Sie alle Felder ausgefüllt?', 0);
        } else {
            setErrorNotification('Status ' + xhr.status.toString(), 0);
        }
    };
    xhr.send(formData);
});
