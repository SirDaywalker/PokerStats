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

        let label = Object.keys(data["games"]).map((x) => parseInt(x) + 1);
        const userId = document.getElementById("win-rate").getAttribute("data-user-id");

        if (data["winners"][userId] === undefined) {
            data["winners"][userId] = {
                "name": "You",
                "wins": 0,
                "payout": 0.0
            };
        }

        const wins = data["winners"][userId].wins;
        const totalGames = label.length;
        const winRate = (wins / totalGames) * 100;

        new Chart("win-rate", {
            type: "doughnut",
            data: {
                labels: ["Gewonnen", "Verloren"],
                datasets: [
                    {
                        label: "Siege",
                        data: [wins, totalGames - wins],
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
                        text: `Siegesrate: ${winRate.toFixed(2)}%`, // Die Gewinnrate in Prozent anzeigen
                        position: "bottom",
                        padding: 10,
                        font: {
                            size: 16,
                            weight: "bold",
                        },
                    },
                },
                cutout: "80%", // Die Größe des inneren Lochs anpassen
            },
        });
    }
);
