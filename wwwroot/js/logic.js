var companiesList;

function initForm() {
    getCompanyList();
    var date = new Date();
    $("#validFrom").val(date.format());
    date.setDate(date.getDate() + 15);
    $("#scheduledEnd").val(date.format());
}

function linkEventsAndComponents() {
    $(".cs-options").live("click", calculate);
    $(".checkbox").live("change", calculate);
    $("#swUsers").live("change", calculate);
    $("#swm").live("change", calculate);
    $("#prod_group_id").on("change", calculate);
    $("#getLicenseInfo").on("click", getLicenseInfo);
    $(".autocomplete-suggestion").live("click", setCompany);
    $('input[type=radio][name=license]').on("change", newOrExistingToggle);

    // $("#newcalc").on("mouseover", createProductName);
    $("#createEntityInRaynet").on("click", createEntityInRaynet);
    $("#businessCase").on("change", businessCaseSelect);
}

function getLicenseInfo() {  //need to be done via API
    var licenseNumber = $("#licnum").val();
    var swUsers = $("#swUsers");
    var existingUsers = $("#existingUsers");

    $("#existingAntivirus").hide();
    $("#existingActiveSync").hide();
    $("#existingAntispam").hide();
    $("#existingWebFilter").hide();

    sendGet("?getLicenseInfo=" + licenseNumber, $("#response"));
    var json = JSON.parse($("#response").val());
    var description = "";
    for (var i = 0; i < json.length; i++) {
        description += json[i] + "\n";
    }
    $("#description").html(description);

    $("#licenseNumber").val(json[0]);
    $("#prod_group_id").val(json[1]);

    $("#lic_type").val(" " + json[2]);

    existingUsers.val(json[3]);
    $("#expirationDate").val(json[4]);


    var extensions = json[5];
    if (extensions.indexOf("Antivirus") !== -1 || extensions.indexOf("Sophos") !== -1)
        $("#existingAntivirus").show();
    if (extensions.indexOf("ActiveSync") !== -1)
        $("#existingActiveSync").show();
    if (extensions.indexOf("AntiSpam") !== -1)
        $("#existingAntispam").show();
    if (extensions.indexOf("Web Filter") !== -1 || extensions.indexOf("WebFilter") !== -1)
        $("#existingWebFilter").show();
    // end simulation

    swUsers.val(existingUsers.val());
    swUsers.attr("min", existingUsers.val());
    calculate();
    // $("#existingAntivirus").show();
}

function businessCaseSelect() {
    var businessCaseId = $("#businessCase").val();
    $("#items").hide();
    $("#downloadPDF").hide();
    $("#createEntityInRaynet").hide();

    if (businessCaseId === "0") {
        $("#createEntityInRaynet").show();
        $("#items").show();
    }
    else {
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

function newOrExistingToggle() {
    var existingUsers = $("#existingUsers").val();
    var swUsers = $("#swUsers");
    var existingProducts = $("#existingProducts");
    var product = $("#productGroup").find(".cs-placeholder");
    var existingProduct = $("#prod_group_id");
    var licType = $("#lic_typeColumn").find(".cs-placeholder");
    var existingLicType = $("#lic_type");
    var licnum = $("#licnum");
    var getLicenseInfo = $("#getLicenseInfo");

    if ($("#newlic").prop("checked")) {
        licnum.hide();
        getLicenseInfo.hide();
        existingProducts.hide();
        existingLicType.hide();
        swUsers.attr("min", "5");
        swUsers.val("5");
        product.show();
        licType.show();
        existingProduct.hide();
        existingProduct.attr("enabled", "enabled");
    } else {
        licnum.show();
        getLicenseInfo.show();
        existingProducts.show();
        existingLicType.show();
        product.hide();
        licType.hide();
        existingProduct.show();
        existingProduct.attr("disabled", "disabled");
    }
}

function calculate() {
    var fullName = "";
    var product = $("#prod_group_id").val();
    var productCode = "487";  //Kerio Control
    var boxProduct = $("#hardware").val();
    if (product === "Kerio Connect") productCode = "486";
    if (product === "Kerio Operator") productCode = "488";
    if (product === "kerioBox" && boxProduct === "V300") productCode = "488";


    $("#businessCaseCategory").val(productCode);

    if ($("#newlic").prop("checked")) {
        fitElements();
        setNewPrices();
        fullName = getFullNameNewLicense();
    } else {
        fitElementsExistingLicense();
        fullName = getFullNameExistingLicense();
    }

    $("#productFullName").val(fullName);
    $("#businessCase").val(fullName);

    var price = $("#price").val();
    var discount = $("#discountPercent").val();
    var totalPrice = (price * (100 - discount)) / 100;
    $("#totalPrice").val(Math.round(100 * totalPrice) / 100);

    var str = $("form").serialize();
    $("#request").text(str);
}

function setNewPrices() {
    var price;
    price = getNewPrice();
    $("#price").val(price);
}

function getLicTypeModifier() {
    var result = 1;
    var lic_type = $("#lic_type").val();
    if (lic_type === " GOV") result = 0.9;
    if (lic_type === " EDU") result = 0.6;
    return result;
}

function getFullNameNewLicense() {
    var result = "";
    var product = $("#prod_group_id").val();

    if (product === "0") return;

    if (product === "kerioBox") {
        var boxProduct = $("#hardware").val();
        if (boxProduct === "V300") result += "New license for Kerio Operator Box V300";
        else result += "Kerio Control " + $("#hwUsers").val() + " " + boxProduct;
    } else {
        result += "New license for " + product;
    }

    result += $("#lic_type").val();

    if (product === "kerioBox") {
        result += ", Kerio Antivirus, Kerio Web Filter";
        if ($("#warrantyLabel").is(":visible") && $("#warranty").is(':checked')) result += " incl. Ext. Warranty";
    }
    if ($("#antispamLabel").is(":visible") && $("#antispam").is(':checked')) result += ", AntiSpam";
    if ($("#antivirusLabel").is(":visible") && $("#antivirus").is(':checked')) result += ", Kerio Antivirus";
    if ($("#activeSyncLabel").is(":visible") && $("#activeSync").is(':checked')) result += ", ActiveSync";
    if ($("#webFilterLabel").is(":visible") && $("#webFilter").is(':checked')) result += ", Kerio Web Filter";

    if ($("#swUsersSection").is(":visible")) result += ", " + $("#swUsers").val() + " users";
    if ($("#swm").val() === "2") result += ", +1year SWM";
    return result;
}

function getFullNameExistingLicense() {
    var result = "";
    var product = $("#prod_group_id").val();

    if (product === "0") return;
    result += "Upgrade to " + product;
    result += $("#lic_type").val();

    if ($("#antispamLabel").is(":visible") && $("#antispam").is(':checked')
        || $("#existingAntispam").is(":visible")) result += ", Kerio AntiSpam";
    if ($("#antivirusLabel").is(":visible") && $("#antivirus").is(':checked')
        || $("#existingAntivirus").is(":visible")) result += ", Kerio Antivirus";
    if ($("#activeSyncLabel").is(":visible") && $("#activeSync").is(':checked')
        || $("#existingActiveSync").is(":visible")) result += ", ActiveSync";
    if ($("#webFilterLabel").is(":visible") && $("#webFilter").is(':checked')
        || $("#existingWebFilter").is(":visible")) result += ", Kerio Web Filter";
    if ($("#swUsersSection").is(":visible")) result += ", " + $("#swUsers").val() + " users";
    if ($("#swm").val() === "2") result += ", +1year SWM";
    return result;
}

function getNewPrice() {
    var result;
    var product = $("#prod_group_id").val();
    var swm = $("#swm").val();
    var hwUsers = $("#hwUsers").val();

    var basicPrice = 0;
    var renPrice = 0;
    var expCount = 0;
    var extPrice = 0;
    var exWarPrice = 0;
    var isExtWar = 0;
    var addSwm = 0;

    if (product === "0") return;

    var prices = CZ_PRICES;
    if ($("#currency").val() === "16") prices = EUR_PRICES;


    if (product === "Kerio Connect") {
        basicPrice = prices.connectServer;
        renPrice = prices.RenConnectServer;
    }
    if (product === "Kerio Control") {
        basicPrice = prices.controlServer;
        renPrice = prices.RenControlServer;
    }
    if (product === "Kerio Operator") {
        basicPrice = prices.operatorServer;
        renPrice = prices.RenOperatorServer;
    }
    if (product === "kerioBox") {
        var boxProduct = $("#hardware").val();
        if (boxProduct === "V300") {
            basicPrice = prices.V300;
            renPrice = prices.RenV300;
            exWarPrice = prices.V300_war;
        }
        if (boxProduct === "NG100") {
            basicPrice = prices.NG100_unl;
            renPrice = prices.RenNG100_unl;
            exWarPrice = prices.NG100_war;
        }
        if (boxProduct === "NG100W") {
            basicPrice = prices.NG100W_unl;
            renPrice = prices.RenNG100W_unl;
            exWarPrice = prices.NG100_war;
        }
        if (boxProduct === "NG300") {
            basicPrice = prices.NG300_unl;
            renPrice = prices.RenNG300_unl;
            exWarPrice = prices.NG300_war;
            if (hwUsers === "25 users") {
                basicPrice = prices.NG300_25;
                renPrice = prices.RenNG300_25;
            }
        }
        if (boxProduct === "NG300W") {
            basicPrice = prices.NG300W_unl;
            renPrice = prices.RenNG300W_unl;
            exWarPrice = prices.NG300_war;
            if (hwUsers === "25 users") {
                basicPrice = prices.NG300W_25;
                renPrice = prices.RenNG300_25;
            }
        }

        if (boxProduct === "NG500") { // Kerio Control NG500
            basicPrice = prices.NG500_unl;
            renPrice = prices.RenNG500_unl;
            exWarPrice = prices.NG500_war;
            if (hwUsers === "50 users") {
                basicPrice = prices.NG500_50;
                renPrice = prices.RenNG500_50;
            }
            if (hwUsers === "100 users") {
                basicPrice = prices.NG500_100;
                renPrice = prices.RenNG500_100;
            }
        }
    }

    if ($("#swUsersSection").is(":visible")) expCount = $("#swUsers").val() / 5 - 1;

    if ($("#antivirusLabel").is(":visible") && $("#antivirus").is(':checked')) extPrice += prices.antivirus;
    if ($("#activeSyncLabel").is(":visible") && $("#activeSync").is(':checked')) extPrice += prices.activeSync;
    if ($("#antispamLabel").is(":visible") && $("#antispam").is(':checked')) extPrice += prices.antiSpam;
    if ($("#webFilterLabel").is(":visible") && $("#webFilter").is(':checked')) extPrice += prices.webFilter;
    if ($("#warrantyLabel").is(":visible") && $("#warranty").is(':checked')) isExtWar = 1;

    if (swm === "2") addSwm = 1;

    result = (basicPrice + expCount * prices.add5users + (expCount + 1) * extPrice // first year swm
        + (renPrice + (expCount ) * prices.RenAdd5users + (expCount + 1) * extPrice) * addSwm) // add swm
        * getLicTypeModifier()   // norm, gov, edu
        + exWarPrice * isExtWar;
    return Math.round(100 * result) / 100;
}

function fitElements() {
    var product = $("#prod_group_id").val();
    var swUsersSection = $("#swUsersSection");
    var antivirusLabel = $("#antivirusLabel");

    hideAll();
    if (product === "0") return;

    if (product === "Kerio Connect") {
        antivirusLabel.show();
        $("#activeSyncLabel").show();
        $("#antispamLabel").show();
    }
    if (product === "Kerio Control") {
        antivirusLabel.show();
        $("#webFilterLabel").show();
    }
    if (product === "kerioBox") {
        $("#warrantyLabel").show();
        $("#boxGroup").show();
        $("#productGroup").attr("colspan", "1");

        var boxProduct = $("#hardware").val();
        if (boxProduct === "V300") swUsersSection.show();
        else  $("#hwUsersSection").show();

        if (boxProduct === "NG300" || boxProduct === "NG300W") $(".users25").show();
        if (boxProduct === "NG500") {
            $(".users50").show();
            $(".users100").show();
        }
    } else {
        $(".GOV").show();
        $(".EDU").show();
        swUsersSection.show();
    }

    $("#productFullName").val(getFullNameNewLicense());
    $("#businessCase").val(getFullNameNewLicense());
}

function hideAll() {
    $(".GOV").hide();
    $(".EDU").hide();


    $("#antivirusLabel").hide();
    $("#activeSyncLabel").hide();
    $("#antispamLabel").hide();
    $("#webFilterLabel").hide();
    $("#warrantyLabel").hide();
    $("#boxGroup").hide();
    $("#hwUsersSection").hide();
    $("#swUsersSection").hide();
    $(".users25").hide();
    $(".users50").hide();
    $(".users100").hide();
    $("#productGroup").attr("colspan", "2");
}
function fitElementsExistingLicense() {
    var product = $("#prod_group_id").val();
    var antivirusLabel = $("#antivirusLabel");
    var existingAntivirus = $("#existingAntivirus");
    hideAll();
    $("#productGroup").attr("colspan", "2");
    if (product === "Kerio Connect") {
        if (!existingAntivirus.is(":visible")) antivirusLabel.show();
        if (!$("#existingActiveSync").is(":visible")) $("#activeSyncLabel").show();
        if (!$("#existingAntispam").is(":visible")) $("#antispamLabel").show();
    }
    if (product === "Kerio Control") {
        if (!existingAntivirus.is(":visible")) antivirusLabel.show();
        if (!$("#existingWebFilter").is(":visible")) $("#webFilterLabel").show();
    }
    $(".GOV").show();
    $(".EDU").show();
    $("#swUsersSection").show();

    // $("#productFullName").val(getFullNameNewLicense());
    // $("#businessCase").val(getFullNameNewLicense());
}

function setCompany() {
    var company = $("#company").val();
    if (company !== "") {
        sendGet("?getPersonsFor=" + company, $("#persons"));
        sendGet("?getIdFor=" + company, $("#companyID"));
        sendGet("?getBusinessCasesFor=" + company, $("#businessCase"));
        $("#mainTable").show();
    }
}

function createEntityInRaynet() {
    serialize();
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

function sendGet(request, container) {
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200) {
            container.html(XmlHTTP.responseText);
        }
    };
    XmlHTTP.open("GET", request, true);
    XmlHTTP.send();
}

Date.prototype.format = function () {
    var mm = this.getMonth() + 1; // getMonth() is zero-based
    var dd = this.getDate();

    return [this.getFullYear(), "-",
        (mm > 9 ? '' : '0') + mm, "-",
        (dd > 9 ? '' : '0') + dd,
    ].join('');
};