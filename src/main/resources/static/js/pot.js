import {sendDataToServer} from "./components/networking.js";
import {setErrorMessage} from "./setErrorMessage.js";

sendDataToServer(null, "/api/v1/games/poker/stats", "GET", null,
    function(response, status, isOK) {
        let data;
        if (isOK) {
           data = JSON.parse(response);
        } else {
            setErrorMessage("Status: " + status + " - " + response);
            return;
        }

        let label = Object.keys(data).map((x) => parseInt(x) + 1);

        new Chart("history", {
            type: "line",
            borderColor: "#757575",
            data: {
                labels: label,
                datasets: [
                    {
                        label: "Pot",
                        data: data[0],
                        data: Object.values(data).map((x) => x.pot),
                        borderColor: "#df00001A",
                        backgroundColor: "#df0000",
                    },
                    {
                        label: "Players",
                        backgroundColor: "#0061df",
                        borderColor: "#0061df1A",

                        // Use the length of the array in data[2] as the x-axis.
                        // Use the length of the array in data.users as the x-axis.
                        data: Object.values(data).map((x) => x.users.length),
                    },
                    {
                        label: "Payout",
                        data: data[1],
                        data: Object.values(data).map((x) => x.payout),
                        backgroundColor: "#64df00",
                        borderColor: "#64df001A",
                    }
                ]
            },
        });
    }
);
