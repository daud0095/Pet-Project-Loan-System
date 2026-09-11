// <tr>
//
//     <td><img class="billede" src="../public/images/computer.png"></img><td>
//         <td>Computer</td>
//         <td>16 RAM</td>
//         <td>8</td>
//         <td class="status">Ledigt</td>
//         <td>-</td>
//         <td>
//             <button type="button" id="loan1" name="loan1" value="Lån">
//                 Lån
//             </button>
//         </td>
//     </tr>



   const sideNumber = document.querySelector("select");



    loadudstyr();

    async function loadudstyr() {

        // Vi henter dataene i JSON-format. (fast struktur)
        const res = await fetch("data/udstyr.json");
        const data = await res.json();

        // 2.process ==> Sidetal opdatering.
        // Hvis sidetællingen er fuld, skal du ikke genberegne; ellers skal du beregne.
        // Fordi kommandoen `load udstyr` konstant kaldes af funktioner, forårsager dette et problem.

        if (sideNumber.options.length === 0) {
            findSidetallet(data);
        }

        // 3.process
        // Den finder det valgte sidetal og sender det til "render"-function.
        const pageNumber = sideNumber.value;

        // så vi kan beregne hvilken 6 udstyr der vil vises baseret på sidetallet.
        render(data, pageNumber);

    }

    // 1.process
    function render(data, pagenumber) {

        const tableBody = document.querySelector("tbody");

        // pageNumber blev tilføjet for 3.process
        // Hvis man vælger 1 på rulleliste, vises de første 6 udstyr på skærmen.
        // Hvis 2 , vises de anden 6 udstyr på skærmen og så videre.

        for (let i = (pagenumber * 6 - 6) ; i < (pagenumber * 6)  && i < data.length ; i++) {

            const tableLine = document.createElement("tr");
            tableBody.appendChild(tableLine);

            // For billede
            const linjeIndholdBillede = document.createElement("td");
            const image = document.createElement("img");
            linjeIndholdBillede.appendChild(image);

            image.className="billede"
            image.src = "../public/images/" + data[i].Billede;
            tableLine.appendChild(linjeIndholdBillede);

            // For navn
            const linjeIndholdComputer = document.createElement("td");
            linjeIndholdComputer.textContent = data[i].Navn;
            tableLine.appendChild(linjeIndholdComputer);

            // For Beskrivelse
            const linjeIndholdBeskrivelse = document.createElement("td");
            linjeIndholdBeskrivelse.textContent = data[i].Beskrivelse;
            tableLine.appendChild(linjeIndholdBeskrivelse);

            // For Stk
            const linjeIndholdStk = document.createElement("td");
            linjeIndholdStk.textContent = data[i].Stk;
            tableLine.appendChild(linjeIndholdStk);

            // For Status
            const linjeIndholdStatus = document.createElement("td");
            linjeIndholdStatus.className = "status";
            linjeIndholdStatus.textContent = data[i].Status;

            // Hvis status er "udlånt", ændres baggrundsfarven.
            if (data[i].Status === "Udlånt") {
                linjeIndholdStatus.style.background = "red";
            } else {
                linjeIndholdStatus.style.background = "#DAFFD6";
            }

            tableLine.appendChild(linjeIndholdStatus);

            // For Afleveringsdato
            const linjeIndholdAfleveringsdato = document.createElement("td");
            linjeIndholdAfleveringsdato.textContent = data[i].Afleveringsdato;
            tableLine.appendChild(linjeIndholdAfleveringsdato);


            const linjeIndholdButton = document.createElement("td");
            const linjeIndholdKnap = document.createElement("button");
            linjeIndholdKnap.className = "btn";
            linjeIndholdKnap.type = "button";
            linjeIndholdKnap.id = "loan" + data[i].id;
            linjeIndholdKnap.name = "loan" + data[i].id;
            linjeIndholdKnap.value = "Lån";
            linjeIndholdKnap.textContent = " Lån";
            linjeIndholdButton.appendChild(linjeIndholdKnap);
            tableLine.appendChild(linjeIndholdButton);

            /*  Hvis udstyrs status er udlånt, bliver Lån knap inaktive   */
            if (linjeIndholdStatus.innerHTML === "Udlånt") {
                linjeIndholdKnap.textContent = "Udlånt";
                linjeIndholdKnap.disabled = true;
            }
        }


        console.log(tableBody);



    }

// 3.process ==> Når der vælges et sidetæller, ryddes "body", og funktionen `loadudstyr` kaldes.
// Dette skyldes, at funktionen `loadudstyr` henter dataene fra JSON'en.
// Hvis "body" ikke ryddes, vil de samme `udstyr`-tilføje oven på gamle udstyr

sideNumber.addEventListener("change", () => {
    const tBody = document.querySelector("tbody");
    tBody.replaceChildren();
    loadudstyr();


})

// 2.process ==> Rulleliste skal være dynamiske
// Rullelisten er organiseret efter den samlede mængde udstyr, og rullelisten laves dynamisk,
// så rullelisten automatisk vises korrekt, uanset om det er 20 udstyr eller 30 udstyr.

// Hvis der er F eks. 20 udstyr, indeholder rullelisten 1, 2, 3, 4.
// Hvis der er F eks. 32 udstyr, indeholder rullelisten 1, 2, 3, 4, 5, 6.
// I logik divideres totalen med 6 og rundes op.

function findSidetallet(data) {

    const pageUdstyrNumber = Math.ceil(data.length / 6);
    sideNumber.replaceChildren();

    // I produktsidet HTML placeres indhold ved hjælp af `for each`.

    for (let j = 1; j <= pageUdstyrNumber; j++) {
        const option = document.createElement("option");
        option.value = j;
        option.textContent = j;
        sideNumber.appendChild(option);
    }
}