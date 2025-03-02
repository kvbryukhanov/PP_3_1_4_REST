const SERVER_URL = 'http://localhost:8080';

export async function serverGetUsers() {
    let response = await fetch(`${SERVER_URL}/api/admin/users`, {
        method: "GET",
        headers: {'Content-Type': 'application/json'}
    });
    return await response.json();
}

export async function serverAddUser(obj) {
    let response = await fetch(`${SERVER_URL}/api/admin/users`, {
        method: "POST",
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(obj)
    });
    return await response.json();
}

export async function serverUpdateUser(userId, userData) {
    try {
        let response = await fetch(`${SERVER_URL}/api/admin/users/${userId}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            new Error('Ошибка обновления пользователя');
        }

        let data = await response.json();
        console.log('User updated successfully:', data);

        return data; // Возвращаем обновленного пользователя, если нужно
    } catch (error) {
        console.error("Ошибка при обновлении пользователя:", error);
        alert("An error occurred while updating the user.");
    }
}

export async function serverGetRoles() {
    let response = await fetch(`${SERVER_URL}/api/admin/roles`, {
        method: "GET",
        headers: {'Content-Type': 'application/json'}
    });
    return await response.json();
}

export async function serverDeleteUser(userId) {
    const response = await fetch(`${SERVER_URL}/api/admin/users/${userId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Ошибка удаления: ${errorMessage}`);
    }

    return response.status;
}
