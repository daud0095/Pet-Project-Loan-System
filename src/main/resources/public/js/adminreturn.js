const loan = document.querySelector("#loan");
const udstyr = document.querySelector("#udstyr");

// baseret på loan og udstyr, kommer der information

const loandate = document.querySelector("#loandate");
const afleveringsdate = document.querySelector("#afleveringsdate");
const lateday = document.querySelector("#lateday");
const gebyr = document.querySelector("#gebyr");
const src = document.querySelector("input");


udstyr.addEventListener("change", () => {
    indhold();
});

// i starten er det fast. derfor skal vi udløse denne område
window.addEventListener("DOMContentLoaded", () => {
    console.log(src.value);
    if (src.value.length > 0) {
        indhold();
    }
});


async function indhold() {

    // først henter vi dataene her
    const res = await fetch("/findproduct?udstyr=" + udstyr.value + "&loan=" + loan.value  );
    const data = await res.json();

    loandate.innerHTML = "Loan Dato : " + data.loanDate;
    afleveringsdate.innerHTML = "Afleverings Dato : " + data.deliveryDate;

    // giver os efter dag
    const deliveryDate = new Date(data.deliveryDate);
    const loanDate = new Date(data.loanDate);

    const lateDays = Math.ceil(
        (deliveryDate - loanDate) / (1000 * 60 * 60 * 24)
    );

    lateday.innerHTML = "LateDay : " + lateDays;
    // ***************

    // Gebyr skal ændres senere
    gebyr.innerHTML = "Gebyr : 25 KR";

}