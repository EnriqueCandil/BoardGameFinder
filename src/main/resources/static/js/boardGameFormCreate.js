document.addEventListener("DOMContentLoaded", async () => {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Sesión no iniciada");
        window.location.href = "/login.html";
        return;
    }

    const payload = JSON.parse(atob(token.split('.')[1]));
    const role = payload.role || null;
    
    if(role != "ADMIN"){
		alert("No ADMIN detected");
		window.location.href = "/home.html";
		return;
	}
    
    const boardGameError = document.getElementById("boardGameError");


    try {
        const response = await fetch("http://localhost:8088/api/enums/all", {
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });

        	if (!response.ok) {
                const message = await response.text();
                throw new Error(message);
            }

        const enums = await response.json();

        for (const [key, values] of Object.entries(enums)) {
            if (key === "categories") {
                const container = document.getElementById("categoriesContainer");

                container.classList.add("d-flex", "flex-wrap", "gap-3");

                values.forEach(value => {
                    const div = document.createElement("div");
                    div.classList.add("form-check");

                    const checkbox = document.createElement("input");
                    checkbox.type = "checkbox";
                    checkbox.className = "form-check-input";
                    checkbox.name = "categories";
                    checkbox.value = value.toUpperCase();
                    checkbox.id = `cat-${value}`;

                    const label = document.createElement("label");
                    label.className = "form-check-label";
                    label.setAttribute("for", checkbox.id);
                    label.textContent = value;

                    div.appendChild(checkbox);
                    div.appendChild(label);
                    container.appendChild(div);
                });
            } else {
                const select = document.getElementById(key);
                if (select) {
                    values.forEach(value => {
                        const option = document.createElement("option");
                        option.value = value;
                        option.textContent = value;
                        select.appendChild(option);
                    });
                }
            }
        }
    } catch (error) {
        console.error(error);
        alert("Error al cargar los datos del formulario");
    }

    const form = document.getElementById("boardGameForm");
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        
        boardGameError.textContent = "";
        
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

        if (selectedCategories.length == 0) {
            mostrarError("Debe seleccionar al menos una categoría.");
            return;
        }

        const gameData = {
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
			document.getElementById("boardGameError").classList.add("d-none");
            const response = await fetch("http://localhost:8088/api/boardgames/create", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(gameData)
            });

            if (!response.ok) {
                const message = await response.text();
                throw new Error(message);
            }

            alert("Juego creado exitosamente");
            window.location.href = "/adminDashboard.html";
        } catch (err) {
            console.error(err);
            alert("Error al crear el juego");
        }
    });
	function mostrarError(mensaje) {
        boardGameError.textContent = mensaje;
        boardGameError.classList.remove("d-none");
        boardGameError.scrollIntoView({ behavior: "smooth" });
    }
});
