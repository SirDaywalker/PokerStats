import {getRandomCards} from './getRandomCards.js';

const iconCards = document.getElementById('icon-cards').children;
iconCards[0].src = '/assets/' + getRandomCards();
iconCards[1].src = '/assets/' + getRandomCards();

const tableCards = document.getElementById('table').children;
tableCards[0].children[0].src = '/assets/' + getRandomCards();
tableCards[1].children[0].src = '/assets/' + getRandomCards();
tableCards[2].children[0].src = '/assets/' + getRandomCards();
tableCards[3].children[0].src = '/assets/' + getRandomCards();
tableCards[4].children[0].src = '/assets/' + getRandomCards();

let index = 0;
function flipCard(stop) {
    setTimeout(() => {
        if (index < stop) {
            tableCards[index].classList.remove('flipped');
            index++;
            flipCard(stop);
        }
    }, 500);
}
flipCard(3);
setTimeout(() => {
    flipCard(4);
}, 2000);
setTimeout(() => {
    flipCard(5);
}, 4000);

// Check the combination of the seven cards
const cards_with_values = {
    2: [],
    3: [],
    4: [],
    5: [],
    6: [],
    7: [],
    8: [],
    9: [],
    10: [],
    "jack": [],
    "queen": [],
    "king": [],
    "ace": [],
};

const cards_with_types = {
    "hearts": [],
    "diamonds": [],
    "clubs": [],
    "spades": [],
};

function processCard(index, card) {
    let card_value = card.split('_')[0];
    let card_type = card.split('_')[2].split('.')[0];
    cards_with_values[card_value].push(card);
    cards_with_types[card_type].push(card);
}

for (let i = 0; i < iconCards.length; i++) {
    processCard(i, iconCards[i].src.split('/').pop());
}
for (let i = 0; i < tableCards.length; i++) {
    processCard(i, tableCards[i].children[0].src.split('/').pop());
}

cards_with_values[11] = cards_with_values["jack"];
cards_with_values[12] = cards_with_values["queen"];
cards_with_values[13] = cards_with_values["king"];
cards_with_values[14] = cards_with_values["ace"];
cards_with_values[1] = cards_with_values[14];

let pair = false;
let three_of_a_kind = false;
let four_of_a_kind = false;
let full_house = pair && three_of_a_kind;
let two_pairs = false;

for (let key in cards_with_values) {
    if (cards_with_values[key].length === 2) {
        if (pair) {
            two_pairs = true;
        }
        pair = true;
    }
    if (cards_with_values[key].length === 3) {
        three_of_a_kind = true;
    }
    if (cards_with_values[key].length === 4) {
        four_of_a_kind = true;
    }
}

let flush = false;

for (let key in cards_with_types) {
    if (cards_with_types[key].length === 5) {
        flush = true;
    }
}

let straight = false;
let straight_flush = flush && straight;
let royal_flush = false;


for (let i = 2; i <= 10; i++) {
    if (cards_with_values[i].length === 1 && cards_with_values[i + 1].length === 1 && cards_with_values[i + 2].length === 1 && cards_with_values[i + 3].length === 1 && cards_with_values[i + 4].length === 1) {
        straight = true;
    }
}

if (cards_with_values[10].length === 1 && cards_with_values[11].length === 1 && cards_with_values[12].length === 1 && cards_with_values[13].length === 1 && cards_with_values[14].length === 1) {
    straight = true;

    if (straight_flush && cards_with_types["hearts"].length === 5) {
        royal_flush = true;
    }

}

console.log("Pair: " + pair);
console.log("Three of a kind: " + three_of_a_kind);
console.log("Four of a kind: " + four_of_a_kind);
console.log("Full house: " + full_house);
console.log("Two pairs: " + two_pairs);
console.log("Flush: " + flush);
console.log("Straight: " + straight);
console.log("Royal flush: " + royal_flush);
console.log("Straight flush: " + straight_flush);




