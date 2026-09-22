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
   const srch = document.querySelector("#srch");

    loadudstyr();

    async function loadudstyr() {

        // Vi henter dataene fra javalin. (fast struktur)
        // Vi ændrer kun path her fordi javascript fanger routes "/products"
        // Denne routes returnerer alle produkter
        // restende af kode er samme.
        const res = await fetch("/products");
        // console.log("STATUS:", res.status);

        const data = await res.json();
        // console.log("DATA:", data);
        // console.log("DATA LENGTH:", data.length);

        // 4.process søge efter function
        const newData = searchUdstyr(data);
        // console.log("NEWDATA:", newData);

        // 2.process ==> Sidetal opdatering.
        // Hvis sidetællingen er fuld, skal du ikke genberegne; ellers skal du beregne.
        // Fordi kommandoen `load udstyr` konstant kaldes af funktioner, forårsager dette et problem.

        if (sideNumber.options.length === 0) {
            findSidetallet(newData);
        }

        // 3.process
        // Den finder det valgte sidetal og sender det til "render"-function.
        const pageNumber = sideNumber.value;
        // console.log("PAGE:", pageNumber);

        // så vi kan beregne hvilken 6 udstyr der vil vises baseret på sidetallet.
        render(newData, pageNumber);

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
            image.src = "/images/" + data[i].picturePath;
            tableLine.appendChild(linjeIndholdBillede);

            // For navn
            const linjeIndholdComputer = document.createElement("td");
            linjeIndholdComputer.textContent = data[i].name;
            tableLine.appendChild(linjeIndholdComputer);

            // For Beskrivelse
            const linjeIndholdBeskrivelse = document.createElement("td");
            linjeIndholdBeskrivelse.textContent = data[i].description;
            tableLine.appendChild(linjeIndholdBeskrivelse);

            // For Stk
            const linjeIndholdStk = document.createElement("td");
            linjeIndholdStk.textContent = data[i].stock;
            tableLine.appendChild(linjeIndholdStk);

            // For Status
            const linjeIndholdStatus = document.createElement("td");
            linjeIndholdStatus.className = "status";
            linjeIndholdStatus.textContent = data[i].status;

            // Hvis status er "udlånt", ændres baggrundsfarven.
            if (data[i].status === "Udlånt") {
                linjeIndholdStatus.style.background = "red";
            } else {
                linjeIndholdStatus.style.background = "#DAFFD6";
            }

            tableLine.appendChild(linjeIndholdStatus);

            // For Afleveringsdato
            const linjeIndholdAfleveringsdato = document.createElement("td");
            linjeIndholdAfleveringsdato.textContent = data[i].deliveryDate;
            tableLine.appendChild(linjeIndholdAfleveringsdato);


            const linjeIndholdButton = document.createElement("td");
            const linjeIndholdKnap = document.createElement("button");
            linjeIndholdKnap.className = "btn";
            linjeIndholdKnap.type = "button";
            linjeIndholdKnap.id = "loan" + data[i].id;

            // vi kan tilgå router på den måde
            // når man trykker på lån-knap, fungerer function
            linjeIndholdKnap.onclick = function () { window.location.href="/loan?id=" + data[i].id } ;

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

// ********************************************
// 4.process ==> Søge efter funktion
srch.addEventListener("change", () => {
    // Først nulstilles skærmen
    const tableBody = document.querySelector("tbody");
    tableBody.replaceChildren();
    console.log(srch.value);

    // Der er et problem fordi sidetal opdateres ikke
    // Hvis sidetal er tom, kører function for sidetal
    // Derfor vi nulstiller sidetal
    sideNumber.replaceChildren();

    // Efter nulstillingen kaldes funktionen for at hente nye data
    loadudstyr();

});

function searchUdstyr(data){

    const search = srch.value;

    // Hvis man skriver noget, fungerer søgefeltet.
    // "filter" udfører en handling på hvert dataelement.
    // For hvert dataelement i datasættet sammenlignede vi datanavnene i JSON-filen.
    if (search.length > 0) {
        return data.filter(item => item.name.toLowerCase().startsWith(search.toLowerCase()));
    }

    // Hvis søgefeltet er tomt, returneres dataene direkte.
    return data;
}

// ********************************************

