import {getRandomCards} from './getRandomCards.js';

const iconCards = document.getElementById('icon-cards').children;
iconCards[0].children[0].src = '/assets/' + getRandomCards();
iconCards[1].children[0].src = '/assets/' + getRandomCards();

const tableCards = document.getElementById('table').children;
for (let i = 0; i < tableCards.length; i++) {
    tableCards[i].children[0].src = '/assets/' + getRandomCards();
}

const cards = {};
for (let i = 0; i < 14; i++) {
    cards[i] = [];
}
const convertCardValue = {
    "jack": [10],
    "queen": [11],
    "king": [12],
    "ace": [0, 13]
}

for (let card of [].concat(Array.prototype.slice.call(iconCards), Array.prototype.slice.call(tableCards))) {
    let cardValue = card.children[0].src.split('/').pop().split('_')[0];
    let cardType = card.children[0].src.split('/').pop().split('_')[2].split('.')[0];
    if (cardValue === "jack" || cardValue === "queen" || cardValue === "king" || cardValue === "ace") {
        cardValue = convertCardValue[cardValue];
    }
    for (let value of cardValue) {
        cards[value].push(cardType);
    }
}

const combination = document.getElementById('combination');

let straight = false;

for (let i = 0; i < 10; i++) {
    for (let j = 0; j < 5; j++) {
        if (cards[i + j].length === 0) {
            break;
        }
        if (j === 4) {
            straight = true;
        }
    }

    // check if straight flush
    if (straight) {
        let hearts = 0;
        let diamonds = 0;
        let clubs = 0;
        let spades = 0;

        for (let j = 0; j < 5; j++) {
            if (cards[i + j].includes("hearts")) {
                hearts++;
            }
            if (cards[i + j].includes("diamonds")) {
                diamonds++;
            }
            if (cards[i + j].includes("clubs")) {
                clubs++;
            }
            if (cards[i + j].includes("spades")) {
                spades++;
            }
        }

        if (hearts === 5 || diamonds === 5 || clubs === 5 || spades === 5) {
            if (i === 9) {
                combination.innerText = "ROYAL FLUSH";
            } else {
                combination.innerText = "STRAIGHT FLUSH";
            }
            return;
        }
        combination.innerText = "STRAIGHT";
        return;
    }
}

// check for regular flush

let hearts = 0;
let diamonds = 0;
let clubs = 0;
let spades = 0;

for (let i = 0; i < 14; i++) {
    if (cards[i].includes("hearts")) {
        hearts++;
    }
    if (cards[i].includes("diamonds")) {
        diamonds++;
    }
    if (cards[i].includes("clubs")) {
        clubs++;
    }
    if (cards[i].includes("spades")) {
        spades++;
    }
}

if (hearts >= 5 || diamonds >= 5 || clubs >= 5 || spades >= 5) {
    combination.innerText = "FLUSH";
    return;
}

// check for four of a kind
for (let i = 0; i < 14; i++) {
    if (cards[i].length === 4) {
        combination.innerText = "FOUR OF A KIND";
        return;
    }
}

let threeOfAKind = false;
let pair = false;
let twoPairs = false;

// check for full house
for (let i = 0; i < 14; i++) {
    if (cards[i].length === 3) {
        threeOfAKind = true;
    }
    if (cards[i].length === 2) {
        if (pair) {
            twoPairs = true;
        }
        pair = true;
    }

    if (threeOfAKind && pair) {
        combination.innerText = "FULL HOUSE";
        return;
    }
    if (threeOfAKind) {
        combination.innerText = "THREE OF A KIND";
        return;
    }
    if (twoPairs) {
        combination.innerText = "TWO PAIRS";
        return;
    }
    if (pair) {
        combination.innerText = "PAIR";
        return;
    }
}

combination.innerText = "HIGH CARD";

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
setTimeout(() => {
    combination.style.transform = 'scale(1)';
}, 4500);
