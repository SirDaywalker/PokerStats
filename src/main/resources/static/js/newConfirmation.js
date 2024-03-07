import {setErrorMessage} from "./setErrorMessage";

document.getElementById('confirmation').addEventListener('click', function(event) {
    setErrorMessage("Email wurde gesendet!")
    if (Notification.permission === "granted") {
        const notification = new Notification("Email wurde gesendet!");
    } else if (Notification.permission !== "denied") {
        Notification.requestPermission().then(function(permission) {
            if (permission === "granted") {
                const notification = new Notification("Email wurde gesendet!");
            }
        });
    }
});