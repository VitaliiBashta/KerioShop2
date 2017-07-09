var companiesList;


function initForm() {
    getCompanyList();
    requestList("getBusinessCaseCategoryList","businessCaseCategory");
    requestList("getBusinessCasePhaseList","businessCasePhase");
    requestList("getContactSourceList","contactSource");

}
function setCompany(company) {
    document.getElementById("persons").innerHTML ="";
    sendPost("setCompany?"+company,"persons");
}

function getCompanyList(){
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP=new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function() {
        if (XmlHTTP.readyState===4 && XmlHTTP.status===200) {
            companiesList = JSON.parse(XmlHTTP.responseText);
            $('#company').autocomplete({
                lookup: companiesList
            });
        }
    };
    XmlHTTP.open("POST","/",true);
    XmlHTTP.send("getCompanyList");
}


function requestList(object, target){
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP=new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function() {
        if (XmlHTTP.readyState===4 && XmlHTTP.status===200) {
            document.getElementById(target).innerHTML = XmlHTTP.responseText;
        }
    };
    XmlHTTP.open("POST","/",true);
    XmlHTTP.send(object);
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

