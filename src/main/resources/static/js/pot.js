let size = document.getElementById("history").getAttribute("data-size");
const pots = document.getElementById("history").getAttribute("data-pots");
const playerDataX = document.getElementById("history").getAttribute("data-player");
size = [...Array(parseInt(size)).keys()];

new Chart("history", {
    type: "line",
    data: {
        labels: size,
        datasets: [
            {
                label: "Pot",
                data: JSON.parse(pots),
                borderColor: "#DF0000",
                fill: false,
                //backgroundColor: "#00000000"
            },
            {
                label: "Players",
                data: JSON.parse(playerDataX),
                borderColor: "#0061df",
                fill: false,
                //backgroundColor: "#00000000"
            }
        ]
    },
});