import {getRandomCards} from './getRandomCards.js';

const iconCards = document.getElementById('icon-cards').children;
iconCards[0].children[0].src = '/assets/' + getRandomCards();
iconCards[1].children[0].src = '/assets/' + getRandomCards();