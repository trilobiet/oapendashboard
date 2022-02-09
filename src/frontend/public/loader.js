

window.onload = function() {

    setTimeout('if(document.getElementById("loader")) document.getElementById("loader").innerHTML += "<br/>loading calendar data..."', 1000);
    setTimeout('if(document.getElementById("loader")) document.getElementById("loader").innerHTML += "<br/>loading geolocation data..."', 3500);
    setTimeout('if(document.getElementById("loader")) document.getElementById("loader").innerHTML += "<br/>loading funder data..."', 6000);
    setTimeout('if(document.getElementById("loader")) document.getElementById("loader").innerHTML += "<br/>loading publisher data..."', 8500);
}

