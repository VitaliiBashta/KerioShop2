/*jslint  browser: true, white: true, plusplus: true */
/*global $, countries */
var countriesArray
$(function () {
    'use strict';

    countriesArray = $.map(countries, function (value, key) { return { value: value, data: key }; });

    companiesList = getCompanyList();

    // Initialize autocomplete with custom appendTo:
    $('#autocomplete-dynamic').autocomplete({
        lookup: companiesList
    });
});