var companiesList;

function initForm() {
    getCompanyList();
    sendGet("?getList=businessCaseCategory", "businessCaseCategory")
    sendGet("?getList=businessCasePhase", "businessCasePhase");
    sendGet("?getList=contactSource", "contactSource");
    $("#licnumdiv").hide();
    $("#updateEntityInRaynet").hide();
    $("#downloadPDF").hide();

    var date = new Date();
    $("#validFrom").val(date.format());
    date.setDate(date.getDate() + 25);
    $("#scheduledEnd").val(date.format());
}

function setDefaults() {
    $("#businessCasePhase").val("42");
    $("#contactSource").val("61");
}

function businessCaseSelect(id) {
    $("#createEntityInRaynet").hide();
    $("#updateEntityInRaynet").hide();
    $("#items").hide();

    if (id === "0") {
        $("#createEntityInRaynet").show();
        $("#items").show();
        $("#downloadPDF").hide();
    }
    else {
        $("#updateEntityInRaynet").show();
        var XmlHTTP;
        if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
        var businessCaseId = $("#businessCase").val();
        XmlHTTP.onreadystatechange = function () {
            if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200)
                var response = XmlHTTP.responseText;
            $("#downloadPDF").attr("href", response);
            $("#downloadPDF").show();
        };
        XmlHTTP.open("GET", "/?getPdfUrl=" + businessCaseId, true);
        XmlHTTP.send();
    }
}

function createProductName() {


    var str = $("form").serialize();
    $("#request").text(str);


    var productId = $("#prod_group_id").val();
    if (productId === "0") return;
    var groupId = $("#goods_type").val();
    var productFullName = $("#prod_group_id option:selected").text();
    var basicPrice = 0;
    var renPrice = 0;
    var expCount = 0;
    var extPrice = 0;
    var exWarPrice = 0;
    var isExtWar = 0;
    var specPrice = 1;
    var addSwm = 0;

    var prices = CZ_PRICES;
    if ($("#currency").val() === "16") prices = EUR_PRICES;
    if (groupId === "1" && $("#goods_type").is(":visible"))
        specPrice = 0.9;
    if (groupId === "2" && $("#goods_type").is(":visible"))
        specPrice = 0.6;

    if (productId === "1") { // Kerio Connect
        basicPrice = prices.connectServer;
        renPrice = prices.RenConnectServer;
    }
    if (productId === "2") { // Kerio Control
        basicPrice = prices.controlServer;
        renPrice = prices.RenControlServer;
    }
    if (productId === "3") { // Kerio Operator
        basicPrice = prices.operatorServer;
        renPrice = prices.RenOperatorServer;
    }
    if (productId === "15") { // Kerio Operator V300
        basicPrice = prices.V300;
    }

    if (productId === "10") { // Kerio Control NG100
        basicPrice = prices.NG100_unl;
        exWarPrice = prices.NG100_war;
    }
    if (productId === "11") { // Kerio Control NG300 Unlimited
        basicPrice = prices.NG300_unl;
        exWarPrice = prices.NG300_war;
    }
    if (productId === "12") { // Kerio Control NG300 25 Users
        basicPrice = prices.NG300_25;
        exWarPrice = prices.NG300_war;
    }
    if (productId === "13") { // Kerio Control NG500 Unlimited
        basicPrice = prices.NG500_unl;
        exWarPrice = prices.NG500_war;
    }
    if (productId === "14") { // Kerio Control NG500 100 Users
        basicPrice = prices.NG500_100;
        exWarPrice = prices.NG500_war;
    }
    if (productId === "16") { // Kerio Control NG500 50 Users
        basicPrice = prices.NG500_50;
        exWarPrice = prices.NG500_war;
    }
    if (productId === "17") { // Kerio Control NG100W
        basicPrice = prices.NG100W_unl;
        exWarPrice = prices.NG100_war;
    }
    if (productId === "18") { // Kerio Control NG300W 25 Users
        basicPrice = prices.NG300W_25;
        exWarPrice = prices.NG300_war;
    }
    if (productId === "19") { // Kerio Control NG300W Unlimited
        basicPrice = prices.NG300W_unl;
        exWarPrice = prices.NG300_war;
    }
    if (productId === "20") { // Kerio Rack Mount Kit for 300 Series HW
        basicPrice = prices.RMK300;
    }


    if ($("#lic_type").is(":visible")) productFullName += $("#goods_type").find("option:selected").text();

    if ($("#lblExt0").is(":visible") && $("#ext_av").is(':checked')) {
        productFullName += ", Kerio Antivirus";
        extPrice += prices.antivirus;
    }
    if ($("#lblExt1").is(":visible") && $("#ext_sync").is(':checked')) {
        productFullName += ", ActiveSync";
        extPrice += prices.activeSync;
    }
    if ($("#lblExt2").is(":visible") && $("#ext_as").is(':checked')) {
        productFullName += ", AntiSpam";
        extPrice += prices.antiSpam;
    }
    if ($("#lblExt3").is(":visible") && $("#ext_war").is(':checked')) {
        productFullName += ", incl. Ex Warr";
        isExtWar = 1;
    }
    if ($("#lblExt4").is(":visible") && $("#ext_wf").is(':checked')) {
        productFullName += ", Kerio Web Filter";
        extPrice += prices.webFilter;
    }

    if ($("#new_swm").is(":visible") && $("#swm_select option:selected").val() == "2") {
        productFullName += ", +1year SWM";
        addSwm = 1;
    }
    if ($("#user_num").is(":visible")) {
        productFullName += ", " + $("#users").val() + " users";
        expCount = $("#users").val() / 5 - 1;
    }
    $("#productName").val(productFullName);
    $("#businessCase").val(productFullName);


    var price = (basicPrice + (expCount ) * prices.add5users + (expCount + 1) * extPrice // first year swm
        + (renPrice + (expCount ) * prices.RenAdd5users + (expCount + 1) * extPrice) * addSwm) // add swm
        * specPrice   // norm, gov, edu
        + exWarPrice * isExtWar;
    $("#price").val(price);

    var discount = $("#discountPercent").val();
    var totalPrice = (price * (100 - discount)) / 100;
    $("#totalPrice").val(totalPrice);

}

function setCompany(company) {
    $("#persons").innerHTML = "";

    sendGet("?getPersonsFor=" + company, "persons");
    sendGet("?getIdFor=" + company, "companyID");
    sendGet("?getBusinessCasesFor=" + company, "businessCase");
}

function createEntityInRaynet() {
    var request = $("#request").val();
    $("#createEntityInRaynet").hide();
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200) {
            sendGet("?getBusinessCasesFor=" + $("#company").val(), "businessCase");
            var response = XmlHTTP.response;
            var elem = document.getElementById("businessCase");
            elem.value =response;
            alert("Obchodní případ vytvořen");

        }
    };
    XmlHTTP.open("PUT", "/businessCase", true);
    XmlHTTP.send(request);
}

function updateEntityInRaynet() {
    var request = $("#request").val();
    var businessCaseId = $("#businessCase").val();
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200)
            $("#response1").attr("href", XmlHTTP.responseText);
        $("#response1").show();
    };
    XmlHTTP.open("POST", "/businessCase/" + businessCaseId, true);
    XmlHTTP.send(request);
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


    $("#businessCaseCategory").val("487");  //Kerio Control
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
        $("#businessCaseCategory").val("486");  //Kerio Connect
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
        $("#businessCaseCategory").val("488");  //Kerio Connect
    }
    if (product === "15") { // Kerio Operator V300
        new_swm.show();
        $("#businessCaseCategory").val("488");  //Kerio Connect
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

function sendGet(request, container) {
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200) {
            document.getElementById(container).innerHTML = XmlHTTP.responseText;
        }
    };
    XmlHTTP.open("GET", request, true);
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

Date.prototype.format = function () {
    var mm = this.getMonth() + 1; // getMonth() is zero-based
    var dd = this.getDate();

    return [this.getFullYear(), "-",
        (mm > 9 ? '' : '0') + mm, "-",
        (dd > 9 ? '' : '0') + dd
    ].join('');
};