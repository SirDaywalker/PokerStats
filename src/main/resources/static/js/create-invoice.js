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
    const due = new Date(document.getElementById('due').value+"T23:59:59").getTime();
    const interest = document.getElementById('interest').value;
    const interestIntervalWeeks = document.getElementById('interestIntervalWeeks').value;

    const data = {
        title: title,
        amount: amount,
        due: due,
        interest: interest,
        interestIntervalWeeks: interestIntervalWeeks,
        users: users
    }

    sendDataToServer(
        JSON.stringify(data),
        "/api/v1/invoice/create",
        'POST',
        'application/json',
        function(response, status, isOK) {
               if (isOK) {
                    window.location.href = '/home';
               } else {
                    setErrorNotification(response, 0);
               }
        })
});

function blockNonNumericInput(event) {
	var regex = new RegExp("^[0-9,.]+$");
	var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
	if (!regex.test(key)) {
   	 event.preventDefault();
    	return false;
	}
}

function blockNonNumericInputAndCommaPoint(event) {
	var regex = new RegExp("^[0-9]+$");
	var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
	if (!regex.test(key)) {
   	 event.preventDefault();
    	return false;
	}
}

const inputFieldAmount = document.getElementById('amount');
inputFieldAmount.addEventListener('keypress', blockNonNumericInput);
const inputFieldInterest = document.getElementById('interest');
inputFieldInterest.addEventListener('keypress', blockNonNumericInput);
const inputFieldInterestIntervalWeeks = document.getElementById('interestIntervalWeeks');
inputFieldInterestIntervalWeeks.addEventListener('keypress', blockNonNumericInputAndCommaPoint);