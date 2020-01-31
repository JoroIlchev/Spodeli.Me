$('#exampleModal').on('show.bs.modal', function (event) {
    let button = $(event.relatedTarget); // Button that triggered the modal
    let id = button.attr('data-offerId'); // Extract info from data-* attributes
    let categoryUrl = button.attr('data-categoryUrl'); // Extract info from data-* attributes
    console.log(id);
    console.log(categoryUrl);
    let getBaseInfo = ({
                           id, name, description, image, startsOn, endsOn, price, region, isActive, isApproved,
                           ownerPhoneNumber, user, categoryName
                       }) => {
        let baseInfo = `
            <p>Име: ${name}</p>
            <p>Описание: ${description}</p>
            <img src="${image}" width="40" height="40" alt="Product">
            <p>Офертата е валидна от: ${startsOn}</p>
            <p>Офертата е валидна до: ${endsOn}</p>
            <p>Цена: ${price} лв.</p>
            <p>Регион: ${region}</p>
            <p>Активна: ${isActive}</p>
            <p>Одобрена: ${isApproved}</p>
            <p>Телефон за връзка: ${ownerPhoneNumber}</p>
            <p>Потребител: ${user}</p>
            <p>Обявата е за категория: ${categoryName}</p>
            `;
        return baseInfo;
    };
    let modalBody = $('.modal-body');
    let url = `/api/${categoryUrl}/${id}`;
    modalBody.empty();
    fetch(url)
        .then(response => response.json())
        .then((offer) => {
            let result = ``;

            result += getBaseInfo(offer);
            if (categoryUrl === "clothes") {
                result += `<p>Размер: ${offer.size}</p>
                           <p>Състояние: ${offer.entityCondition}</p>
                           <p>Необходим ли е депозит: ${offer.depositNeeded}</p>
                           `;
            }
            if (categoryUrl === "powertools") {
                result += `<p>Състояние: ${offer.toolCondition}</p>
                                <p>Лесно преносим ли е: ${offer.isPortable}</p>
                                <p>Необходимо ли е допълнително оборудване: ${offer.isNeedExtraEquipment}</p>
                                <p>Необходимо ли са специални умения: ${offer.isNeedSpecialSkills}</p>
                           <p>Необходим ли е депозит: ${offer.depositNeeded}</p>
                           `;
            }
            if (categoryUrl === "realestates") {
                let extrasAsString = (offer.extras).join(', ');
                result += `<p>Тип на имота: ${offer.type}</p>
                                <p>Площ на имота: ${offer.area}</p>
                                <p>Екстри: ${extrasAsString}</p>
                                <p>Възможност за групови събирания: ${offer.isPartyFree}</p>
                           `;
            }
            if (categoryUrl === "vehicles") {

                result += `<p>Марка: ${offer.brand}</p>
                                <p>Модел: ${offer.model}</p>
                                <p>Гориво: ${offer.fuel}</p>
                                <p>Скоростна кутия: ${offer.gearBox}</p>
                                <p>Изминати километри: ${offer.kilometers}</p>
                                <p>Изисква ли се свидетелство за управление: ${offer.drivingLicenseNeeded}</p>
                                <p>Категория: ${offer.drivingCategory}</p>
                           `;
            }
            modalBody.append(result)
        });
    let modal = $(this);
    modal.find('.modal-title').text('Детайли за офертата');
    modal.find('.modal-body input').val(id)
});