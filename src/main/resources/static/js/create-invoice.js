import {setErrorNotification} from "./components/notifications.js";
import {sendDataToServer} from "./components/networking.js";

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
            users.push(user.children[4].innerText);
    }

    const title = document.getElementById('title').value;
    const amount = document.getElementById('amount').value;
    const due = document.getElementById('due').value;
    const interest = document.getElementById('interest').value;
    const interestIntervalDays = document.getElementById('interestIntervalDays').value;

    const data = {
        title: title,
        amount: amount,
        due: due,
        interest: interest,
        interestIntervalDays: interestIntervalDays,
        users: users
    }

    sendDataToServer(
        JSON.stringify(data),
        "/api/v1/invoice/create-invoice",
        'POST',
        'application/json',
        function(response, status, isOK) {
               if (isOK) {
                    window.location.href = '/confirm-invoice';
               } else {
                    setErrorNotification(response, 0);
               }
        })
});
