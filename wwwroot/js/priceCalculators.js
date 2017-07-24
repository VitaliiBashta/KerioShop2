function calculateNewPrice(currency, product, users, swm, extensions) {
    if (product === "0") return;
    var basicPrice = 0;
    var renPrice = 0;
    var expansionCount = 0;
    var extPrice = 0;
    var exWarPrice = 0;

    var prices = CZ_PRICES;
    if (currency === "16") prices = EUR_PRICES;

    if (product === "Kerio Connect") {
        expansionCount = users / 5 - 1;
        basicPrice = prices.connectServer;
        renPrice = prices.RenConnectServer;
    }
    if (product === "Kerio Control") {
        expansionCount = users / 5 - 1;
        basicPrice = prices.controlServer;
        renPrice = prices.RenControlServer;
    }
    if (product === "Kerio Operator") {
        expansionCount = users / 5 - 1;
        basicPrice = prices.operatorServer;
        renPrice = prices.RenOperatorServer;
    }
    if (product === "V300") {
        expansionCount = users / 5 - 1;
        basicPrice = prices.V300;
        renPrice = prices.RenV300;
        exWarPrice = prices.V300_war;
    }

    if (product === "NG100") {
        basicPrice = prices.NG100_unl;
        renPrice = prices.RenNG100_unl;
        exWarPrice = prices.NG100_war;
    }
    if (product === "NG100W") {
        basicPrice = prices.NG100W_unl;
        renPrice = prices.RenNG100_unl;
        exWarPrice = prices.NG100_war;
    }
    if (product === "NG300") {
        basicPrice = prices.NG300_unl;
        renPrice = prices.RenNG300_unl;
        exWarPrice = prices.NG300_war;
        if (users === "25 users") {
            basicPrice = prices.NG300_25;
            renPrice = prices.RenNG300_25;
        }
    }
    if (product === "NG300W") {
        basicPrice = prices.NG300W_unl;
        renPrice = prices.RenNG300_unl;
        exWarPrice = prices.NG300_war;
        if (users === "25 users") {
            basicPrice = prices.NG300W_25;
            renPrice = prices.RenNG300_25;
        }
    }
    if (product === "NG500") {
        basicPrice = prices.NG500_unl;
        renPrice = prices.RenNG500_unl;
        exWarPrice = prices.NG500_war;
        if (users === "50 users") {
            basicPrice = prices.NG500_50;
            renPrice = prices.RenNG500_50;
        }
        if (users === "100 users") {
            basicPrice = prices.NG500_100;
            renPrice = prices.RenNG500_100;
        }
    }

    if (extensions.indexOf("Antivirus") > -1) extPrice += prices.antivirus;
    if (extensions.indexOf("ActiveSync") > -1) extPrice += prices.activeSync;
    if (extensions.indexOf("AntiSpam") > -1) extPrice += prices.antiSpam;
    if (extensions.indexOf("WebFilter") > -1) extPrice += prices.webFilter;

    var purchasePrice = basicPrice + expansionCount * prices.add5users + (expansionCount + 1) * extPrice;
    var nextYearPrice = (renPrice + expansionCount * prices.RenAdd5users + (expansionCount + 1) * extPrice);
    var result = (purchasePrice + (swm - 1) * nextYearPrice) * getLicTypeModifier();   // norm, gov, edu

    if (extensions.indexOf("Warranty") > -1) result += exWarPrice;

    return Math.round(100 * result) / 100;
}

function calculateExistingPrice(currency, product, users, swm) {
    var validFrom = new Date($("#validFrom").val());
    var expirationDate = new Date($("#expirationDate").val());
    var timeDiff = expirationDate.getTime() - validFrom.getTime();
    var diffDays = Math.round(timeDiff / (1000 * 3600 * 24));
    var existingExtensions = $("#existingExtensions").text();
    var prices = CZ_PRICES;
    if (currency === "16") prices = EUR_PRICES;

    var renPrice = 0;
    var expansionCount = 0;
    var extPrice = 0;
    var addedExtPrice = 0;

    var addAntivirus = $("#antivirus").is(':checked');
    var addActiveSync = $("#activeSync").is(':checked');
    var addAntispam = $("#antispam").is(':checked');
    var addWebFilter = $("#webFilter").is(':checked');

    var addUsers = users - $("#existingUsers").html();
    $("#addUsers").val(addUsers);
    if (product === "0") return;
    if (diffDays > 0) {
        if (product === "Kerio Connect") {
            expansionCount = users / 5 - 1;
            renPrice = prices.RenConnectServer;
        }
        if (product === "Kerio Control") {
            expansionCount = users / 5 - 1;
            renPrice = prices.RenControlServer;
        }
        if (product === "Kerio Operator") {
            expansionCount = users / 5 - 1;
            renPrice = prices.RenOperatorServer;
        }

        if (existingExtensions.indexOf("Antivirus") > -1 || existingExtensions.indexOf("Sophos") > -1)
            extPrice += prices.antivirus;
        if (existingExtensions.indexOf("ActiveSync") > -1) extPrice += prices.activeSync;
        if (existingExtensions.indexOf("AntiSpam") > -1) extPrice += prices.antiSpam;
        if (existingExtensions.indexOf("WebFilter") > -1) extPrice += prices.webFilter;
        if (addAntivirus) addedExtPrice += prices.antivirus;
        if (addActiveSync) addedExtPrice += prices.activeSync;
        if (addAntispam) addedExtPrice += prices.antiSpam;
        if (addWebFilter) addedExtPrice += prices.webFilter;

        var nextYearPrice = (renPrice + expansionCount * prices.RenAdd5users + (expansionCount + 1) * extPrice
            + (addUsers / 5) * (prices.add5users + extPrice)) * swm +
            (users / 5) * addedExtPrice * (swm + 1);
        var result = (nextYearPrice) * getLicTypeModifier();   // norm, gov, edu
        return Math.round(100 * result) / 100;
    } else
        return 0;
}