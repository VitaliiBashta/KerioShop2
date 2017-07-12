var companiesList;
var itemCount = 0;

function initForm() {
    getCompanyList();
    sendGet("?getList=businessCaseCategory", "businessCaseCategory")
    sendGet("?getList=businessCasePhase", "businessCasePhase");
    sendGet("?getList=contactSource", "contactSource");
    $("#licnumdiv").hide();
}

function calculatePrice() {
    var price = $("#price").val();
    var discount = $("#discountPercent").val();
    var totalPrice = (price * (100 - discount)) / 100;
    $("#totalPrice").html(totalPrice);
}

function setDefaults() {
    $("#businessCaseCategory").val("225");
    $("#businessCasePhase").val("42");
    $("#contactSource").val("61");
}

function createProductName() {
    var productId = $("#prod_group_id").val();
    if (productId === "0") return;
    var groupId = $("#goods_type").val();
    var productFullName = $("#prod_group_id option:selected").text();
    var basicPrice = 0;
    var expPrice = 0;
    var expCount = 0;
    var avPrice = 0;
    var asPrice = 0;
    var syncPrice = 0;
    var wfPrice = 0;
    var exWarPrice = 0;
    var specPrice = 1;
    if (groupId === "1")
        specPrice = 0.9;
    if (groupId === "2")
        specPrice = 0.6;

    if (productId === "1") { // Kerio Connect
        basicPrice = 11010;
        expPrice = 14260 - 11010;
    }
    if (productId === "2") { // Kerio Control
        basicPrice = 6430;
        expPrice = 9680 - 6430;
    }
    if (productId === "3") { // Kerio Operator
        basicPrice = 7840;
        expPrice = 11090 - 7840;
    }
    if (productId === "15") { // Kerio Operator V300
        basicPrice = 21150;
        expPrice = 24400 - 21150;
    }

    if (productId === "10") { // Kerio Control NG100
        basicPrice = 14690;
        exWarPrice = 0;
    }
    if (productId === "11") { // Kerio Control NG300 Unlimited
        basicPrice = 46880;
        exWarPrice = 0;
    }
    if (productId === "12") { // Kerio Control NG300 25 Users
        basicPrice = 29860;
        exWarPrice = 0;
    }
    if (productId === "13") { // Kerio Control NG500 Unlimited
        basicPrice = 92870;
        exWarPrice = 0;
    }
    if (productId === "14") { // Kerio Control NG500 100 Users
        basicPrice = 76630;
        exWarPrice = 0;
    }
    if (productId === "16") { // Kerio Control NG500 50 Users
        basicPrice = 62300;
        exWarPrice = 0;
    }
    if (productId === "17") { // Kerio Control NG100W
        basicPrice = 18323;
        exWarPrice = 0;
    }
    if (productId === "18") { // Kerio Control NG300W 25 Users
        basicPrice = 34296;
        exWarPrice = 0;
    }
    if (productId === "19") { // Kerio Control NG300W Unlimited
        basicPrice = 51680;
        exWarPrice = 0;
    }
    if (productId === "20") { // Kerio Rack Mount Kit for 300 Series HW
        basicPrice = 2960;
    }


    if ($("#lic_type").is(":visible")) productFullName += $("#goods_type").find("option:selected").text();

    if ($("#lblExt0").is(":visible") && $("#ext_av").is(':checked')) {
        productFullName += ", Kerio Antivirus";
        avPrice = 235;
    }
    if ($("#lblExt1").is(":visible") && $("#ext_sync").is(':checked')) {
        productFullName += ", ActiveSync";
        syncPrice = 235;
    }
    if ($("#lblExt2").is(":visible") && $("#ext_as").is(':checked')) {
        productFullName += ", AntiSpam";
        asPrice = 235;
    }
    if ($("#lblExt3").is(":visible") && $("#ext_war").is(':checked')) {
        productFullName += ", incl. Ex Warr";
    }
    if ($("#lblExt4").is(":visible") && $("#ext_wf").is(':checked')) {
        productFullName += ", Kerio Web Filter";
        wfPrice = 470;
    }

    if ($("#new_swm").is(":visible") && $("#swm_select option:selected").val() == "2") {
        productFullName += ", +1year SWM";

    }
    if ($("#user_num").is(":visible")) {
        productFullName += ", " + $("#users").val() + " users";
        expCount = $("#users").val() / 5;
    }
    $("#productName").val(productFullName);


    var price = (basicPrice + (expCount - 1) * expPrice
        + expCount * (avPrice + asPrice + syncPrice + wfPrice)) * specPrice
        + exWarPrice * expCount;
    $("#price").val(price);

    var discount = $("#discountPercent").val();
    var totalPrice = (price * (100 - discount)) / 100;
    $("#totalPrice").val(totalPrice);

}

function setCompany(company) {
    $("#persons").innerHTML = "";

    sendGet("?getPersonsFor=" + company, "persons");
    sendGet("?getIdFor=" + company, "companyID");
}

function submitForm() {
    var request = $("#request").val();
    var url = "/businessCase";
    var container = "response";
    sendPut(request, url, container);
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


function addItem() {
    var request = "?item=" + itemCount;
    request += "&product=" + $("#prod_group_id").find("option:selected").text();
    if ($("#lic_type").is(":visible")) request += "&goods_type=" + $("#goods_type").find("option:selected").text();
    if ($("#user_num").is(":visible")) request += "&users=" + $("#users").val();
    if ($("#new_swm").is(":visible")) request += "&swm=" + $("#swm_select").val();
    if ($("#lblExt0").is(":visible") && $("#ext_av").is(':checked')) request += "&antivirus=1";
    if ($("#lblExt1").is(":visible") && $("#ext_sync").is(':checked')) request += "&activeSync=1";
    if ($("#lblExt2").is(":visible") && $("#ext_as").is(':checked')) request += "&AntiSpam=1";
    if ($("#lblExt3").is(":visible") && $("#ext_war").is(':checked')) request += "&ExtWarranty=1";
    if ($("#lblExt4").is(":visible") && $("#ext_wf").is(':checked')) request += "&WebFilter=1";
    sendPut(request, "/addItem", "productName");
    itemCount++;

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
            document.getElementById(container).innerHTML = XmlHTTP.responseText;
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