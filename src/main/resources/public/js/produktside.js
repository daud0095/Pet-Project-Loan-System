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
        const res = await fetch("data/JSON");
        const data = await res.json();
        if (sideNumber.options.length === 0) {
            findSidetallet(data);
        }
        const pageNumber = sideNumber.value;
        render(data, pageNumber);

    }

    function render(data, pagenumber) {
        const tableBody = document.querySelector("tbody");
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

sideNumber.addEventListener("change", () => {
    const tBody = document.querySelector("tbody");
    tBody.replaceChildren();
    loadudstyr();


})

function findSidetallet(data) {
    const pageUdstyrNumber = Math.ceil(data.length / 6);
    sideNumber.replaceChildren();
    for (let j = 1; j <= pageUdstyrNumber; j++) {
        const option = document.createElement("option");
        option.value = j;
        option.textContent = j;
        sideNumber.appendChild(option);
    }
}