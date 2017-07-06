var companiesList;
function getCompanies(){
    sendPost("getCompanies","companies");
}

function setCompany(company) {
    sendPost("setCompany?"+company,"persons");
}
function getPersons(){
    sendPost("getPersons","persons");
}

function getCompanyList(){
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP=new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function() {
        if (XmlHTTP.readyState===4 && XmlHTTP.status===200) {
            companiesList = JSON.parse(XmlHTTP.responseText);
            $('#autocomplete-dynamic').autocomplete({
                lookup: companiesList
            });
        }
    };
    XmlHTTP.open("POST","/",true);
    XmlHTTP.send("getCompanyList");


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

