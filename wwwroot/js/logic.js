var companiesList;

function initForm() {
    getCompanyList();
    var date = new Date();
    $("#validFrom").val(date.format());
    date.setDate(date.getDate() + 15);
    $("#scheduledEnd").val(date.format());

    $(".cs-options").live("click", calculate);
    $(".checkbox").live("change", calculate);
    $("#swUsers").live("change", calculate);
    $("#validFrom").live("change", calculate);
    $("#price").live("change", calculate);
    $("#swm").live("change", calculate);
    // $("#prod_group_id").on("change", calculate);
    $("#getLicenseInfo").on("click", getLicenseInfo);
    $(".autocomplete-suggestion").live("click", setCompany);
    $("#existingLicense").on("change", newOrExistingToggle);

    $("#createEntityInRaynet").on("click", createEntityInRaynet);
    $("#businessCase").on("change", businessCaseSelect);
}

function getLicenseInfo() {  //need to be done via API
    var licenseNumber = $("#licnum").val();
    var swUsers = $("#swUsers");

    var XmlHTTP = new XMLHttpRequest();

    XmlHTTP.open("GET", "/licenseInfo/?" + licenseNumber, false);
    XmlHTTP.send();
    if (XmlHTTP.status === 200) {
        $("#existingProducts").show();
        $("#products").show();
        $("#existingProduct").show();

        var response = XmlHTTP.responseText;
        $("#response").html(response);
        var json = JSON.parse(response);
        var description = "";
        for (var i = 0; i < json.length; i++) {
            description += json[i] + "\n";
        }
        $("#description").html(description);
        $("#licenseNumber").html(json[0]);
        $("#prod_group_id").val(json[1]);
        $("#lic_type").val(" " + json[2]);
        $("#existingUsers").html(json[3]);
        $("#expirationDate").val(json[4]);
        $("#existingExtensions").html(json[5]);
        // end simulation

        var validFrom = new Date($("#validFrom").val());
        var expirationDate = new Date(json[4]);
        var timeDiff = expirationDate.getTime() - validFrom.getTime();
        var diffYears = Math.round(timeDiff / (1000 * 3600 * 24 * 365));
        var swm = $("#swm");
        swm.attr("min", "0");
        if (diffYears < 0) {
            swm.attr("max", 1 - diffYears);
        }
        swUsers.val(json[3]);
        swUsers.attr("min", json[3]);
    }
    calculate();
}

function businessCaseSelect() {
    var businessCaseId = $("#businessCase").val();
    var items = $("#items");
    var createEntityInRaynet = $("#createEntityInRaynet");
    var downloadPDF = $("#downloadPDF");
    downloadPDF.hide();
    items.hide();
    createEntityInRaynet.hide();

    if (businessCaseId === "0") {
        createEntityInRaynet.show();
        items.show();
    }
    else {
        var XmlHTTP;
        if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();

        XmlHTTP.onreadystatechange = function () {
            if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200) {
                var response = XmlHTTP.responseText;
                downloadPDF.attr("href", response);
                downloadPDF.show();
            }
        };
        XmlHTTP.open("GET", "/info/?getPdfUrl=" + businessCaseId, true);
        XmlHTTP.send();
    }
}

function serialize() {
    var str = $("form").serialize();
    console.log(str);
    $("#request").text(str);
}

function newOrExistingToggle() {
    var swUsers = $("#swUsers");
    var licnum = $("#licnum");
    var getLicenseInfo = $("#getLicenseInfo");
    var products = $("#products");

    if ($("#existingLicense").prop("checked")) {
        products.hide();
        licnum.show();
        getLicenseInfo.show();
        $("#lic_type").show();
        $("#prod_group_id").show();
        $("#productGroup").find(".cs-placeholder").hide();
        $("#lic_typeColumn").find(".cs-placeholder").hide();
    } else {
        products.show();
        licnum.hide();
        getLicenseInfo.hide();
        $("#existingProducts").hide();
        $("#lic_type").hide();
        swUsers.attr("min", "5");
        swUsers.val("5");
        $("#productGroup").find(".cs-placeholder").show();
        $("#lic_typeColumn").find(".cs-placeholder").show();
        $("#prod_group_id").hide();
    }
}

function calculate() {
    var productGroup = $("#prod_group_id").val();
    var boxProduct = $("#hardware").val();
    var productCode = "487";  //Kerio Control
    if (productGroup === "Kerio Connect") productCode = "486";
    if (productGroup === "Kerio Operator") productCode = "488";
    if (productGroup === "" && boxProduct === "V300") productCode = "488";
    $("#businessCaseCategory").val(productCode);


    if ($("#existingLicense").prop("checked")) {
        fitElementsExistingLicense();
    } else {
        fitElements();
    }

    var price = $("#price").val();
    var discount = $("#discountPercent").val();
    var totalPrice = (price * (100 - discount)) / 100;
    $("#totalPrice").val(Math.round(100 * totalPrice) / 100);

    var str = $("form").serialize();
    $("#request").text(str);
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

    if (product === "") {
        var boxProduct = $("#hardware").val();
        if (boxProduct === "V300") result += "New license for Kerio Operator Box V300";
        else result += "Kerio Control " + $("#hwUsers").val() + " " + boxProduct;
    } else {
        result += "New license for " + product;
    }

    result += $("#lic_type").val();

    if (product === "" && boxProduct !== "V300") {
        result += ", Kerio Antivirus, Kerio Web Filter";

    }
    if ($("#warrantyLabel").is(":visible") && $("#warranty").is(':checked')) result += " incl. Ext. Warranty";
    if ($("#antispamLabel").is(":visible") && $("#antispam").is(':checked')) result += ", AntiSpam";
    if ($("#antivirusLabel").is(":visible") && $("#antivirus").is(':checked')) result += ", Kerio Antivirus";
    if ($("#activeSyncLabel").is(":visible") && $("#activeSync").is(':checked')) result += ", ActiveSync";
    if ($("#webFilterLabel").is(":visible") && $("#webFilter").is(':checked')) result += ", Kerio Web Filter";

    if ($("#swUsersSection").is(":visible")) result += ", " + $("#swUsers").val() + " users";
    var swm = $("#swm").val();
    if (swm > "0") result += ", +" + swm + " year" + (swm > 1 ? "s" : "") + " SWM";
    return result;
}

function getFullNameExistingLicense() {
    var result = "";
    var product = $("#prod_group_id").val();
    var existingExtensions = $("#existingExtensions").html();
    if (product === "0") return;
    result += "Upgrade to " + product;
    result += $("#lic_type").val();

    if ($("#antispamLabel").is(":visible") && $("#antispam").is(':checked')
        || (existingExtensions.indexOf("Antispam") > -1 )) result += ", Kerio AntiSpam";
    if ($("#antivirusLabel").is(":visible") && $("#antivirus").is(':checked')
        || (existingExtensions.indexOf("Sophos") > -1 && existingExtensions.indexOf("Antivirus") > -1)) result += ", Kerio Antivirus";
    if ($("#activeSyncLabel").is(":visible") && $("#activeSync").is(':checked')
        || existingExtensions.indexOf("ActiveSync") > -1) result += ", ActiveSync";
    if ($("#webFilterLabel").is(":visible") && $("#webFilter").is(':checked')
        || existingExtensions.indexOf("WebFilter") > -1) result += ", Kerio Web Filter";
    if ($("#swUsersSection").is(":visible")) result += ", " + $("#swUsers").val() + " users";
    var swm = $("#swm").val();
    if (swm > "0") result += ", +" + swm + " year" + (swm > 1 ? "s" : "") + " SWM";
    return result;
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

function fitElements() {
    var productGroup = $("#prod_group_id").val();
    var swUsersSection = $("#swUsersSection");
    var antivirusLabel = $("#antivirusLabel");
    var users;
    var swm = $("#swm").val();

    hideAll();

    if (productGroup === "0") return;
    users = $("#swUsers").val();
    if (productGroup === "Kerio Connect") {
        antivirusLabel.show();
        $("#activeSyncLabel").show();
        $("#antispamLabel").show();
    }
    if (productGroup === "Kerio Control") {
        antivirusLabel.show();
        $("#webFilterLabel").show();
    }
    if (productGroup === "") {
        $("#warrantyLabel").show();
        $("#boxGroup").show();
        $("#productGroup").attr("colspan", "1");
        users = $("#hwUsers").val();

        var boxProduct = $("#hardware").val();
        if (boxProduct === "V300") {
            swUsersSection.show();
            users = $("#swUsers").val();
        }
        else $("#hwUsersSection").show();

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

    var currency = $("#currency").val();
    if (productGroup === "") productGroup = boxProduct;

    var newProduct = {
        product: productGroup,
        users: users,
        swm: swm,
        antivirus: antivirusLabel.is(":visible") && $("#antivirus").is(':checked'),
        activeSync: $("#activeSyncLabel").is(":visible") && $("#activeSync").is(':checked'),
        antiSpam: $("#antispamLabel").is(":visible") && $("#antispam").is(':checked'),
        webFilter: $("#webFilterLabel").is(":visible") && $("#webFilter").is(':checked'),
        exWarranty: $("#warrantyLabel").is(":visible") && $("#warranty").is(':checked')
    };

    $("#price").val(calculateNewPrice2(currency, newProduct));
    var fullName = getFullNameNewLicense();
    $("#productFullName").val(fullName);
    $("#businessCase").val(fullName);
}

function fitElementsExistingLicense() {
    var product = $("#prod_group_id").val();
    var currency = $("#currency").val();
    var users = $("#swUsers").val();
    var swm = Number($("#swm").val());

    var antivirusLabel = $("#antivirusLabel");
    var activeSyncLabel = $("#activeSyncLabel");
    var antispamLabel = $("#antispamLabel");
    var webFilterLabel = $("#webFilterLabel");

    var existingAntivirus = $("#existingAntivirus");
    var existingExtensions = $("#existingExtensions").html();
    hideAll();
    $("#productGroup").attr("colspan", "2");
    if (product === "Kerio Connect") {
        if (existingExtensions.indexOf("Sophos") === -1 && existingExtensions.indexOf("Antivirus") === -1) antivirusLabel.show();
        if (existingExtensions.indexOf("ActiveSync") === -1) activeSyncLabel.show();
        if (existingExtensions.indexOf("AntiSpam") === -1) antispamLabel.show();
    }
    if (product === "Kerio Control") {
        if (existingExtensions.indexOf("Sophos") === -1 && existingExtensions.indexOf("Antivirus") === -1) antivirusLabel.show();
        if (existingExtensions.indexOf("WebFilter") === -1) webFilterLabel.show();
    }
    $(".GOV").show();
    $(".EDU").show();
    $("#swUsersSection").show();

    var fullName = getFullNameExistingLicense();
    $("#productFullName").val(fullName);
    $("#businessCase").val(fullName);

    var oldProduct = {
        product: product,
        users: $("#existingUsers").html(),
        expirationDate: $("#expirationDate").val(),
        extensions: existingExtensions
    };

    var newProduct = {
        validFrom: $("#validFrom").val(),
        users: users,
        swm: swm,
        antivirus: antivirusLabel.is(":visible") && $("#antivirus").is(':checked'),
        activeSync: activeSyncLabel.is(":visible") && $("#activeSync").is(':checked'),
        antiSpam: antispamLabel.is(":visible") && $("#antispam").is(':checked'),
        webFilter: webFilterLabel.is(":visible") && $("#webFilter").is(':checked')
    };
    var price = calculateExistingPrice(currency, product, users, swm);
    var price2 = calculateExistingPrice2(currency, newProduct, oldProduct);
    $("#price").val(price);
    $("#price2").val(price2);
}

function setCompany() {
    var company = $("#company").val();
    if (company !== "") {
        sendGet("/info/?getPersonsFor=" + company, $("#persons"));
        sendGet("/info/?getIdFor=" + company, $("#companyID"));
        sendGet("/info/?getBusinessCasesFor=" + company, $("#businessCase"));
        $("#mainTable").show();
    } else {
        $("#mainTable").hide();
    }
}

function createEntityInRaynet() {
    serialize();
    var request = $("#request").val();
    $("#createEntityInRaynet").hide();
    var container = document.getElementById("businessCase");
    sendPut("/businessCase", request, container);
}

function sendPut(url, params, container) {
    serialize();
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200) {
            sendGet("/info/?getBusinessCasesFor=" + $("#company").val(), "businessCase");
            container.innerHTML = XmlHTTP.response;
            alert("Obchodní případ vytvořen");
        }
    };
    XmlHTTP.open("PUT", url, true);
    XmlHTTP.send(params);
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
    XmlHTTP.open("GET", "/companyList/", true);
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
        (dd > 9 ? '' : '0') + dd
    ].join('');
};