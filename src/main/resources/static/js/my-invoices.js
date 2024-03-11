const form = document.getElementById('my-invoices');

form.addEventListener('submit', function(event) {
	event.preventDefault();
	window.location.href = "/home";
})