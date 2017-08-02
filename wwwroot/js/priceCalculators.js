function calculateNewPrice(currency, newProduct) {
    var product = newProduct.product;
    var users = newProduct.users;
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

    if (newProduct.antivirus) extPrice += prices.antivirus;
    if (newProduct.activeSync) extPrice += prices.activeSync;
    if (newProduct.antiSpam) extPrice += prices.antiSpam;
    if (newProduct.webFilter) extPrice += prices.webFilter;

    var purchasePrice = basicPrice + expansionCount * prices.add5users + (expansionCount + 1) * extPrice;
    var nextYearPrice = (renPrice + expansionCount * prices.RenAdd5users + (expansionCount + 1) * extPrice);
    var result = (purchasePrice + (newProduct.swm - 1) * nextYearPrice) * getLicTypeModifier();   // norm, gov, edu

    if (newProduct.exWarranty) result += exWarPrice;

    return Math.round(100 * result) / 100;
}

function calculateExistingPrice(currency, newProduct, oldProduct) {
    var validFrom = new Date(newProduct.validFrom);
    var expirationDate = new Date(oldProduct.expirationDate);

    var timeDiff = expirationDate.getTime() - validFrom.getTime();
    var diffDays = Math.round(timeDiff / (1000 * 3600 * 24));
    var diffQuarters = Math.ceil(diffDays * 4 / 365);
    var existingExtensions = oldProduct.extensions;
    var prices = CZ_PRICES;
    if (currency === "16") prices = EUR_PRICES;

    var renPrice = 0;
    var existingExtPrice = 0;
    var addedExtPrice = 0;
    if (oldProduct.product === "0") return;
    if (diffQuarters > -100) {
        if (oldProduct.product === "Kerio Connect") renPrice = prices.RenConnectServer;
        if (oldProduct.product === "Kerio Control") renPrice = prices.RenControlServer;
        if (oldProduct.product === "Kerio Operator") renPrice = prices.RenOperatorServer;

        if (existingExtensions.indexOf("Antivirus") > -1 || existingExtensions.indexOf("Sophos") > -1)
            existingExtPrice += prices.antivirus;
        if (existingExtensions.indexOf("ActiveSync") > -1) existingExtPrice += prices.activeSync;
        if (existingExtensions.indexOf("AntiSpam") > -1) existingExtPrice += prices.antiSpam;
        if (existingExtensions.indexOf("WebFilter") > -1) existingExtPrice += prices.webFilter;
        if (newProduct.antivirus) addedExtPrice += prices.antivirus;
        if (newProduct.activeSync) addedExtPrice += prices.activeSync;
        if (newProduct.antiSpam) addedExtPrice += prices.antiSpam;
        if (newProduct.webFilter) addedExtPrice += prices.webFilter;

        var addedUsers5x = (newProduct.users - oldProduct.users) / 5;
        var oldUsers5x = oldProduct.users / 5;
        var prorate = diffQuarters / 4;
        var renewExisting = newProduct.swm *
            (renPrice - prices.RenAdd5users + oldUsers5x * (prices.RenAdd5users + existingExtPrice));

        var addedUsersPrice = addedUsers5x * (
            prices.add5users - prices.RenAdd5users +
            (prices.RenAdd5users + existingExtPrice) * (newProduct.swm + prorate)
        );
        var addedExtensionsPrice = newProduct.users / 5 * addedExtPrice * (newProduct.swm + prorate);
        var result = (renewExisting + addedUsersPrice + addedExtensionsPrice) * getLicTypeModifier();   // norm, gov, edu
        return Math.round(100 * result) / 100;
    } else
        return 0;
}

function getLicTypeModifier() {
    var result = 1;
    var lic_type = $("#lic_type").val();
    if (lic_type === " GOV") result = 0.9;
    if (lic_type === " EDU") result = 0.6;
    return result;
}