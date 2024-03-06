import {sendDataToServer} from "./components/networking.js";

sendDataToServer(null, "/api/v1/games/poker/stats", "GET", null,
    function(response, status, isOK) {
        let data;
        if (isOK) {
           data = JSON.parse(response);
        } else {
            console.error("Status " + status + ": " + response);
            return;
        }

        let label = [...Array(data[0].length).keys()].map((x) => x + 1);

        new Chart("history", {
            type: "line",
            borderColor: "#757575",
            data: {
                labels: label,
                datasets: [
                    {
                        label: "Pot",
                        data: data[0],
                        borderColor: "#df00001A",
                        backgroundColor: "#df0000",
                    },
                    {
                        label: "Players",
                        backgroundColor: "#0061df",
                        borderColor: "#0061df1A",

                        // Use the length of the array in data[2] as the x-axis.
                        data: data[2].map((x) => x.length),
                    },
                    {
                        label: "Payout",
                        data: data[1],
                        backgroundColor: "#64df00",
                        borderColor: "#64df001A",
                    }
                ]
            },
        });
    }
);
