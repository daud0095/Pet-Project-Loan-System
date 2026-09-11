/* for JSON - Dynamiske indehold*/

// 2.process
// Sidetallet skal kunne findes automatisk,
// fordi det vil blive divideret med 6 i henhold til antallet af data
const sidetal = document.querySelector("select");

// 4.process
const srch = document.querySelector("#srch");

// Hent første udstyr for 1.process
loadudstyr();

async function loadudstyr() {
    const res = await fetch("data/udstyr.json");
    const data = await res.json();

    // 4.process for search
    const newData = searchUdstyr(data);

    // 2.process ==> sidetal opdatering
    // Hvis sidetællingen er fuld, skal du ikke genberegne; ellers skal du beregne.
    // Fordi kommandoen `load udstyr` konstant kaldes af funktioner, forårsager dette et problem.

    if (sidetal.options.length === 0) {
        findSidetallet(newData);
    }

    // 3.process
    // Hvis man vælger 1 på rulleliste, vises de første 6 udstyr på skærmen.
    // Hvis 2 , vises de anden 6 udstyr på skærmen og så videre.
    const pagenumber = sidetal.value;

    render(newData,pagenumber);
}

function render(data,pagenumber) {

    // 1.process
    const tablebody = document.querySelector("tbody");

    // pageNumber blev tilføjet for 3.process
    for (let i = (pagenumber * 6 - 6) ; i < (pagenumber * 6)  && i < data.length ; i++) {

        const tablelinje = document.createElement("tr");
        tablebody.appendChild(tablelinje);

        // For billede
        const linjeIndholdBillede = document.createElement("td");
        const image = document.createElement("img");
        linjeIndholdBillede.appendChild(image);

        image.className="billede"
        image.src = "../public/images/" + data[i].Billede;
        tablelinje.appendChild(linjeIndholdBillede);

        // For navn
        const linjeIndholdComputer = document.createElement("td");
        linjeIndholdComputer.textContent = data[i].Navn;
        tablelinje.appendChild(linjeIndholdComputer);

        // For Beskrivelse
        const linjeIndholdBeskrivelse = document.createElement("td");
        linjeIndholdBeskrivelse.textContent = data[i].Beskrivelse;
        tablelinje.appendChild(linjeIndholdBeskrivelse);

        // For Stk
        const linjeIndholdStk = document.createElement("td");
        linjeIndholdStk.textContent = data[i].Stk;
        tablelinje.appendChild(linjeIndholdStk);

        // For Status
        const linjeIndholdStatus = document.createElement("td");
        linjeIndholdStatus.className = "status";
        linjeIndholdStatus.textContent = data[i].Status;

        if (data[i].Status === "Udlånt") {
            linjeIndholdStatus.style.background = "red";
        } else {
            linjeIndholdStatus.style.background = "#DAFFD6";
        }

        tablelinje.appendChild(linjeIndholdStatus);

        // For Afleveringsdato
        const linjeIndholdAfleveringsdato = document.createElement("td");
        linjeIndholdAfleveringsdato.textContent = data[i].Afleveringsdato;
        tablelinje.appendChild(linjeIndholdAfleveringsdato);


        const linjeIndholdButton = document.createElement("td");
        const linjeIndholdKnap = document.createElement("button");
        linjeIndholdKnap.className = "btn";
        linjeIndholdKnap.type = "button";
        linjeIndholdKnap.id = "loan" + data[i].id;
        linjeIndholdKnap.name = "loan" + data[i].id;
        linjeIndholdKnap.value = "Lån";
        linjeIndholdKnap.textContent = " Lån";
        linjeIndholdButton.appendChild(linjeIndholdKnap);
        tablelinje.appendChild(linjeIndholdButton);

        /*  Hvis udstyrs status er udlånt, bliver Lån knap inaktive   */
        if (linjeIndholdStatus.innerHTML === "Udlånt") {
            linjeIndholdKnap.textContent = "Udlånt";
            linjeIndholdKnap.disabled = true;
        }

    }

    console.log(tablebody);

    /* Loan */
    loan();

}

/* ****************************************************** */
// 2.process ==> Rulleliste skal være dynamiske
function findSidetallet(data) {
    const pageUdstyrNumber = Math.ceil(data.length / 6);
    sidetal.replaceChildren();
    for (let j = 1; j <= pageUdstyrNumber; j++) {
        const option = document.createElement("option");
        option.value = j;
        option.textContent = j;
        sidetal.appendChild(option);
    }
}

/*     <option value="1">1</option>  */
/* ****************************************************** */

// 3.process ==> Programmet lytter til sidetal og henter udstyr fra Json
sidetal.addEventListener("change", () => {
    const tablebody = document.querySelector("tbody");
    tablebody.replaceChildren();
    loadudstyr();
});

/* ****************************************************** */

// 4.process ==> Søge efter funktion
function searchUdstyr(data) {
    const search = srch.value;

    if (search.length > 0) {
        return data.filter(item =>
            item.Navn.toLowerCase().startsWith(search.toLowerCase())
        );
    }

    return data;
}

// 3.process
// Den sammenligner navnene i henhold til hvad der er skrevet i søgefeltet;
// for eksempel kan du skrive "com" og udføre en søgning.
srch.addEventListener("change", () => {
    const tablebody = document.querySelector("tbody");
    // Den genberegner sideantallet og body baseret på de nye data.
    // Vi nulstiller dem
    tablebody.replaceChildren();
    sidetal.replaceChildren();

    loadudstyr();
});


//**************************
/* user og password registereres til localstorage */
const params = new URLSearchParams(window.location.search);
const mail = params.get("skolemail");
const password = params.get("password");

if (localStorage.getItem("mail") === null) {
    localStorage.setItem("mail", mail);
    localStorage.setItem("password", password);
}

const user = document.querySelector(".user");
user.innerHTML = localStorage.getItem("mail");

console.log(localStorage.getItem("mail"));
console.log(localStorage.getItem("password"));



/* Loan */
function loan() {
    const produktpage = document.querySelector(".produktpage");
    const loan = document.querySelectorAll(".btn");
    console.log(loan);

    for (const lon of loan) {
        lon.addEventListener("click", () => {
            produktpage.replaceChildren();

            // for at hente computer
            const navn = lon.parentElement.previousElementSibling.previousElementSibling.previousElementSibling.previousElementSibling.previousElementSibling.innerHTML;
            const beskrivelse = lon.parentElement.previousElementSibling.previousElementSibling.previousElementSibling.previousElementSibling.innerHTML;
            const stk = lon.parentElement.previousElementSibling.previousElementSibling.previousElementSibling.innerHTML;

            console.log(navn);
            console.log(beskrivelse);
            console.log(stk);

            // Titel
            const loanHead = document.createElement("h2");
            loanHead.innerHTML = "Lån udstyr";
            loanHead.className = "loantitel";
            produktpage.appendChild(loanHead);

            const loanUdstyr = document.createElement("div");
            loanUdstyr.className = "loanudstyr";
            loanUdstyr.innerHTML = "<p>" + navn + "</p>" +
                "<p>Beskrivelse : " + beskrivelse + "   -   " + "Stk. : " + stk + "</p>";
            produktpage.appendChild(loanUdstyr);

            const borrowerDiv = document.createElement("div");
            borrowerDiv.innerHTML = "<p>Låner</p>";
            borrowerDiv.className = "loandetaljeret";
            produktpage.appendChild(borrowerDiv);

            const borrowerRulleliste = document.createElement("select");
            borrowerRulleliste.name = "loan";
            borrowerDiv.appendChild(borrowerRulleliste);

            const borrowerOption = document.createElement("option");
            borrowerOption.innerHTML = localStorage.getItem("mail");
            borrowerRulleliste.appendChild(borrowerOption);

            const afleveringsDatoLabel = document.createElement("p");
            afleveringsDatoLabel.innerHTML="Afleverings Dato"
            borrowerDiv.appendChild(afleveringsDatoLabel);

            const afleveringsDatoDato = document.createElement("input");
            afleveringsDatoDato.type = "date";
            borrowerDiv.appendChild(afleveringsDatoDato);

            const forklaring = document.createElement("p");
            forklaring.innerHTML = "Bæmerkning(Valgfri)";
            borrowerDiv.appendChild(forklaring);

            const forklaringArea = document.createElement("input");
            forklaringArea.type = "textarea";
            forklaringArea.placeholder = "Du kan skrive her noget..."
            borrowerDiv.appendChild(forklaringArea);

            const btnDiv = document.createElement("div");
            btnDiv.className = "loanbtn";
            produktpage.appendChild(btnDiv);

            const cancelBtn = document.createElement("button");
            cancelBtn.type = "button";
            cancelBtn.value = "Annulere";
            cancelBtn.className = "cancelBtn";
            cancelBtn.innerHTML = "Annulere";
            btnDiv.appendChild(cancelBtn);

            const approvalBtn = document.createElement("button");
            approvalBtn.type = "button";
            approvalBtn.value = "Bekræfte lån";
            approvalBtn.className = "approvalBtn";
            approvalBtn.innerHTML = "Bekræfte lån";
            btnDiv.appendChild(approvalBtn);

            console.log(produktpage);

            cancelBtn.addEventListener("click", () => {
                window.location.href = "produktside.html";
            });

        });
    }



}

/*
<tr>
    <td><img className="billede" src="../public/images/computer.png"/></td>
    <td>Computer</td>
    <td>16 RAM</td>
    <td>8</td>
    <td className="status">Ledigt</td>
    <td>-</td>
    <td>
        <button type="button" id="loan1" name="loan1" value="Lån">
            Lån
        </button>
    </td>
</tr>
*/



/* localstorage */




