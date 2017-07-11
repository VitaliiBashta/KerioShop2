var companiesList;
var itemCount =0;

function initForm() {
    getCompanyList();
    sendGet("?getList=businessCaseCategory", "businessCaseCategory")
    sendGet("?getList=businessCasePhase", "businessCasePhase");
    sendGet("?getList=contactSource", "contactSource");
    $("#licnumdiv").hide();
    document.getElementById("businessCaseCategory").value = '225';
}


function setCompany(company) {
    $("#persons").innerHTML = "";

    sendGet("?getPersonsFor=" + company, "persons");
    sendGet("?getIdFor=" + company, "companyID");
    // sendGet("?getBusinessCasesFor=" + company, "businessCase");
}


function setProduct(product) {
    var licenseType = $("#lic_type");
    var users = $("#user_num");
    var new_swm = $("#new_swm");
    var antivirus = $("#lblExt0");
    var activeSync = $("#lblExt1");
    var antispam = $("#lblExt2");
    var extWarranty = $("#lblExt3");
    var webFilter = $("#lblExt4");

    licenseType.hide();
    users.hide();
    new_swm.hide();
    antivirus.hide();
    activeSync.hide();
    antispam.hide();
    extWarranty.hide();
    webFilter.hide();

    if (product === "1") { // Kerio Connect
        licenseType.show();
        users.show();
        new_swm.show();
        antivirus.show();
        activeSync.show();
        antispam.show();
    }
    if (product === "2") { // Kerio Control
        licenseType.show();
        users.show();
        new_swm.show();
        antivirus.show();
        webFilter.show();
    }
    if (product === "3") { // Kerio Operator
        licenseType.show();
        users.show();
        new_swm.show();
    }
    if (product === "15") { // Kerio Operator V300
        new_swm.show();
    }

    if (product === "10") { // Kerio Control NG100
        extWarranty.show();
    }
    if (product === "11") { // Kerio Control NG300 Unlimited
        extWarranty.show();
    }
    if (product === "12") { // Kerio Control NG300 25 Users
        extWarranty.show();
    }
    if (product === "13") { // Kerio Control NG500 Unlimited
        extWarranty.show();
    }
    if (product === "14") { // Kerio Control NG500 100 Users
        extWarranty.show();
    }
    if (product === "16") { // Kerio Control NG500 50 Users
        extWarranty.show();
    }
    if (product === "17") { // Kerio Control NG100W
        extWarranty.show();
    }
    if (product === "18") { // Kerio Control NG300W 25 Users
        extWarranty.show();
    }
    if (product === "19") { // Kerio Control NG300W Unlimited
        extWarranty.show();
    }
    if (product === "20") { // Kerio Rack Mount Kit for 300 Series HW
    }

}

function getCompanyList() {
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200) {
            companiesList = JSON.parse(XmlHTTP.responseText);
            $('#company').autocomplete({
                lookup: companiesList
            });
        }
    };
    XmlHTTP.open("GET", "/?getCompanyList", true);
    XmlHTTP.send();
}


function requestList(object, target) {
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200) {
            document.getElementById(target).innerHTML = XmlHTTP.responseText;
        }
    };
    XmlHTTP.open("POST", "/", true);
    XmlHTTP.send("getList=" + object);
}

function addItem() {
    var request = "?item="+itemCount;
    request += "&product=" + $("#prod_group_id").find("option:selected").text();
    if ($("#lic_type").is(":visible")) request += "&goods_type=" + $("#goods_type").find("option:selected").text();
    if ($("#user_num").is(":visible")) request += "&users=" + $("#users").val();
    if ($("#new_swm").is(":visible")) request += "&swm=" + $("#swm_select").val();
    if ($("#lblExt0").is(":visible") && $("#ext_av").is(':checked')) request += "&antivirus=1";
    if ($("#lblExt1").is(":visible") && $("#ext_sync").is(':checked')) request += "&activeSync=1";
    if ($("#lblExt2").is(":visible") && $("#ext_as").is(':checked')) request += "&AntiSpam=1";
    if ($("#lblExt3").is(":visible") && $("#ext_war").is(':checked')) request += "&ExtWarranty=1";
    if ($("#lblExt4").is(":visible") && $("#ext_wf").is(':checked')) request += "&WebFilter=1";
    sendPut(request,"/","orderItems");
    itemCount++;
    $("#businessCaseCategory").val("225");
    $("#businessCasePhase").val("42");
    $("#contactSource").val("61");
    $("#description").val(" ");

}


function sendPost(request, container) {
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200)
            document.getElementById(container).innerHTML = XmlHTTP.responseText;
    };
    var params = request;
    XmlHTTP.open("POST", "/", true);
    XmlHTTP.send(params);
}

function sendPut(request, uri, container) {
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200)
            document.getElementById(container).innerHTML += XmlHTTP.responseText;
    };
    var params = request;
    XmlHTTP.open("PUT", uri, container);
    XmlHTTP.send(params);
}


function sendGet(request, container, append) {
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200) {
            if (append)
                document.getElementById(container).innerHTML += XmlHTTP.responseText;
            else
                document.getElementById(container).innerHTML = XmlHTTP.responseText;
            console.log("loaded");
        }
    };
    XmlHTTP.open("GET", request, true);
    console.log("loading ....")
    XmlHTTP.send();
}

function setNewOrExistingLic() {
    var newLicense = $("#licnumdiv");
    var newLicenseRadio = $("#newlic");
    var existingLicense = $("#newcalc");

    newLicense.hide();
    existingLicense.hide();
    if (newLicenseRadio.is(':checked')) existingLicense.show();
    else
        newLicense.show();

}