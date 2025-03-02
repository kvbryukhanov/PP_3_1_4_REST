// Функция создания строки таблицы
export function createUserRow(user) {
    const $tr = document.createElement("tr");
    $tr.innerHTML = `
        <td>${user.id}</td>
        <td>${user.firstName}</td>
        <td>${user.lastName}</td>
        <td>${user.username}</td>
        <td>${user.roles.map(role => role.name).join("<br>") || "Нет ролей"}</td>
        <td>
            <button class="btn btn-info text-white edit-btn" data-toggle="modal" data-target="#editUserModal"
                data-user-id="${user.id}" data-first-name="${user.firstName}" 
                data-last-name="${user.lastName}" data-username="${user.username}" 
                data-roles="${user.roles.map(role => role.name).join(",")}">Edit</button>
        </td>
        <td>
            <button class="btn btn-danger text-white delete-btn" data-toggle="modal" data-target="#deleteUserModal"
                data-user-id="${user.id}" data-first-name="${user.firstName}"
                data-last-name="${user.lastName}" data-username="${user.username}"
                data-roles="${user.roles.map(role => role.name).join(",")}">Delete</button>
        </td>
    `;

    // Добавляем обработчики событий
    $tr.querySelector(".edit-btn").addEventListener("click", function () {
        fillEditForm(this);
    });

    $tr.querySelector(".delete-btn").addEventListener("click", function () {
        fillDeleteForm(this);
    });

    return $tr;
}

// Заполняет форму редактирования
export function fillEditForm(button) {
    document.getElementById("editUserId").value = button.dataset.userId;
    document.getElementById("editFirstName").value = button.dataset.firstName;
    document.getElementById("editLastName").value = button.dataset.lastName;
    document.getElementById("editUsername").value = button.dataset.username;

    let roles = button.dataset.roles.split(",");
    let roleSelect = document.getElementById("editRoles");
    for (let option of roleSelect.options) {
        option.selected = roles.includes(option.textContent.trim());
    }
}

// Заполняет форму удаления
export function fillDeleteForm(button) {
    document.getElementById("deleteUserId").value = button.dataset.userId;
    document.getElementById("deleteFirstName").value = button.dataset.firstName;
    document.getElementById("deleteLastName").value = button.dataset.lastName;
    document.getElementById("deleteUsername").value = button.dataset.username;

    let roles = button.dataset.roles.split(",");
    let roleSelect = document.getElementById("deleteRoles");
    for (let option of roleSelect.options) {
        option.selected = roles.includes(option.textContent.trim());
    }
}

// Отрисовывает таблицу
export function render(users) {
    const $userTable = document.getElementById("usersTableBody");
    $userTable.innerHTML = ''; // Очищаем таблицу
    users.forEach(user => $userTable.append(createUserRow(user)));
}