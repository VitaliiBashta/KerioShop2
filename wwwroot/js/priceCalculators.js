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
    var diffQuarters = Math.ceil(diffDays * 4 / 365);
    $("#quarters").val(diffQuarters);
    var existingExtensions = $("#existingExtensions").text();
    var prices = CZ_PRICES;
    if (currency === "16") prices = EUR_PRICES;

    var renPrice = 0;
    var existingExtPrice = 0;
    var addedExtPrice = 0;

    var addAntivirus = $("#antivirusLabel").is(":visible") && $("#antivirus").is(':checked');
    var addActiveSync = $("#activeSyncLabel").is(":visible") && $("#activeSync").is(':checked');
    var addAntispam = $("#antispamLabel").is(":visible") && $("#antispam").is(':checked');
    var addWebFilter = $("#webFilterLabel").is(":visible") && $("#webFilter").is(':checked');
    var existingUsers = $("#existingUsers").html();
    var addedUsers = users - existingUsers;
    $("#addUsers").val(addedUsers);
    if (product === "0") return;
    if (diffQuarters > 0) {
        if (product === "Kerio Connect") {
            renPrice = prices.RenConnectServer;
        }
        if (product === "Kerio Control") {
            renPrice = prices.RenControlServer;
        }
        if (product === "Kerio Operator") {
            renPrice = prices.RenOperatorServer;
        }
        if (existingExtensions.indexOf("Antivirus") > -1 || existingExtensions.indexOf("Sophos") > -1)
            existingExtPrice += prices.antivirus;
        if (existingExtensions.indexOf("ActiveSync") > -1) existingExtPrice += prices.activeSync;
        if (existingExtensions.indexOf("AntiSpam") > -1) existingExtPrice += prices.antiSpam;
        if (existingExtensions.indexOf("WebFilter") > -1) existingExtPrice += prices.webFilter;
        if (addAntivirus) addedExtPrice += prices.antivirus;
        if (addActiveSync) addedExtPrice += prices.activeSync;
        if (addAntispam) addedExtPrice += prices.antiSpam;
        if (addWebFilter) addedExtPrice += prices.webFilter;

        var renewExisting = swm * (renPrice + (existingUsers / 5 - 1) * prices.RenAdd5users + existingUsers / 5 * existingExtPrice);
        var prorata = diffQuarters / 4;
        var addedUsersPrice = (addedUsers / 5) * (prices.add5users - prices.RenAdd5users * (1 - prorata)) +
            (addedUsers / 5) * (prices.RenAdd5users * swm) +
            (addedUsers / 5) * existingExtPrice * prorata +
            (addedUsers / 5) * existingExtPrice * swm;
        var addedExtensionsPrice = users / 5 * addedExtPrice * swm +
            users / 5 * addedExtPrice * prorata;
        var result = (renewExisting + addedUsersPrice + addedExtensionsPrice) * getLicTypeModifier();   // norm, gov, edu
        return Math.round(100 * result) / 100;
    } else
        return 0;
}

function calculateExistingPrice2(currency, newProduct, oldProduct) {
    var validFrom2 = new Date(newProduct.validFrom);
    var expirationDate2 = new Date(oldProduct.expirationDate);

    var timeDiff2 = expirationDate2.getTime() - validFrom2.getTime();
    var diffDays = Math.round(timeDiff2 / (1000 * 3600 * 24));
    var diffQuarters = Math.ceil(diffDays * 4 / 365);
    var existingExtensions2 = oldProduct.extensions;
    var prices = CZ_PRICES;
    if (currency === "16") prices = EUR_PRICES;

    var renPrice = 0;
    var existingExtPrice = 0;
    var addedExtPrice = 0;
    var existingUsers2 = oldProduct.users;
    var addedUsers = newProduct.users - existingUsers2;
    if (oldProduct.product === "0") return;
    if (diffQuarters > 0) {
        if (oldProduct.product === "Kerio Connect") {
            renPrice = prices.RenConnectServer;
        }
        if (oldProduct.product === "Kerio Control") {
            renPrice = prices.RenControlServer;
        }
        if (oldProduct.product === "Kerio Operator") {
            renPrice = prices.RenOperatorServer;
        }
        if (existingExtensions2.indexOf("Antivirus") > -1 || existingExtensions2.indexOf("Sophos") > -1)
            existingExtPrice += prices.antivirus;
        if (existingExtensions2.indexOf("ActiveSync") > -1) existingExtPrice += prices.activeSync;
        if (existingExtensions2.indexOf("AntiSpam") > -1) existingExtPrice += prices.antiSpam;
        if (existingExtensions2.indexOf("WebFilter") > -1) existingExtPrice += prices.webFilter;
        if (newProduct.antivirus) addedExtPrice += prices.antivirus;
        if (newProduct.activeSync) addedExtPrice += prices.activeSync;
        if (newProduct.antiSpam) addedExtPrice += prices.antiSpam;
        if (newProduct.webFilter) addedExtPrice += prices.webFilter;

        var renewExisting = newProduct.swm * (renPrice + (existingUsers2 / 5 - 1) * prices.RenAdd5users + existingUsers2 / 5 * existingExtPrice);
        var prorata = diffQuarters / 4;
        var addedUsers5x = (addedUsers / 5);
        var addedUsersPrice = addedUsers5x * (prices.add5users - prices.RenAdd5users * (1 - prorata)) +
            addedUsers5x * (prices.RenAdd5users * newProduct.swm) +
            addedUsers5x * existingExtPrice * prorata +
            addedUsers5x * existingExtPrice * newProduct.swm;
        var addedExtensionsPrice = newProduct.users / 5 * addedExtPrice * (newProduct.swm + prorata);
        var result = (renewExisting + addedUsersPrice + addedExtensionsPrice) * getLicTypeModifier();   // norm, gov, edu
        return Math.round(100 * result) / 100;
    } else
        return 0;
}