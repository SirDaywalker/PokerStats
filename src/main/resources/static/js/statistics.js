import { sendDataToServer } from "./components/networking.js";
import { processStatsData, sampleData } from "./components/stats.js";
import { setErrorNotification } from "./components/notifications.js";

sendDataToServer(null, "/api/v1/games/poker/stats", "GET", null,
    function (response) {
        let data = sampleData;
        try {
            data = processStatsData(response);
        } catch (e) {
            setErrorNotification(e.message, -1);
            return;
        }
        const userId = document.getElementById("win-rate").getAttribute("data-user-id");

        if (data["winners"][userId] === undefined) {
            data["winners"][userId] = {
                "name": "You",
                "wins": 0,
                "payout": 0.0
            };
        }

        const latestGames_typeGamesWonElements = document.getElementsByClassName("win");
        for (let element of latestGames_typeGamesWonElements) {
            let id = element.getAttribute("data-id");
            element.innerText = "+ " + data.games[id].payout + " €";
        }

        const totalPayoutElement = document.getElementById("total-payout");
        const totalPayout = data.winners[userId].payout;
        totalPayoutElement.innerText = totalPayout.toFixed(2) + " €";

        const wins = data["winners"][userId].wins;
        const totalGames = [];
        for (let game in data["games"]) {
            let users = data["games"][game].users;
            if (Object.keys(users).includes(userId)) {
                totalGames.push(data["games"][game]);
            }
        }
        const winRate = (wins / totalGames.length) * 100;

        new Chart("win-rate", {
            type: "doughnut",
            data: {
                labels: ["Gewonnen", "Verloren"],
                datasets: [
                    {
                        label: "Siege",
                        data: [wins, totalGames.length - wins],
                        backgroundColor: ["#0061df1A", "#df00001A"],
                        borderColor: ["#0061df", "#df0000"],
                    }
                ]
            },
            options: {
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                let label = context.dataset.label || "";
                                if (label === "Siege") {
                                    return context.parsed + " Spiele";
                                }
                                return context.parsed + " Spiele";
                            }
                        }
                    },
                    legend: {
                        display: false, // Die Legende ausblenden
                    },
                    title: {
                        display: true,
                        text: `Siegesrate: ${winRate.toFixed(2)}%`,
                        position: "bottom",
                        padding: {
                            top: 20,
                            bottom: 0
                        },
                        font: {
                            size: 16,
                            weight: "bold",
                        },
                    },
                },
                maintainAspectRatio: false,
            },
        });
    }
);
