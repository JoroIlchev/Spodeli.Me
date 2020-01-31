$(document).ready(function () {
    $('#allRadio').attr('checked', true);
    let table = $('#my-offers');
    table.empty();
    fetch('/api/allOffers/all')
        .then(response => response.json())
        .then((offers) => {
                if (offers.length === 0){
                    table.append(`<div class="text-center"><p class="font-weight-bold mt-5">Нямате публикувани обяви</p></div>`)
                    return;
                }
                offers.forEach(offer => {
                    let row = $(`<tr class="row mx-auto text-center">`);
                    let tdFirst = $('<td class="col-md-1">')
                        .append(`<img src="${offer.image}" width="40" height="40" alt="Product">`);
                    row.append(tdFirst);
                    row.append(`<td class="col-md-2" >${offer.name}`);
                    row.append(`<td class="col-md-3" >${offer.startsOn} - ${offer.endsOn}</td>`);
                    row.append(`<td class="col-md-1"><div>${offer.price}</div>`);
                    row.append(`<td class="col-md-1">${offer.isActive}`);
                    row.append(`<td class="col-md-1">${offer.isApproved}`);
                    let tdButtons = $(`<td class="col-md-3">`)
                        .append(` <a href="/offer/${offer.categoryUrl}/edit/${offer.id}" class="btn btn-info font-weight-bold text-white btn-width-offers mx-1">Редакция</a>`)
                        .append(`<a href="/offer/${offer.categoryUrl}/delete/${offer.id}" class="btn btn-danger font-weight-bold text-white btn-width-offers mx-1">Изтрий</a>`);
                    row.append(tdButtons);
                    table.append(row);
                });

            }
        )

});

$('input[type=radio][name=selection]').change(function () {
    let category = $(this).val();
    let table = $('#my-offers');
    console.log(category);
    table.empty();
    fetch('/api/' + category + '/all')
        .then(response => response.json())
        .then((offers) => {
            if (offers.length === 0){
                table.append(`<div class="text-center"><p class="font-weight-bold mt-5">Нямате обяви публикувани в категорията</p></div>`)
                return;
            }
                offers.forEach(offer => {
                    let row = $(`<tr class="row mx-auto text-center">`);
                    let tdFirst = $('<td class="col-md-1">')
                        .append(`<img src="${offer.image}" width="40" height="40" alt="Product">`);
                    row.append(tdFirst);
                    row.append(`<td class="col-md-2" >${offer.name}`);
                    row.append(`<td class="col-md-3" >${offer.startsOn} - ${offer.endsOn}</td>`);
                    row.append(`<td class="col-md-1"><div>${offer.price}</div>`);
                    row.append(`<td class="col-md-1">${offer.isActive}`);
                    row.append(`<td class="col-md-1">${offer.isApproved}`);
                    let tdButtons = $(`<td class="col-md-3">`)
                        .append(` <a href="/offer/${offer.categoryUrl}/edit/${offer.id}" class="btn btn-info font-weight-bold text-white btn-width-offers mx-1">Редакция</a>`)
                        .append(`<a href="/offer/${offer.categoryUrl}/delete/${offer.id}" class="btn btn-danger font-weight-bold text-white btn-width-offers mx-1">Изтрий</a>`);
                    row.append(tdButtons);
                    table.append(row);
                });

            }
        )
});
