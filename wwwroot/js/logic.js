var companiesList;

function initForm() {
    getCompanyList();
    sendGet("?getList=businessCaseCategory", "businessCaseCategory")
    sendGet("?getList=businessCasePhase", "businessCasePhase");

    var date = new Date();
    $("#validFrom").val(date.format());
    date.setDate(date.getDate() + 25);
    $("#scheduledEnd").val(date.format());
}
function linkEventsAndComponents() {
    $("#createEntityInRaynet").on("mouseover", serialize);

    $("#company").on("click", setDefaults);
    $(".cs-options").live("click", getFullNameAndCalculatePrice);
    $(".checkbox").live("change", getFullNameAndCalculatePrice);
    $("#swUsers").live("change", getFullNameAndCalculatePrice);
    $("#prod_group_id").on("change", getFullNameAndCalculatePrice);
    $("#company").on("blur", setCompany);

    // $("#newcalc").on("mouseover", createProductName);


    $("#createEntityInRaynet").on("click", createEntityInRaynet);

    $("#newlic").on("click", setNewOrExistingLic);
    $("#existlic").on("click", setNewOrExistingLic);
    $("#businessCase").on("change", businessCaseSelect);
    $("#updateEntityInRaynet").on("click", updateEntityInRaynet);

}

function setDefaults() {
    $("#businessCasePhase").val("42");
    $("#contactSource").val("61");
}

function businessCaseSelect() {
    var businessCaseId = $("#businessCase").val();
    $("#createEntityInRaynet").hide();
    $("#updateEntityInRaynet").hide();
    $("#items").hide();

    if (businessCaseId === "0") {
        $("#createEntityInRaynet").show();
        $("#items").show();
        $("#downloadPDF").hide();
    }
    else {
        $("#updateEntityInRaynet").show();
        var XmlHTTP;
        if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();

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

function serialize() {
    var str = $("form").serialize();
    console.log(str);
    $("#request").text(str);
}

function getFullNameAndCalculatePrice() {
    var product = $("#prod_group_id").val();
    var GOV = $(".GOV");
    var EDU = $(".EDU");
    var new_swm = $("#new_swm");
    var boxGroup = $("#boxGroup");
    var hwUsers = $("#hwUsers").val();
    var swUsers = $("#swUsers").val();
    var hwUsersSection = $("#hwUsersSection");
    var swUsersSection = $("#swUsersSection");
    var antivirusLabel = $("#antivirusLabel");
    var activeSyncLabel = $("#activeSyncLabel");
    var antispamLabel = $("#antispamLabel");
    var warrantyLabel = $("#warrantyLabel");
    var webFilterLabel = $("#webFilterLabel");
    var users25 = $(".users25");
    var users50 = $(".users50");
    var users100 = $(".users100");
    var lic_type = $("#lic_type").find(".cs-placeholder").text();
    var swm = new_swm.find(".cs-placeholder").text();

    var basicPrice = 0;
    var renPrice = 0;
    var expCount = 0;
    var extPrice = 0;
    var exWarPrice = 0;
    var isExtWar = 0;
    var specPrice = 1;
    var addSwm = 0;

    GOV.hide();
    EDU.hide();
    antivirusLabel.hide();
    activeSyncLabel.hide();
    antispamLabel.hide();
    webFilterLabel.hide();
    warrantyLabel.hide();
    boxGroup.hide();
    hwUsersSection.hide();
    swUsersSection.hide();

    users25.hide();
    users50.hide();
    users100.hide();

    if (product === "0") return;
    $("#productGroup").attr("colspan", "2");

    var productFullName = "";
    var prices = CZ_PRICES;
    if ($("#currency").val() === "16") prices = EUR_PRICES;


    if (product === "kerioConnect") {
        GOV.show();
        EDU.show();
        antivirusLabel.show();
        activeSyncLabel.show();
        antispamLabel.show();
        swUsersSection.show();

        productFullName += "New license for Kerio Connect";
        basicPrice = prices.connectServer;
        renPrice = prices.RenConnectServer;
    }
    if (product === "kerioControl") {
        GOV.show();
        EDU.show();
        swUsersSection.show();
        antivirusLabel.show();
        webFilterLabel.show();
        productFullName += "New license for Kerio Control";
        basicPrice = prices.controlServer;
        renPrice = prices.RenControlServer;
    }
    if (product === "kerioOperator") {
        GOV.show();
        EDU.show();
        swUsersSection.show();
        new_swm.show();
        productFullName += "New license for Kerio Operator";
        basicPrice = prices.operatorServer;
        renPrice = prices.RenOperatorServer;
    }
    if (product === "kerioBox") {
        hwUsersSection.show();
        boxGroup.show();
        warrantyLabel.show();
        hwUsersSection.show();
        $("#productGroup").attr("colspan", "1");

        var boxProduct = $("#hardware").val();
        if (boxProduct === "V300") {
            productFullName += "New license for Kerio Operator Box V300";
            swUsersSection.show();
            hwUsersSection.hide();
            basicPrice = prices.V300;
            renPrice = prices.RenV300;
            exWarPrice = prices.V300_war;
        }
        if (boxProduct === "NG100") {
            productFullName += "Kerio Control NG100";
            basicPrice = prices.NG100_unl;
            renPrice = prices.RenNG100_unl;
            exWarPrice = prices.NG100_war;
        }
        if (boxProduct === "NG100W") {
            productFullName += "Kerio Control NG100W";
            basicPrice = prices.NG100W_unl;
            renPrice = prices.RenNG100W_unl;
            exWarPrice = prices.NG100_war;
        }
        if (boxProduct === "NG300") {
            productFullName += "Kerio Control NG300";
            users25.show();
            basicPrice = prices.NG300_unl;
            renPrice = prices.RenNG300_unl;
            exWarPrice = prices.NG300_war;
            if (hwUsers === "25") {
                basicPrice = prices.NG300_25;
                renPrice = prices.RenNG300_25;
            }
        }
        if (boxProduct === "NG300W") {
            productFullName += "Kerio Control NG300W";
            users25.show();
            basicPrice = prices.NG300W_unl;
            renPrice = prices.RenNG300W_unl;
            exWarPrice = prices.NG300_war;
            if (hwUsers === "25") {
                basicPrice = prices.NG300W_25;
                renPrice = prices.RenNG300_25;
            }
        }

        if (boxProduct === "NG500") { // Kerio Control NG500
            users50.show();
            users100.show();
            basicPrice = prices.NG500_unl;
            renPrice = prices.RenNG500_unl;
            exWarPrice = prices.NG500_war;
            if (hwUsers === "50") {
                basicPrice = prices.NG500_50;
                renPrice = prices.RenNG500_50;
            }
            if (hwUsers === "100") {
                basicPrice = prices.NG500_100;
                renPrice = prices.RenNG500_100;
            }
        }
    }

    if (lic_type === "GOV") {
        specPrice = 0.9;
        productFullName += " GOV";
    }

    if (lic_type === "EDU") {
        specPrice = 0.6;
        productFullName += " EDU";
    }

    if (swUsersSection.is(":visible")) {
        productFullName += ", " + swUsers + " users";
        expCount = swUsers / 5 - 1;
    }

    if (antivirusLabel.is(":visible") && $("#antivirus").is(':checked')) {
        productFullName += ", Kerio Antivirus";
        extPrice += prices.antivirus;
    }
    if (activeSyncLabel.is(":visible") && $("#activeSync").is(':checked')) {
        productFullName += ", ActiveSync";
        extPrice += prices.activeSync;
    }
    if (antispamLabel.is(":visible") && $("#antispam").is(':checked')) {
        productFullName += ", AntiSpam";
        extPrice += prices.antiSpam;
    }
    if (webFilterLabel.is(":visible") && $("#webFilter").is(':checked')) {
        productFullName += ", Kerio Web Filter";
        extPrice += prices.webFilter;
    }
    if (warrantyLabel.is(":visible") && $("#warranty").is(':checked')) {
        productFullName += ", incl. Ex Warr";
        isExtWar = 1;
    }

    if (swm === "2") {
        productFullName += ", +1year SWM";
        addSwm = 1;
    }

    $("#productFullName").val(productFullName);
    $("#businessCase").val(productFullName);


    var price = (basicPrice + expCount * prices.add5users + (expCount + 1) * extPrice // first year swm
        + (renPrice + (expCount ) * prices.RenAdd5users + (expCount + 1) * extPrice) * addSwm) // add swm
        * specPrice   // norm, gov, edu
        + exWarPrice * isExtWar;
    $("#price").val(Math.round(100 * price) / 100);

    var discount = $("#discountPercent").val();
    var totalPrice = (price * (100 - discount)) / 100;
    $("#totalPrice").val(Math.round(100 * totalPrice) / 100);

    var str = $("form").serialize();
    $("#request").text(str);
}

function setCompany() {
    var company = $("#company").val();
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
            elem.value = response;
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