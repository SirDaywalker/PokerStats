import {calculatePasswordStrength} from "./components/security.js";

password.addEventListener('input', function() {
    let percentage = calculatePasswordStrength(this.value);
    document.getElementById('bar').style.width = (100 * percentage) + '%';
    document.getElementById('bar').style.backgroundColor = 'hsl(' + (percentage * 180) + ', 100%, 50%)';
});