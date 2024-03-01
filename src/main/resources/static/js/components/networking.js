/**
 * This function sends a request to the server.
 * @param data - The data to send in the request. Must match the content type.
 * @param {string} url - The URL to send the request to.
 * @param {string} method - The method to use for the request.
 * @param {string | null} contentType - The content type of the request.
 * @param {function} callback - The function to call when the request is complete. Should have three parameters:
 *                              response, status, and isOK.
 * @returns {void}
 */
export function sendDataToServer(data, url, method, contentType, callback) {
    const init = {
        method: method,
        headers: {
            'Content-Type': contentType
        },
        body: data
    };
    if (contentType === null) { // If the content type is not set, let the browser set it.
        delete init.headers['Content-Type'];
    }
    fetch(url, init)
    .then(response => {
        response.text().then(text => {
            callback(text, response.status, response.ok);
        });
    });
}