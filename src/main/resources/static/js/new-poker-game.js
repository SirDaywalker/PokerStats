const users_element = document.getElementById('users');
const selected_users = document.getElementById('selected');

const potElement = document.getElementById('total-pot');
const payout = document.getElementById('winning-amount');

let defaultPot = "";
for (let char of potElement.value) {
    if (char === '€') {
        break;
    }
    defaultPot += char;
}
defaultPot = Number(defaultPot.replace(/,/g, '.'));

function updatePot() {
    let pot = defaultPot;
    for (let user of selected_users.children) {
        if (user.localName === "span") {
            continue;
        }
        pot += Number(user.children[3].innerText);
    }
    pot = pot.toString().replace(/,/g, '.');
    pot = Number(pot).toFixed(2);
    let halfPot = (pot / 2).toFixed(2);
    pot = pot.toString().replace(/\./g, ',');
    halfPot = halfPot.toString().replace(/\./g, ',');

    potElement.value = pot + " € im Pot";
    // Round to 2 decimal places
    payout.value = halfPot + " € für den Gewinner";
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
