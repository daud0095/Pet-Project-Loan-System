const elev = document.querySelector("#elevlink");
const admin = document.querySelector("#adminlink");
const skolemail = document.getElementsByTagName("label");
console.log(skolemail);

elev.addEventListener("click", (e) => {
    elev.style.background = "#7a8a9950";
    elev.style.color = "#FF0024FF";
    admin.style.background = "#CAF0F8";
    admin.style.color = "#03045E";
    e.preventDefault();
    skolemail[0].innerHTML = "Skolemail:";
});

admin.addEventListener("click", (e) => {
    elev.style.background = "#CAF0F8";
    elev.style.color = "#03045E";
    admin.style.background = "#7a8a9950";
    admin.style.color = "#FF0024FF";
    e.preventDefault();
    skolemail[0].innerHTML = "Admin:";
});

