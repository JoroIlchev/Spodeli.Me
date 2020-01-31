$(document).ready(function () {
    $('#allRadio').attr('checked', true);
    let table = $('#all-offers');
    fetch('/api/all/allOffers')
        .then((response) => response.json())
        .then((offers) => {
            table.empty();
            if (offers.length === 0) {
                table.append(`<div class="text-center"><p class="font-weight-bold mt-5">Нямате обяви публикувани в категорията</p></div>`)
                return;
            }
            offers.forEach(offer => {
                let row = $(`<tr class="row mx-auto text-center">`);
                row.append(`<td class="col-md-2" >${offer.name}`);
                row.append(`<td class="col-md-3" >${offer.startsOn} - ${offer.endsOn}</td>`);
                row.append(`<td class="col-md-1"><div>${offer.price}</div>`);
                if (offer.isActive) {
                    row.append(`<td class="col-md-1">Да`);
                } else {
                    row.append(`<td class="col-md-1">Не`);
                }
                if (offer.isApproved) {
                    row.append(`<td class="col-md-1">Да`);
                } else {
                    row.append(`<td class="col-md-1">Не`);
                }

                let tdButtons = $(`<td class="col-md-4">`)
                    .append(`<button type="button" class="btn btn-primary" data-toggle="modal" data-offerId="${offer.id}" data-categoryUrl="${offer.categoryUrl}" data-target="#exampleModal">Детайли</button>`);
                if (offer.isApproved) {
                    tdButtons.append(`<a data-offer-url="/api/${offer.categoryUrl}/${offer.id}/${offer.isApproved}" class="change-approved-status btn btn-warning font-weight-bold text-white btn-width-offers mx-1">Откажи</a>`);
                } else {
                    tdButtons.append(`<a data-offer-url="/api/${offer.categoryUrl}/${offer.id}/${offer.isApproved}" class="change-approved-status btn btn-success font-weight-bold text-white btn-width-offers mx-1">Одобри</a>`)
                }

                tdButtons.append(`<a data-offer-url="/api/${offer.categoryUrl}/delete/${offer.id}" class="delete-offer btn btn-danger font-weight-bold text-white btn-width-offers mx-1">Изтрий</a>`);
                row.append(tdButtons);
                table.append(row);
            });
        })
});

$('input[type=radio][name=selection]').change(function () {
    let category = $(this).val();
    console.log(category);
    let table = $('#all-offers');
    table.empty();
    fetch('/api/all/' + category)
        .then(response => response.json())
        .then((offers) => {

            if (offers.length === 0) {
                table.append(`<div class="text-center"><p class="font-weight-bold mt-5">Нямате обяви публикувани в категорията</p></div>`)
                return;
            }
            offers.forEach(offer => {

                let row = $(`<tr class="row mx-auto text-center">`);
                row.append(`<td class="col-md-2" >${offer.name}`);
                row.append(`<td class="col-md-3" >${offer.startsOn} - ${offer.endsOn}</td>`);
                row.append(`<td class="col-md-1"><div>${offer.price}</div>`);
                if (offer.isActive) {
                    row.append(`<td class="col-md-1">Да`);
                } else {
                    row.append(`<td class="col-md-1">Не`);
                }
                if (offer.isApproved) {
                    row.append(`<td class="col-md-1">Да`);
                } else {
                    row.append(`<td class="col-md-1">Не`);
                }

                let tdButtons = $(`<td class="col-md-4">`);
                tdButtons.append(`<button type="button" class="btn btn-primary" data-toggle="modal" data-offerId="${offer.id}" data-categoryUrl="${offer.categoryUrl}" data-target="#exampleModal">Детайли</button>`);

                const btn = currentButton(offer);
                tdButtons.append(btn);


                tdButtons.append(`<a data-offer-url="/api/${offer.categoryUrl}/delete/${offer.id}" class="delete-offer btn btn-danger font-weight-bold text-white btn-width-offers mx-1">Изтрий</a>`);
                row.append(tdButtons);
                table.append(row);
            });
        })

});

function currentButton(offer) {
    if (offer.isApproved) {

        return $(`<a data-offer-url="/api/${offer.categoryUrl}/${offer.id}/${offer.isApproved}" class="change-approved-status btn btn-warning font-weight-bold text-white btn-width-offers mx-1">Откажи</a>`);
    }
    return $(`<a data-offer-url="/api/${offer.categoryUrl}/${offer.id}/${offer.isApproved}" class="change-approved-status btn btn-success font-weight-bold text-white btn-width-offers mx-1">Одобри</a>`)

}

$('#all-offers').on('click', '.change-approved-status', function (ev) {
    const url = $(this).attr('data-offer-url');
    console.log(this.parentNode);
    fetch(url, {method: 'post'})
        .then();
    //TODO да покажа другия бутон за действие
    $(this).closest('tr').find('.change-approved-status').hide();



    ev.preventDefault();
    return false;
});
$('#all-offers').on('click', '.delete-offer', function (ev) {
    const url = $(this).attr('data-offer-url');
    console.log(url);
    fetch(url, {method: 'post'})
        .then();

    $(this).closest('tr').hide();
    ev.preventDefault();
    return false;
});