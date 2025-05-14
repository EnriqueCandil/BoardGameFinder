document.addEventListener("DOMContentLoaded", async () => {
    const token = localStorage.getItem("token");
    const gameId = localStorage.getItem("editBoardGameId");

    if (!token || !gameId) {
        alert("Sesión no iniciada o ID inválido");
        window.location.href = "/login.html";
        return;
    }

    const enums = await fetchEnums(token);
    const game = await fetchGameById(gameId, token);

    populateForm(enums, game);

    const form = document.getElementById("boardGameForm");
    form.addEventListener("submit", async (e) => {
        e.preventDefault();

		const idBoardGame = gameId;
        const name = document.getElementById("name").value.trim();
        const description = document.getElementById("description").value.trim();
        const imageUrl = document.getElementById("imageUrl").value.trim();
        const numPlayers = document.getElementById("numPlayers").value;
        const estTime = document.getElementById("estTime").value;
        const editorial = document.getElementById("editorial").value;
        const age = document.getElementById("age").value;

        const selectedCategories = Array.from(document.querySelectorAll("#categoriesContainer input:checked"))
            .map(cb => cb.value);
            
		if (!name || !description || !imageUrl || !numPlayers || !estTime || !editorial || !age) {
		    mostrarError("Por favor, complete todos los campos.");
		    return;
		}
		
		if (!isValidURL(imageUrl)) {
		    mostrarError("La URL de la imagen no es válida.");
		    return;
		}
		
		if (selectedCategories.length === 0) {
		    mostrarError("Debe seleccionar al menos una categoría.");
		    return;
		}

        const gameData = {
			idBoardGame,
            name,
            description,
            imageUrl,
            numPlayers,
            estTime,
            editorial,
            age,
            categories: selectedCategories
        };

        try {
			document.getElementById("formError").classList.add("d-none");
			
            const response = await fetch(`http://localhost:8088/api/boardgames/update`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(gameData)
            });

            if (!response.ok) throw new Error(await response.text());

            alert("Juego actualizado exitosamente");
            window.location.href = "/adminDashboard.html";
        } catch (err) {
            console.error(err);
            alert("Error al actualizar el juego");
        }
    });
});

function mostrarError(mensaje) {
    const errorDiv = document.getElementById("formError");
    errorDiv.textContent = mensaje;
    errorDiv.classList.remove("d-none");
    errorDiv.scrollIntoView({ behavior: "smooth" });
}

function isValidURL(url) {
    try {
        new URL(url);
        return true;
    } catch (_) {
        return false;
    }
}

async function fetchEnums(token) {
    const response = await fetch("http://localhost:8088/api/enums/all", {
        headers: {
			"Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    });

    if (!response.ok) throw new Error("No se pudieron cargar los enums");
    return await response.json();
}

async function fetchGameById(id, token) {
    const response = await fetch(`http://localhost:8088/api/boardgames/${id}`, {
        headers: {
			"Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        }
    });

    if (!response.ok) throw new Error("No se pudo cargar el juego");
    return await response.json();
}

function populateForm(enums, game) {
    for (const [key, values] of Object.entries(enums)) {
        if (key === "categories") continue;
        const select = document.getElementById(key);
        if (select) {
            select.innerHTML = "";
            values.forEach(value => {
                const option = document.createElement("option");
                option.value = value;
                option.textContent = value;
                if (value === game[key]) option.selected = true;
                select.appendChild(option);
            });
        }
    }

    document.getElementById("name").value = game.name;
    document.getElementById("description").value = game.description;
    document.getElementById("imageUrl").value = game.imageUrl;

    const container = document.getElementById("categoriesContainer");
    container.innerHTML = "";
    enums.categories.forEach(cat => {
        const div = document.createElement("div");
        div.classList.add("form-check");

        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.className = "form-check-input";
        checkbox.name = "categories";
        checkbox.value = cat;
        checkbox.id = `cat-${cat}`;
        if (game.categories.includes(cat)) checkbox.checked = true;

        const label = document.createElement("label");
        label.className = "form-check-label";
        label.setAttribute("for", checkbox.id);
        label.textContent = cat;

        div.appendChild(checkbox);
        div.appendChild(label);
        container.appendChild(div);
    });
    

}
