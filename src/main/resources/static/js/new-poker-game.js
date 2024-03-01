import {setErrorMessage} from "./setErrorMessage.js";

const users_element = document.getElementById('users');
const selected_users = document.getElementById('selected');

const potElement = document.getElementById('pot');
const payoutElement = document.getElementById('payout');

const defaultPot = Number(potElement.getAttribute('data-pot'));

function updatePot() {
    let pot = defaultPot;
    for (let user of selected_users.children) {
        if (user.localName === "span") {
            continue;
        }
        pot += Number(user.children[1].getAttribute('data-buy-in'));
    }
    pot = pot.toFixed(2);
    let payout = (pot / 2).toFixed(2)

    potElement.value = String(pot).replace(/\./, ",") + " € im Pot";
    payoutElement.value = String(payout).replace(/\./, ",") + " € für den Gewinner";
}

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
        updatePot();

        if (users_element.children.length === 0) {
            selected_users.style.borderBottom = 'none';
        } else {
            selected_users.style.borderBottom = 'var(--border)';
        }
    });
}

const form = document.getElementById('new-poker-game-form');
form.addEventListener('submit', function(event) {
    event.preventDefault();
    const users = [];
    for (let user of selected_users.children) {
        if (user.localName === "span") {
            continue;
        }
        users.push(user.children[0].children[1].innerText);
    }

    const data = {
        players: users,
        notes: document.getElementById('notes').value
    };
    fetch('/api/v1/poker-game/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => {
        if (response.ok) {
            window.location.href = '/games/poker';
        } else {
            response.text().then(text => {
                if (text === "") {
                    text = "Status " + response.status + ": " + response.statusText
                }
                setErrorMessage(text);
            });
        }
    });
});
