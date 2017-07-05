
function getCompanies(){
    sendPost("getCompanies","companies");
}

function getPersons(){
    sendPost("getPersons","persons");
}



function sendPost(request, container){
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP=new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function() {
        if (XmlHTTP.readyState===4 && XmlHTTP.status===200)
            document.getElementById(container).innerHTML=XmlHTTP.responseText;
    };
    var params = request;
    XmlHTTP.open("POST","/",true);
    XmlHTTP.send(params);
}
