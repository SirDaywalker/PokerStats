import {sendDataToServer} from "./components/networking.js";
import {setErrorNotification} from "./components/notifications.js";
import {sampleData} from "./components/stats.js";

sendDataToServer(null, "/api/v1/games/poker/stats", "GET", null,
    function(response, status, isOK) {
        let data = sampleData;

        if (isOK) {
           data = JSON.parse(response);
        } else {
            setErrorNotification("Status: " + status + " - " + response, -1);
            return;
        }

        if (data["games"].length === 0) {
            setErrorNotification("Es wurden noch keine Spiele gespielt.", -1);
            return;
        }

        let label = []
        for (let i = 1; i <= Object.keys(data["games"]).length; i++) {
            label.push(i);
        }
        // Sort games by key
        data["games"] = Object.keys(data["games"]).sort().reduce(
            (obj, key) => {
                obj[key] = data["games"][key];
                return obj;
            },
            {}
        );

        new Chart("history", {
            type: "line",
            borderColor: "#757575",
            data: {
                labels: label,
                datasets: [
                    {
                        label: "Pot",
                        data: Object.values(data["games"]).map((x) => x.pot),
                        borderColor: "#df00001A",
                        backgroundColor: "#df0000",
                    },
                    {
                        label: "Players",
                        backgroundColor: "#0061df",
                        borderColor: "#0061df1A",

                        // Use the length of the array in data.users as the x-axis.
                        data: Object.values(data["games"]).map((x) => x.users.length),
                    },
                    {
                        label: "Payout",
                        data: Object.values(data["games"]).map((x) => x.payout),
                        backgroundColor: "#64df00",
                        borderColor: "#64df001A",
                    }
                ]
            },
            options: {
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                let label = context.dataset.label || '';

                                if (label === "Players") {
                                    function compareLength(a, b) {
                                        return a.length - b.length;
                                    }

                                    // Return all users and sort them by length.
                                    return [...data["games"][context.parsed.x].users].sort(compareLength);
                                }
                                // Add the currency to the pot and payout. in euro
                                return label + ': €' + context.parsed.y;
                            }
                        }
                    }
                }
            }
        });

        if (Object.keys(data["winners"]).length === 0) {
            setErrorNotification("Es gibt noch keine Gewinner.", 1);
            setErrorNotification("Es gibt noch keine Gewinner.", 2);
            return;
        }

        // Sort users by wins
        data["winners"] = Object.values(data["winners"]).sort((a, b) => b.wins - a.wins);

        new Chart("best-players", {
            type: "bar",
            data: {
                labels: Object.values(data["winners"]).map((x) => x.name),
                datasets: [
                    {
                        label: "Top Spieler",
                        data: Object.values(data["winners"]).map((x) => x.wins),
                        backgroundColor: "#0061df",
                        borderColor: "#0061df1A",
                    }
                ]
            },
        });
        data["winners"] = Object.values(data["winners"]).sort((a, b) => b.payout - a.payout);
        new Chart("top-profit", {
            type: "bar",
            data: {
                labels: Object.values(data["winners"]).map((x) => x.name),
                datasets: [
                    {
                        label: "Top Profiteure",
                        data: Object.values(data["winners"]).map((x) => x.payout),
                        backgroundColor: "#64df00",
                    }
                ]
            },
        });
    }
);
