let size = document.getElementById("history").getAttribute("data-size");
const pots = document.getElementById("history").getAttribute("data-pots");
const playerDataX = document.getElementById("history").getAttribute("data-player");
const payouts = document.getElementById("history").getAttribute("data-payouts");
size = [...Array(parseInt(size)).keys()].map((x) => x + 1);

new Chart("history", {
    type: "line",
    borderColor: "#757575",
    data: {
        labels: size,
        datasets: [
            {
                label: "Pot",
                data: JSON.parse(pots),
                borderColor: "#df00001A",
                backgroundColor: "#df0000",
            },
            {
                label: "Players",
                data: JSON.parse(playerDataX),
                backgroundColor: "#0061df",
                borderColor: "#0061df1A",
            },
            {
                label: "Payout",
                data: JSON.parse(payouts),
                backgroundColor: "#64df00",
                borderColor: "#64df001A",
            }
        ]
    },
});