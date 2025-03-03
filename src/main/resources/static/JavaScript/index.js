import {serverAddUser, serverDeleteUser, serverGetRoles, serverGetUsers, serverUpdateUser} from "./api.js";
import {fillRoleSelects, render} from "./ui.js";

document.addEventListener("DOMContentLoaded", async function () {
    //Инициализация для админа
    if (document.location.pathname === "/admin") {
        console.log("Admin page loaded");

        try {
            const roles = await serverGetRoles();
            fillRoleSelects(roles)
            const users = await serverGetUsers();
            render(users);
        } catch (error) {
            console.error("Ошибка загрузки:", error);
        }
    }

    // Обработчик формы добавления пользователя
    document.getElementById("newUserForm").addEventListener("submit",
        async function (event) {
            event.preventDefault();
            console.log("Submitting new user form");

            let selectedRoles = Array.from(document.getElementById("roles").selectedOptions)
                .map(option => ({id: parseInt(option.value)}));

            let newUser = {
                firstName: document.getElementById("firstName").value,
                lastName: document.getElementById("lastName").value,
                username: document.getElementById("username").value,
                password: document.getElementById("password").value,
                roles: selectedRoles
            };

            try {
                let addedUser = await serverAddUser(newUser);
                console.log("User added:", addedUser);

                const users = await serverGetUsers();
                render(users);

                $('#usersTab').tab('show');

                document.getElementById("newUserForm").reset();
            } catch (error) {
                console.error("Ошибка добавления пользователя:", error);
            }
        });

    // Обработчик формы редактирования
    document.getElementById("submitEditUserForm").addEventListener("click",
        async function () {
            let selectedRoles = Array.from(document.getElementById("editRoles").selectedOptions)
                .map(option => ({id: parseInt(option.value)}));

            const userId = document.getElementById("editUserId").value.trim();
            const firstName = document.getElementById("editFirstName").value.trim();
            const lastName = document.getElementById("editLastName").value.trim();
            const username = document.getElementById("editUsername").value.trim();
            const password = document.getElementById("editPassword").value.trim();
            const roles = selectedRoles;

            if (!firstName || !lastName || !username || roles.length === 0) {
                alert("Please fill in all required fields.");
            } else {
                console.log("Updating user:", {userId, firstName, lastName, username, roles});

                await serverUpdateUser(userId, {firstName, lastName, username, password, roles});

                const users = await serverGetUsers();
                render(users);

                $('#editUserModal').modal('hide');
            }
        });

    // Обработчик формы удаления
    document.getElementById("submitDeleteUserForm").addEventListener("click",
        async function () {
            const userId = document.getElementById("deleteUserId").value.trim();
            if (userId) {
                try {
                    console.log(`User with ID ${userId} delete start....`);
                    const status = await serverDeleteUser(userId);
                    if (status === 204) {
                        console.log(`User with ID ${userId} deleted successfully.`);
                        const users = await serverGetUsers();
                        render(users);
                        $('#deleteUserModal').modal('hide');
                    }
                } catch (error) {
                    console.error("Ошибка удаления пользователя:", error);
                    alert("An error occurred while deleting the user.");
                }
            } else {
                alert("User ID is required for deletion.");
            }
        });
});