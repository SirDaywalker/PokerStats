import {setErrorNotification} from "./notifications.js";

function setPreviewImage(preview, file, placeholder) {
    preview.src = URL.createObjectURL(file);
    preview.style.display = 'block';

    if (placeholder) {
        placeholder.style.display = 'none';
    }
}

function removePreviewImage(preview, placeholder) {
    if (placeholder) {
        preview.src = '';
        preview.style.display = 'none';
        placeholder.style.display = 'none';
    } // If there is no placeholder, the preview should not be removed
}

function checkFile(file) {
    if (!file.type.startsWith('image/')) {
        throw new Error('Bitte wählen Sie ein Bild aus.');
    }
    if (file.size > 10485760) {
        throw new Error('Das Bild darf nicht größer als 10MB sein.');
    }
}

export function setProfilePicture(previewID, inputObject, placeholderID = 'img-example') {
    const preview = document.getElementById(previewID);
    const placeholder = document.getElementById(placeholderID);
    const file = inputObject.files[0];

    if (file) {
        try {
            checkFile(file);
        } catch (error) {
            setErrorNotification(error.message, 0);
            inputObject.value = null;
            removePreviewImage(preview, placeholder);
            return;
        }
        setPreviewImage(preview, file, placeholder);
    }
}