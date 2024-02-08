const cardTypes = ["hearts", "diamonds", "clubs", "spades"];
const cardValues = ["2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace"];

const cards = [];

for (let i = 0; i < cardTypes.length; i++) {
    for (let j = 0; j < cardValues.length; j++) {
        cards.push(cardValues[j] + "_of_" + cardTypes[i] + ".svg");
    }
}

export function getRandomCards() {
    let index = Math.floor(Math.random() * cards.length);
    let card = cards[index];
    cards.splice(index, 1);
    return card;
}