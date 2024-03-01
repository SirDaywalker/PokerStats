/**
 * Check how strong a password is.
 * @param password - The password to check.
 * @returns {number} - The strength of the password as a percentage.
 */
export function calculatePasswordStrength(password) {
    let percentage = 0;

    percentage += Number(new RegExp(/[A-Z]/).test(password)) * 0.25;
    percentage += Number(new RegExp(/[a-z]/).test(password)) * 0.25;
    percentage += Number(new RegExp(/[0-9]/).test(password)) * 0.25;
    percentage += Number(new RegExp(/[^A-Za-z0-9]/).test(password)) * 0.25;
    percentage *= Math.min(password.length / 20, 1)
    return percentage;
}

