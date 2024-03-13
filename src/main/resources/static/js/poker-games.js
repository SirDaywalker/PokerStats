import {sendDataToServer} from "./components/networking.js";
import {setDefaultNotification, setErrorNotification} from "./components/notifications.js";

let gameContainers = document.getElementsByClassName('game');

function updateGame(gameId, winner) {
    const data = {
        gameId: gameId,
        userId: winner
    }

    sendDataToServer(
        JSON.stringify(data),
        '/api/v1/games/poker/update-winner',
        'POST',
        'application/json',
        (text, status, isOK) => {
            if (!isOK) {
                return setErrorNotification("Fehler beim Aktualisieren des Spiels.", 0);
            }
            setDefaultNotification("Spiel erfolgreich aktualisiert.", 0);
        }
    )
}

function moveUserToSelected(event) {
    let user = event.target;

    if (!user.classList.contains('user')) {
        user = user.parentElement;
    }

    for (let element of gameContainers) {
        if (element.contains(user)) {
            let userContainer = element.getElementsByClassName('user-container')[0];
            let selectedContainer = element.getElementsByClassName('selected')[0];

            if (selectedContainer.children.length >= 2) {
                for (let children of selectedContainer.children) {
                    if (children.classList.contains('user')) {
                        let userClone = children.cloneNode(true);
                        userClone.addEventListener('click', moveUserToSelected);
                        userContainer.appendChild(userClone);
                        selectedContainer.removeChild(children);
                        break;
                    }
                }
            }

            let winnerText = element.getElementsByClassName('winner')[0];
            if (winnerText) {
                winnerText.children[0].innerHTML = user.innerText;
            } else {
                // Create a new html element like above
                const winner = document.createElement('div');
                winner.classList.add('winner');
                const span = document.createElement('span');
                span.innerHTML = user.innerText;
                winner.appendChild(span);
                const div = document.createElement('div');
                const span2 = document.createElement('span');
                span2.innerHTML = 'Gewinner';
                div.appendChild(span2);
                winner.appendChild(div);

                let parent = null;
                if (element.getElementsByClassName('badge')[0]) {
                    parent = element.getElementsByClassName('badge')[0].parentElement;
                } else {
                    parent = element.getElementsByClassName('user-container')[0];
                }
                let runningBadge = element.getElementsByClassName('badge')[0];
                if (runningBadge) {
                    runningBadge.remove();
                }
                parent.insertBefore(winner, parent.childNodes[2]);
            }

            updateGame(element.getAttribute('data-game-id'), user.getAttribute('data-user-id'));

            let userClone = user.cloneNode(true);
            userClone.addEventListener('click', moveUserToUserContainer);
            selectedContainer.appendChild(userClone);
            userContainer.removeChild(user);
            return;
        }
    }
}

function moveUserToUserContainer(event) {
    let user = event.target;

    if (!user.classList.contains('user')) {
        user = user.parentElement;
    }

    for (let element of gameContainers) {
        if (element.contains(user)) {
            let userContainer = element.getElementsByClassName('user-container')[0];

            if (!element.getElementsByClassName('badge')[0]) {

                // Create a new html element to store the user's badge
                const runningBadge = document.createElement('div');
                runningBadge.classList.add('badge');
                runningBadge.classList.add('green-to-yellow');
                const innerDiv = document.createElement('div');
                const span = document.createElement('span');
                span.innerHTML = 'Running';
                innerDiv.appendChild(span);
                runningBadge.appendChild(innerDiv);

                let parent = null;
                if (element.getElementsByClassName('winner')[0]) {
                    parent = element.getElementsByClassName('winner')[0].parentElement;
                } else {
                    parent = element.getElementsByClassName('badge')[0].parentElement;
                }
                let winnerText = element.getElementsByClassName('winner')[0];
                if (winnerText) {
                    winnerText.remove();
                }
                parent.insertBefore(runningBadge, parent.childNodes[2]);
            }

            updateGame(element.getAttribute('data-game-id'), null);

            let userClone = user.cloneNode(true);
            userClone.addEventListener('click', moveUserToSelected);
            userContainer.appendChild(userClone);
            user.remove();
            return;
        }
    }
}

for (let element of gameContainers) {
    element.children[0].addEventListener('click', function () {
        if (element.classList.contains('expanded')) {
            element.classList.remove('expanded');
            return;
        }
        element.classList.add('expanded');
    });

    let userElements = element.getElementsByClassName('user');
    for (let userElement of userElements) {
        userElement.addEventListener('click', moveUserToSelected);
    }
}
