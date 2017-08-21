var companiesList;

function initForm() {
    getCompanyList();
    var date = new Date();
    var company = $("#company");
    $("#validFrom").val(date.format());
    date.setDate(date.getDate() + 15);
    $("#scheduledEnd").val(date.format());
    $("input").live("change", calculate);
    $(".cs-options").live("click", calculate);
    $("select").live("change", calculate);

    company.live("awesomplete-open", editCompany);
    company.live("awesomplete-selectcomplete", setCompany);
    $("#participant").live("awesomplete-selectcomplete", setParticipant);
    $("#opSwitch").on("change", opSwitch);
    // $("#offer").on("change", offerSelect);
}

function toJSONString() {
    var obj = {};
    var elements = $("#openForm").find("input, select, textarea");
    for (var i = 0; i < elements.length; ++i) {
        var name = elements[i].name;
        var value = elements[i].value;
        if (name) obj[name] = value;
    }
    return JSON.stringify(obj);
}

function getLicenseInfo() {  //need to be done via API
    var licenseNumber = $("#licenseNumber");
    var swUsers = $("#swUsers");
    var prod_group_id = $("#prod_group_id");
    var lic_type = $("#lic_type");
    var existingUsers = $("#existingUsers");
    var existingExtensions = $("#existingExtensions");
    var expirationDate = $("#expirationDate");

    var XmlHTTP = new XMLHttpRequest();

    licenseNumber.html("");
    prod_group_id.val("");
    lic_type.val(" ");
    existingUsers.html("");
    expirationDate.html("");
    existingExtensions.html("");

    XmlHTTP.open("GET", "/licenseInfo/?" + $("#licnum").val(), false);
    XmlHTTP.send();
    if (XmlHTTP.status === 200) {
        $("#existingProducts").show();
        $("#products").show();
        $("#existingProduct").show();

        var response = XmlHTTP.responseText;
        $("#response").val(response);
        var json = JSON.parse(response);
        var description = "";
        for (var i = 0; i < json.length; i++) {
            description += json[i] + "\n";
        }
        $("#description").html(description);
        licenseNumber.html(json[0]);
        prod_group_id.val(json[1]);
        lic_type.val(" " + json[2]);
        existingUsers.html(json[3]);
        expirationDate.html(json[4]);
        existingExtensions.html(json[5]);
        // end simulation

        var validFrom = new Date($("#validFrom").val());
        var expDate = new Date(json[4]);
        var timeDiff = expDate.getTime() - validFrom.getTime();
        var diffYears = Math.round(timeDiff / (1000 * 3600 * 24 * 365));
        var swm = $("#swm");
        swm.attr("min", "0");
        if (diffYears < 0) {
            swm.attr("max", 1 - diffYears);
        }
        swUsers.val(json[3]);
        // swUsers.attr("min", json[3]);
    }
    calculate();
}

function opSwitch() {
    var businessCase = $("#businessCase");
    var newOP = $("#newOP");
    var offersSeparateSwitchLabel = $("#offersSeparateSwitchLabel");
    var products = $("#products");
    var createEntityInRaynet = $("#createEntityInRaynet");
    if ($("#opSwitch").prop("checked")) {
        createEntityInRaynet.hide();
        newOP.hide();
        products.hide();
        offersSeparateSwitchLabel.hide();
        sendGet("/businessCase/?" + $("#companyId").val(), businessCase, false, businessCase);
    }
    else {
        businessCase.hide();
        $("#offerDiv").hide();
        createEntityInRaynet.show();
        newOP.show();
        $("#existingOP").hide();
        offersSeparateSwitchLabel.show();
        products.show();
    }
}

function businessCaseSelect() {
    var businessCaseId = $("#businessCase").val();

    $("#mailTo").hide();
    $("#downloadPDF").hide();
    $("#sendInvoiceInfo").hide();

    $("#existingOP").show();
    sendGet("/offer/?" + businessCaseId, $("#offer"), false, $("#offerDiv"));
}

function offerSelect() {
    var offerID = $("#offer").val();
    var downloadPDF = $("#downloadPDF");
    var mailTo = $("#mailTo");
    var sendInvoiceInfo = $("#sendInvoiceInfo");

    sendGet("/Pdf/?" + offerID, downloadPDF, true, downloadPDF);
    sendGet("/MailTo/?" + offerID, mailTo, true, mailTo);
    sendGet("/invoiceInfo/?" + offerID, sendInvoiceInfo, true, sendInvoiceInfo);
}

function newOrExistingLicenseToggle() {
    var swUsers = $("#swUsers");
    var licnum = $("#licnum");
    var getLicenseInfo = $("#getLicenseInfo");

    if ($("#existingLicense").prop("checked")) {
        licnum.show();
        getLicenseInfo.show();
        $("#lic_type").show();
        $("#prod_group_id").show();
        $("#productGroup").find(".cs-placeholder").hide();
        $("#lic_typeColumn").find(".cs-placeholder").hide();
    } else {
        licnum.hide();
        getLicenseInfo.hide();
        $("#existingProducts").hide();
        $("#lic_type").hide();
        $("#description").html(" ");
        swUsers.attr("min", "5");
        swUsers.val("5");
        $("#prod_group_id").hide();
        $("#productGroup").find(".cs-placeholder").show();
        $("#lic_typeColumn").find(".cs-placeholder").show();
    }
}

function calculate() {
    var productGroup = $("#prod_group_id").val();
    var boxProduct = $("#hardware").val();
    var offersSeparateSwitch = $("#offersSeparateSwitch");
    var productCode = "487";  //Kerio Control
    if (productGroup === "Kerio Connect") productCode = "486";
    if (productGroup === "Kerio Operator") productCode = "488";
    if (productGroup === "" && boxProduct === "V300") productCode = "488";
    $("#businessCaseCategory").val(productCode);
    if ($("#products").is(":visible")) {
        if ($("#existingLicense").prop("checked")) {
            fitElementsExistingLicense();
        } else {
            fitElements();
        }
    }
    var partnerPrice = ($("#price").val() * (100 - $("#partnerMargin").val())) / 100;

    $("#partnerPrice").val(Math.round(100 * partnerPrice) / 100);

    offersSeparateSwitch.val("false");
    if (offersSeparateSwitch.is(":checked")) {
        offersSeparateSwitch.val("true");
    }
    $("#request").val(toJSONString());
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

    if (product === "" && boxProduct !== "V300") result += ", Kerio Antivirus, Kerio Web Filter";

    if ($("#warrantyLabel").is(":visible") && $("#warranty").is(':checked')) result += " incl. Ext. Warranty";
    if ($("#antispamLabel").is(":visible") && $("#antispam").is(':checked')) result += ", AntiSpam";
    if ($("#antivirusLabel").is(":visible") && $("#antivirus").is(':checked')) result += ", Kerio Antivirus";
    if ($("#activeSyncLabel").is(":visible") && $("#activeSync").is(':checked')) result += ", ActiveSync";
    if ($("#webFilterLabel").is(":visible") && $("#webFilter").is(':checked')) result += ", Kerio Web Filter";

    if ($("#swUsersSection").is(":visible")) result += ", " + $("#swUsers").val() + " users";
    var swm = $("#swm").val();
    if (swm > "0") result += ", +" + swm + " year" + (swm > 1 ? "s" : "") + " SWM";
    if (product === "RMK300")
        result = "Kerio Rack Mount Kit for 300 Series HW";
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
        || (existingExtensions.indexOf("AntiSpam") > -1 )) result += ", Kerio AntiSpam";
    if ($("#antivirusLabel").is(":visible") && $("#antivirus").is(':checked')
        || (existingExtensions.indexOf("Sophos") > -1 || existingExtensions.indexOf("Antivirus") > -1)) result += ", Kerio Antivirus";
    if ($("#activeSyncLabel").is(":visible") && $("#activeSync").is(':checked')
        || existingExtensions.indexOf("ActiveSync") > -1) result += ", ActiveSync";
    if ($("#webFilterLabel").is(":visible") && $("#webFilter").is(':checked')
        || existingExtensions.indexOf("WebFilter") > -1) result += ", Kerio Web Filter";
    if ($("#swUsersSection").is(":visible")) result += ", " + $("#swUsers").val() + " users";
    var swm = $("#swm").val();
    if (swm > "0") result += ", +" + swm + " year" + (swm > 1 ? "s" : "") + " SWM";

    return result;
}

function getFullName() {
    if ($("#existingLicense").prop("checked")) {
        return getFullNameExistingLicense();
    } else {
        return getFullNameNewLicense();
    }
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

function addProduct() {
    for (var i = 1; i <= 3; i++) {
        var product = $("#product" + i);
        if (!product.is(":visible")) {
            $("#productFullName" + i).val(getFullName());
            $("#price" + i).val($("#price").val());
            $("#partnerPrice" + i).val($("#partnerPrice").val());
            product.show();
            calculate();
            return;
        }
    }
}

function removeProduct(itemNumber) {
    if (itemNumber === undefined) return;
    $("#product" + itemNumber).hide();
    $("#partnerPrice" + itemNumber).val("");
    $("#price" + itemNumber).val("");
    $("#productFullName" + itemNumber).val("");
    calculate();
}

function fitElements() {
    var productGroup = $("#prod_group_id").val();
    var swUsersSection = $("#swUsersSection");
    var antivirusLabel = $("#antivirusLabel");
    var activeSyncLabel = $("#activeSyncLabel");
    var antispamLabel = $("#antispamLabel");
    var webFilterLabel = $("#webFilterLabel");
    var warrantyLabel = $("#warrantyLabel");
    var swm = $("#swm").val();

    hideAll();

    if (productGroup === "0") return;
    var users = $("#swUsers").val();
    if (productGroup === "Kerio Connect") {
        antivirusLabel.show();
        activeSyncLabel.show();
        antispamLabel.show();
    }
    if (productGroup === "Kerio Control") {
        antivirusLabel.show();
        webFilterLabel.show();
    }
    if (productGroup === "") {
        warrantyLabel.show();
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
        activeSync: activeSyncLabel.is(":visible") && $("#activeSync").is(':checked'),
        antiSpam: antispamLabel.is(":visible") && $("#antispam").is(':checked'),
        webFilter: webFilterLabel.is(":visible") && $("#webFilter").is(':checked'),
        exWarranty: warrantyLabel.is(":visible") && $("#warranty").is(':checked')
    };

    var price = calculateNewPrice(currency, newProduct);
    price = Math.round(price * (100 - $("#discount").val())) / 100;
    $("#price").val(price);
    var fullName = getFullNameNewLicense();
    $("#productFullName").val(fullName);
    $("#businessCase").val(fullName);
}

function fitElementsExistingLicense() {
    var product = $("#prod_group_id").val();
    var currency = $("#currency").val();
    var swm = Number($("#swm").val());

    var antivirusLabel = $("#antivirusLabel");
    var activeSyncLabel = $("#activeSyncLabel");
    var antispamLabel = $("#antispamLabel");
    var webFilterLabel = $("#webFilterLabel");

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
        expirationDate: $("#expirationDate").html(),
        extensions: existingExtensions
    };

    var newProduct = {
        validFrom: $("#validFrom").val(),
        users: $("#swUsers").val(),
        swm: swm,
        antivirus: antivirusLabel.is(":visible") && $("#antivirus").is(':checked'),
        activeSync: activeSyncLabel.is(":visible") && $("#activeSync").is(':checked'),
        antiSpam: antispamLabel.is(":visible") && $("#antispam").is(':checked'),
        webFilter: webFilterLabel.is(":visible") && $("#webFilter").is(':checked')
    };

    var price = calculateExistingPrice(currency, newProduct, oldProduct);
    price = Math.round(price * (100 - $("#discount").val())) / 100;
    $("#price").val(price);
}

function editCompany() {
    $("#mainTable").hide();
    $("#newOP").hide();
    $("#existingProducts").hide();
    $("#products").hide();
    $("#person").hide();
}

function setCompany() {
    var company = $("#company").val();
    var person = $("#person");
    person.html("");
    var XmlHTTP = new XMLHttpRequest();
    if (company !== "") {
        XmlHTTP.open("GET", "/company/?" + company, false);
        XmlHTTP.send();
        if (XmlHTTP.status === 200) {
            var response = XmlHTTP.responseText.split(',');
            var id = response[0];
            $("#companyId").val(id);
            $("#mainTable").show();
            $("#newOP").show();
            $("#products").show();
            $("#owner").val(response[1]);
            $("#partnerMargin").val(response[2]);
            var personList = response[3];
            if (personList === "") alert("Není kontaktní osoba! Vyplňte v Raynetu");
            else {
                person.html(personList);
                person.show();
            }
            calculate();
        }
    }
}

function setParticipant() {
    var participant = $("#participant").val();
    var XmlHTTP = new XMLHttpRequest();
    if (participant !== "") {
        XmlHTTP.open("GET", "/company/?" + participant, false);
        XmlHTTP.send();
        if (XmlHTTP.status === 200) {
            var response = XmlHTTP.responseText.split(',');
            var id = response[0];
            $("#participantId").val(id);
        }
    }
}

function createEntityInRaynet() {
    $("#createEntityInRaynet").hide();
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200) {
            $("#businessCase").html(XmlHTTP.response);
            alert("Obchodní případ vytvořen");
        }
    };
    XmlHTTP.open("PUT", "/businessCase", true);
    XmlHTTP.send($("#request").val());
}

function getCompanyList() {
    var XmlHTTP;
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200) {
            var text = XmlHTTP.responseText;
            companiesList = JSON.parse(text);
            var client = document.getElementById("company");
            var awesompleteClient = new Awesomplete(client, {autoFirst: true, minChars: 1, maxItems: 20});
            awesompleteClient.list = companiesList;
            var participant = document.getElementById("participant");
            var awesompleteParticipant = new Awesomplete(participant, {autoFirst: true, minChars: 1, maxItems: 20});
            awesompleteParticipant.list = companiesList;
        }
    };
    XmlHTTP.open("GET", "/company/", true);
    XmlHTTP.send();
}

function sendGet(request, container, isHref, showElement) {
    var XmlHTTP;
    if (showElement) showElement.hide();
    if (window.XMLHttpRequest) XmlHTTP = new XMLHttpRequest();
    XmlHTTP.onreadystatechange = function () {
        if (XmlHTTP.readyState === 4 && XmlHTTP.status === 200) {
            if (isHref) container.attr("href", XmlHTTP.responseText);
            else container.html(XmlHTTP.responseText);
            if (showElement) showElement.show();
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