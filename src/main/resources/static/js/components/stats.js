export const sampleData = {
    "games": {
        "0": {
            "users": [],
            "payout": 7,
            "pot": 14,
        }
    },
    "winners": {
        "117": {
            "name": "Peter",
            "wins": 2,
            "payout": 14.0,
        },
    }
}


/**
 * Process the stats response from api/v1/games/poker/stats
 * @param data {string} The response data.
 * @returns {*} The parsed response.
 */
export function processStatsData(data) {
    try {
        data = JSON.parse(data);
    } catch (e) {
        throw new Error("Die Antwort des Servers konnte nicht verarbeitet werden.");
    }

    if (data["games"].length === 0) {
        throw new Error("Es wurden noch keine Spiele gespielt.");
    }
    return data;
}