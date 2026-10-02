const activeLoan = document.querySelector(".activeLoan");
const returnloan = document.querySelector(".returnLoan");

// når man trykker på aktiveloan, først nulstilles skærmen, bagefter kommer der ny data
const body = document.querySelector("tbody");

// vi lytter til begge klik
activeLoan.addEventListener("click", () => {
    body.replaceChildren();

    // vi laver routes på den måde. Hvis det er aktiveloan. bliver id 1
    // vi kan hente denne id i routes og lave nogle ting
    window.location.href="/myloan?id=1"
});

returnloan.addEventListener("click", () => {
    body.replaceChildren();
    window.location.href="/myloan?id=2"
});

// id = 1  ==> aktiveloan
// id = 2  ==> returnloan