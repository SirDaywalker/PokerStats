document.getElementById('picture').addEventListener('change', function() {
    let reader = new FileReader();
    reader.onload = function(e) {
        document.getElementById('image-preview').style.display = 'block';
        document.getElementById('image-preview').src = e.target.result;
    };
    reader.readAsDataURL(this.files[0]);
});

const form = document.getElementsByTagName('form')[0];

form.addEventListener('submit', function(event) {
    event.preventDefault();

    const name = document.getElementById('name').value;
    const password = document.getElementById('password').value;
    const picture = document.getElementById('picture').files[0];

    const formData = new FormData();
    formData.append('name', name);
    formData.append('password', password);
    formData.append('picture', picture);

    const xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/v1/auth/register', true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            window.location.href = '/';
        } else {
            alert('Error: ' + xhr.responseText);
        }
    };
    xhr.send(formData);
});
