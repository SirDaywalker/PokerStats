document.getElementById('card').children[0].src = '/assets/error/error' + Math.floor(Math.random() * 3) + ".jpg";
setTimeout(() => {
    document.getElementById('card').classList.remove('flipped');
},500)